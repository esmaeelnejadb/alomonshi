package com.alomonshi.bussinesslayer.reservetimes;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;

import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.object.uiobjects.ClientReservedTimes;
import com.alomonshi.object.uiobjects.GenerateReserveTimeForm;
import com.alomonshi.restwebservices.message.ServerMessage;


public class ReserveTimeService{

    private GenerateReserveTimeForm generateReserveTimeForm;
    private ReserveTimeGenerator reserveTimeGenerator;
    private ReserveTimeDeletor reserveTimeDeletor;
    private ServiceResponse serviceResponse;

    public ReserveTimeService(ServiceResponse serviceResponse){
        this.serviceResponse = serviceResponse;
        reserveTimeGenerator = new ReserveTimeGenerator();
        reserveTimeDeletor = new ReserveTimeDeletor(serviceResponse);
    }

    public ReserveTimeService setGenerateReserveTimeForm (GenerateReserveTimeForm generateReserveTimeForm) {
        this.generateReserveTimeForm = generateReserveTimeForm;
        return this;
    }

    /**
     * Handling reserve times to be inserted in data base, for inserting a list of reserve times
     * firstly all old times will be deleted if no time has been reserved in that duration, if there was a time
     * service response is returned with setting response to false and list of reserved time for showing to user
     * if there was not, inserting process will be started and if all times insert correctly, server response
     * will be returned with true response, otherwise response will set to false with empty response data
     * @return service response
     */

	public synchronized ServiceResponse handleGeneratingReserveTime(){
	    try {
            //delete previous reserve times if exited
            serviceResponse = reserveTimeDeletor.deleteUnitReserveTimeBetweenDays(
                    generateReserveTimeForm.getUnitID(),
                    generateReserveTimeForm.getStartDate(),
                    generateReserveTimeForm.getEndDate());
            //If there are no reserved times in requested days
            if (serviceResponse.getResponse()){
                List<ReserveTime> reserveTimes = reserveTimeGenerator
                        .setGenerateReserveTimeForm(generateReserveTimeForm)
                        .generateAllDayReserveTimes();
                boolean response = reserveTimes != null && TableReserveTime.insertReserveTimeList(reserveTimes);
                serviceResponse.setResponse(response);
                if (response)
                    serviceResponse.setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    serviceResponse.setMessage(ServerMessage.FAULTMESSAGE);
            }else
                serviceResponse.setMessage(ServerMessage.RESERVETIMEERROR_01);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception in generating reserve times " + e);
            serviceResponse.setMessage(ServerMessage.FAULTMESSAGE);
        }
	    return serviceResponse;
	}

    /**
     * Reserve a time for client
     * @param reserveTime time to be reserved
     * @return service response
     */
	public synchronized ServiceResponse registerReserveTime(ReserveTime reserveTime) {
        ReserveTimeCheck reserveTimeCheck = new ReserveTimeCheck(reserveTime, serviceResponse);
	    if (reserveTimeCheck.checkTimeAvailability().getResponse()) {
            if (ClientReserveTimeHandler.setNewReserveTime(reserveTime)) {
                serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            }else
                serviceResponse.setResponse(false).setMessage(ServerMessage.RESERVETIMEERROR_02);
        }
        return serviceResponse;
    }

    /**
     * Cancel reserve time
     * @param reserveTime to be canceled
     * @return service response to be returned
     */

    public synchronized ServiceResponse cancelReserveTime(ReserveTime reserveTime) {
	    if (ClientReserveTimeHandler.cancelClientReserveTime(reserveTime))
	        serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
	    else
	        serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
	    return serviceResponse;
    }

    /**
     * Getting client reserved times
     * @param clientID intended client id
     * @return list of client reserved times
     */

    public static List<ClientReservedTimes> getClientReservedTimes(int clientID) {
        return TableReserveTime.getClientReservedTimes(clientID);
    }
}