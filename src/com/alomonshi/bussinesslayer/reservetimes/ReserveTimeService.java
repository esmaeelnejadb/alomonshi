package com.alomonshi.bussinesslayer.reservetimes;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;

import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.ClientReservedTime;
import com.alomonshi.object.uiobjects.ReserveTimeForm;
import com.alomonshi.object.uiobjects.ReserveTimeList;
import com.alomonshi.restwebservices.message.SMSMessage;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.utility.sendsms.SMSUtils;


public class ReserveTimeService{

    private ReserveTimeForm reserveTimeForm;
    private ServiceResponse serviceResponse;
    private ReserveTimeDeletor reserveTimeDeletor;

    public ReserveTimeService(ServiceResponse serviceResponse){
        this.serviceResponse = serviceResponse;
    }

    /**
     * Set generate reserve times object
     * @param reserveTimeForm to be set
     * @return this object
     */
    public ReserveTimeService setReserveTimeForm(ReserveTimeForm reserveTimeForm) {
        this.reserveTimeForm = reserveTimeForm;
        return this;
    }

    /**
     * Getting admin reserve times in a day
     * @param reserveTime that intended unit and day have been drived from
     * @return list of reserve times in a day
     */
    public ServiceResponse getAdminUnitReserveDayTimes(ReserveTime reserveTime){
        Map<MiddayID, List<ReserveTime>> reserveTimes = TableReserveTime.getAdminUnitReserveTimeInADay(
                reserveTime.getDateID(), reserveTime.getUnitID());
        if (reserveTimes.isEmpty()) {
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.RESERVETIMEERROR_05);
        }else
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(reserveTimes);
    }

    /**
     * Handling reserve times to be inserted in data base, for inserting a list of reserve times
     * firstly all old times will be deleted if no time has been reserved in that duration, if there was a time
     * service response is returned with setting response to false and list of reserved time for showing to user
     * if there was not, inserting process will be started and if all times insert correctly, server response
     * will be returned with true response, otherwise response will set to false with empty response data
     * @return service response
     */
	public ServiceResponse handleGeneratingReserveTime(){
	    try {
            ReserveTimeGenerator reserveTimeGenerator = new ReserveTimeGenerator();
            List<ReserveTime> reserveTimes = reserveTimeGenerator
                    .setReserveTimeForm(reserveTimeForm)
                    .generateReserveTimes();
            //If input data are correct and time have been generated
            if (!reserveTimes.isEmpty()) {
                reserveTimeDeletor = new ReserveTimeDeletor(serviceResponse);
                //delete previous reserve times if exited in a day
                if (reserveTimeForm.getMidday() == null)
                    serviceResponse = reserveTimeDeletor.deleteUnitDayReserveTimeBetweenDays(reserveTimeForm);
                //delete previous reserve times if exited in a midday
                else
                    serviceResponse = reserveTimeDeletor.deleteUnitMiddayReserveTimeBetweenDays(reserveTimeForm);
                //if not reserved times are existed in the period
                if (serviceResponse.getResponse()){
                    //If inserting process has been done correctly
                    if (TableReserveTime.insertReserveTimeList(reserveTimes))
                        serviceResponse.setMessage(ServerMessage.SUCCESSMESSAGE);
                    else
                        serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
                }else
                    serviceResponse.setMessage(ServerMessage.RESERVETIMEERROR_01);
            }
            else
                serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);

        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception in generating reserve times " + e);
            serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }
	    return serviceResponse;
	}

    /**
     * Deleting reserve time in all days
     * @return service response
     */
	public ServiceResponse deleteReserveTimes(){
	    reserveTimeDeletor = new ReserveTimeDeletor(serviceResponse);
	    //if all day reserve times should be deleted
	    if (reserveTimeForm.getMidday() == null)
	        return reserveTimeDeletor.deleteUnitDayReserveTimeBetweenDays(reserveTimeForm);
	    else // if midday reserve times should be deleted
	        return reserveTimeDeletor.deleteUnitMiddayReserveTimeBetweenDays(reserveTimeForm);
    }

    /**
     * Reserve a time for client
     * @param reserveTime time to be reserved
     * @return service response
     */
	public ServiceResponse registerReserveTime(ReserveTime reserveTime) {
        ReserveTimeCheck reserveTimeCheck = new ReserveTimeCheck(reserveTime, serviceResponse);
	    if (reserveTimeCheck.checkTimeAvailability().getResponse()
                && reserveTimeCheck.isServicesBelongToReserveTimeUnit().getResponse()) {
            if (ClientReserveTimeHandler.setNewReserveTime(reserveTime)) {
                serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            }else
                serviceResponse.setResponse(false).setMessage(ServerMessage.RESERVETIMEERROR_02);
        }
        return serviceResponse;
    }

    /**
     * Cancel reserve time by client
     * @param reserveTime to be canceled
     * @return service response to be returned
     */

    public ServiceResponse cancelReserveTime(ReserveTime reserveTime) {
        ReserveTimeCheck reserveTimeCheck = new ReserveTimeCheck(
                reserveTime
                , serviceResponse);
        if (reserveTimeCheck.isTimeCancelable().getResponse()) {
            ClientReservedTime clientReservedTime = TableReserveTime.getClientReservedTime(reserveTime.getID());
            if (ClientReserveTimeHandler.cancelClientReserveTime(reserveTime)) {
                //Sending sms to client
                String[] toNumber = {TableClient.getUser(reserveTime.getClientID()).getPhoneNo()};
                SMSUtils.sendSMS(toNumber, SMSMessage.getClientCancelTimeMessage(clientReservedTime));
                serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            }else
                serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
            return serviceResponse;
        }else
            return serviceResponse.setMessage(ServerMessage.INTERNALERRORMESSAGE);
    }

    /**
     * Getting client reserved times
     * @param clientID intended client id
     * @return list of client reserved times
     */

    public static List<ClientReservedTime> getClientReservedTimes(int clientID) {
        return TableReserveTime.getClientReservedTimes(clientID);
    }

    /**
     * Cancel single reservable times got from ui
     * (Changes reserve times status from RESERVABLE to CANCELED)
     * @param reserveTimes to be deleted
     * @return service response
     */

    public ServiceResponse retrieveSingleCanceledTimes(ReserveTimeList reserveTimes){
        List<ReserveTime> reserveTimeList = new ArrayList<>();//List of reserve times to be canceled
        if (checkAndFillCanceledTimesFromIDs(reserveTimeList, reserveTimes)) {
            if (TableReserveTime.retrieveCanceledTimeList(reserveTimeList))
                return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }else {
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Cancel single reservable times got from ui
     * (Changes reserve times status from RESERVABLE to CANCELED)
     * @param reserveTimes to be deleted
     * @return service response
     */

    public ServiceResponse cancelSingleReservableTimes(ReserveTimeList reserveTimes){
        List<ReserveTime> reserveTimeList = new ArrayList<>();//List of reserve times to be canceled
        if (checkAndFillReserveTimesFromIDs(reserveTimeList, reserveTimes)) {
            if (TableReserveTime.cancelReservableTimeList(reserveTimeList))
                return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);

        }else {
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Cancel times which had been reserved by clients before
     * (Set their status from RESERVED to RESERVABLE)
     * @param reserveTimes to be canceled
     * @return service response
     */
    public ServiceResponse cancelSingleReservedTimes(ReserveTimeList reserveTimes) {
        List<ReserveTime> reserveTimeList = new ArrayList<>();//List of reserved times to be canceled
        if (checkAndFillReservableTimesFromIDs(reserveTimeList, reserveTimes)) {
            //not success times will be send to UI
            return cancelReservedTimeListFromAdmin(reserveTimeList);
        }else {
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Cancel reserved times between days from admin
     * @return service response
     */
    public ServiceResponse cancelReservedTimeBetweenDays() {
        //List of reserved times to be canceled
        List<ReserveTime> reserveTimeList = getListOfReservedTimesBetweenDaysForDelete();
        if (!reserveTimeList.isEmpty()) {
            return cancelReservedTimeListFromAdmin(reserveTimeList);
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.RESERVETIMEERROR_06);

    }

    /**
     *
     * Checking and Filling reserve time list got from ui with their times got from data base and two checks will been done :
     * 1- checking if reserve time unit id belong to the admin unit
     * 2- checking that non of reserve time has RESERVED status
     * @param reserveTimes to be filled and checked
     */
    private boolean checkAndFillReserveTimesFromIDs(List<ReserveTime> reserveTimeList, ReserveTimeList reserveTimes) {
        for (int i = 0; i < reserveTimes.getReserveTimeIDs().size(); i++) {
            //Getting each reserve time object from table and check its unit id with gog unit id from UI
            reserveTimeList.add(i, TableReserveTime.getReserveTime(reserveTimes.getReserveTimeIDs().get(i)));
            if (reserveTimeList.get(i).getStatus() == ReserveTimeStatus.RESERVED
                    || reserveTimeList.get(i).getStatus() == ReserveTimeStatus.CANCELED
                    || reserveTimeList.get(i).getUnitID() != reserveTimes.getUnitID())
                return false;
        }
        return true;
    }

    /**
     * Checking and Filling reserve time list got from ui with their times got from data base and two checks will been done :
     * 1- checking if reserve time unit id belong to the admin unit
     * 2- checking that all of reserve times have CANCELED status
     * @param reserveTimes to be filled and checked
     */
    private boolean checkAndFillCanceledTimesFromIDs(List<ReserveTime> reserveTimeList, ReserveTimeList reserveTimes) {
        for (int i = 0; i < reserveTimes.getReserveTimeIDs().size(); i++) {
            //Getting each reserve time object from table and check its unit id with gog unit id from UI
            reserveTimeList.add(i, TableReserveTime.getReserveTime(reserveTimes.getReserveTimeIDs().get(i)));
            if (reserveTimeList.get(i).getStatus() != ReserveTimeStatus.CANCELED
                    || reserveTimeList.get(i).getUnitID() != reserveTimes.getUnitID())
                return false;
        }
        return true;
    }

    /**
     * Filling reserved time list from their ids got from data base and two checks will been done :
     * 1- checking if reserve time unit id belong to the admin unit
     * 2- checking that all of reserve times have RESERVED status
     * @param reserveTimes to be filled and checked
     */
    private boolean checkAndFillReservableTimesFromIDs(List<ReserveTime> reserveTimeList, ReserveTimeList reserveTimes) {
        for (int i = 0; i < reserveTimes.getReserveTimeIDs().size(); i++) {
            //Getting each reserve time object from table and check its unit id with got unit id from UI
            reserveTimeList.add(i, TableReserveTime.getReserveTime(reserveTimes.getReserveTimeIDs().get(i)));
            if (reserveTimeList.get(i).getStatus() != ReserveTimeStatus.RESERVED
                    || reserveTimeList.get(i).getUnitID() != reserveTimes.getUnitID())
                return false;
        }
        return true;
    }

    /**
     * Cancel a list of reserved times from admin and send sms to client
     * @param reserveTimeList to be canceled
     * @return service response
     */

    private ServiceResponse cancelReservedTimeListFromAdmin(List<ReserveTime> reserveTimeList) {
        //Getting not successful canceled times
        List<ClientReservedTime> notSuccessTimes = new ArrayList<>();
        for (ReserveTime reserveTime: reserveTimeList) {
            //Cancel Reserve Time
            ClientReservedTime clientReservedTime = TableReserveTime.getClientReservedTime(reserveTime.getID());
            if (ClientReserveTimeHandler.cancelClientReserveTime(reserveTime)) {
                //Getting client phone number from table
                String[] toNumber = {TableClient.getUser(reserveTime.getClientID()).getPhoneNo()};
                //Sending canceling message to client
                SMSUtils.sendSMS(toNumber, SMSMessage.getAdminCancelTimeMessage(clientReservedTime));
            }else
                notSuccessTimes.add(clientReservedTime);
        }
        if (notSuccessTimes.isEmpty())
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.FAULTMESSAGE)
                    .setResponseData(notSuccessTimes);
    }

    /**
     * Getting list of reserved time for delete;
     * @return should be deleted reserved times
     */
    private List<ReserveTime> getListOfReservedTimesBetweenDaysForDelete() {
        if (reserveTimeForm.getMidday() != null)
            return TableReserveTime
                    .getUnitMiddayReservedTimesBetweenDays(reserveTimeForm);
        else
            return TableReserveTime
                    .getUnitReservedTimesBetweenDays(reserveTimeForm);

    }
}
