package com.alomonshi.restwebservices.servicesclient;
import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.reservetimes.ReserveTimeService;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/reserveTime")
public class ClientReserveTimes {

    private ReserveTimeService reserveTimeService;
    private ServiceResponse serviceResponse;

    @Context
    HttpServletResponse httpServletResponse;
    /**
     * Getting list of reserve times in a single day and single unit
     * @param dateID intended date
     * @param unitID intended unit
     * @return list of requested reserve times
     */
    @JsonView(JsonViews.ClientViews.class)
    @GET
    @Path("/getUnitReserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Enum, List<ReserveTime>> getReserveTime(@QueryParam("dateID") int dateID, @QueryParam("unitID") int unitID) {
        try {
            return TableReserveTime.getClientUnitReserveTimeInADay(dateID, unitID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     * Register time for a client
     * @param reserveTime to be reserved
     * @return service response
     */
    @ClientSecured
    @POST
    @Path("/registerReserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceResponse registerReserveTime(ReserveTime reserveTime){
        //Getting request time information from requested data (parameter id)
        serviceResponse = new ServiceResponse();
        try {
            ReserveTime requestedTime = TableReserveTime.getReserveTime(reserveTime.getID());
            //Setting client id and requested services from requested data (parameter client id and service ids)
            requestedTime.setClientID(reserveTime.getClientID());
            requestedTime.setServiceIDs(reserveTime.getServiceIDs());
            reserveTimeService = new ReserveTimeService(serviceResponse);
            return reserveTimeService.registerReserveTime(requestedTime);
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot register time " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Cancel a reserve time
     * @param reserveTime to be canceled
     * @return service response
     */
    @ClientSecured
    @POST
    @Path("/cancelReserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceResponse cancelReserveTime(ReserveTime reserveTime) {
        serviceResponse = new ServiceResponse();
        try {
            ReserveTime requestedTime = TableReserveTime.getReserveTime(reserveTime.getID());
            reserveTimeService = new ReserveTimeService(serviceResponse);
            return reserveTimeService.cancelReserveTime(requestedTime);
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot cancel time " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/registerReserveTime")
    public void doOptionsForRegisterTime() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/cancelReserveTime")
    public void doOptionsForCancelTime() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

}