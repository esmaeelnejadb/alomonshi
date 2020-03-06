package com.alomonshi.restwebservices.servicesadmin;
import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck.CheckAdminAuthority;
import com.alomonshi.bussinesslayer.reservetimes.ReserveTimeService;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.ReserveTimeForm;
import com.alomonshi.object.uiobjects.ReserveTimeList;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/adminReserveTime")
public class AdminReserveTimesWebService {

    private ReserveTimeService reserveTimeService;
    private ServiceResponse serviceResponse;

    @Context
    HttpServletResponse httpServletResponse;

    @JsonView(JsonViews.SubAdminViews.class)
    @CompanySubAdminSecured
    @POST
    @Path("/getUnitReserveTimes")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getUnitReserveTime(ReserveTime reserveTime){
        serviceResponse = new ServiceResponse();
        try {
            //Checking id admin can change the unit
            if(CheckAdminAuthority.isUserUnitAuthorized(reserveTime.getClientID()
                    , reserveTime.getUnitID())) {
                reserveTimeService = new ReserveTimeService(serviceResponse);
                return reserveTimeService.getAdminUnitReserveDayTimes(reserveTime);
            }else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.ACCESSFAULT);

        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not get reserve times " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * generating new reserve times
     * @param reserveTimeForm input data
     * @return true id all time generated truly
     */
    @JsonView(JsonViews.SubAdminViews.class)
    @CompanySubAdminSecured
    @POST
    @Path("/reserveTimes")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse generateReserveTimes(ReserveTimeForm reserveTimeForm){
        serviceResponse = new ServiceResponse();
        try {
            //Checking id admin can change the unit
            if (CheckAdminAuthority.isUserUnitAuthorized(reserveTimeForm.getClientID()
                    , reserveTimeForm.getUnitID())) {
                reserveTimeService = new ReserveTimeService(serviceResponse)
                        .setReserveTimeForm(reserveTimeForm);
                return reserveTimeService.handleGeneratingReserveTime();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not generate new times " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Deleting reserve times between days
     * @param reserveTimeForm to be deleted information
     * @return service response
     */

    @JsonView(JsonViews.SubAdminViews.class)
    @CompanySubAdminSecured
    @DELETE
    @Path("/reserveTimes")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse deleteUnitReserveTimes(ReserveTimeForm reserveTimeForm){
        serviceResponse = new ServiceResponse();
        try {
            //Checking id admin can change the unit
            if (CheckAdminAuthority.isUserUnitAuthorized(reserveTimeForm.getClientID()
                    , reserveTimeForm.getUnitID())) {
                reserveTimeService = new ReserveTimeService(serviceResponse)
                        .setReserveTimeForm(reserveTimeForm);
                return reserveTimeService.deleteReserveTimes();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot delete times " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Canceling single reservable times in a day (Changes their status from RESERVABLE to CANCELED)
     * (note: canceled times can be retrieved later)
     * @param reserveTimes to be canceled
     * @return service response
     */
    @CompanySubAdminSecured
    @DELETE
    @Path("/reserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse cancelSingleReservableTimes(ReserveTimeList reserveTimes) {
        serviceResponse = new ServiceResponse();
        try {
            //Checking id admin can change the unit
            if (CheckAdminAuthority.isUserUnitAuthorized(reserveTimes.getClientID()
                    , reserveTimes.getUnitID())) {
                reserveTimeService = new ReserveTimeService(serviceResponse);
                return reserveTimeService.cancelSingleReservableTimes(reserveTimes);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);

        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot cancel times " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Retrieving single canceled times in a day (Changes their status from CANCELED to RESERVABLE)
     * @param reserveTimes to be retrieved
     * @return service response
     */
    @CompanySubAdminSecured
    @POST
    @Path("/reserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse retrieveSingleCanceledTimes(ReserveTimeList reserveTimes) {
        serviceResponse = new ServiceResponse();
        try {
            //Checking id admin can change the unit
            if (CheckAdminAuthority.isUserUnitAuthorized(reserveTimes.getClientID()
                    , reserveTimes.getUnitID())) {
                reserveTimeService = new ReserveTimeService(serviceResponse);
                return reserveTimeService.retrieveSingleCanceledTimes(reserveTimes);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);

        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot retrieve times " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Cancel client reserved times in a day (Change their status from RESERVED to RESERVABLE)
     * (note: this service is considered to inform clients separately from their reserved times
     * have been canceled )
     * @param reserveTimeList to be canceled
     * @return service response
     */

    @CompanySubAdminSecured
    @PUT
    @Path("/reserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse cancelClientReservedTimes(ReserveTimeList reserveTimeList) {
        serviceResponse = new ServiceResponse();
        try {
            reserveTimeService = new ReserveTimeService(serviceResponse);
            if (CheckAdminAuthority.isUserUnitAuthorized(reserveTimeList.getClientID()
                    , reserveTimeList.getUnitID()))
                return reserveTimeService.cancelSingleReservedTimes(reserveTimeList);
            else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);

        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot cancel reserved times " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/getUnitReserveTimes")
    public void doOptionsForAdminGettingReserveTimes() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/reserveTimes")
    public void doOptionsForAdminReserveTimes() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/reserveTime")
    public void doOptionsForAdminReserveTime() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

}