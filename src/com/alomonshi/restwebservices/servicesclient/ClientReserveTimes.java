package com.alomonshi.restwebservices.servicesclient;
import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.reservetimes.ReserveTimeService;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.ClientReservedTimes;
import com.alomonshi.object.views.JsonViews;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.fasterxml.jackson.annotation.JsonView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/reserveTime")
public class ClientReserveTimes {

    private ReserveTimeService reserveTimeService;

    /**
     * Getting list of reserve times in a single day and single unit
     * @param dateID intended date
     * @param unitID intended unit
     * @return list of requested reserve times
     */
    @GET
    @Path("/getUnitReserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(JsonViews.ClientViews.class)
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
        ReserveTime requestedTime = TableReserveTime.getReserveTime(reserveTime.getID());

        //Setting client id and requested services from requested data (parameter client id and service ids)
        requestedTime.setClientID(reserveTime.getClientID()).setServiceIDs(reserveTime.getServiceIDs());

        ServiceResponse serviceResponse = new ServiceResponse();
        reserveTimeService = new ReserveTimeService(serviceResponse);
        return reserveTimeService.registerReserveTime(requestedTime);
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
        ReserveTime requestedTime = TableReserveTime.getReserveTime(reserveTime.getID());
        ServiceResponse serviceResponse = new ServiceResponse();
        reserveTimeService = new ReserveTimeService(serviceResponse);
        return reserveTimeService.cancelReserveTime(requestedTime);
    }

    /**
     * Getting client reserved times
     * @param clientID intended client
     * @return list of client reserved times
     */
    @ClientSecured
    @GET
    @Path("/getClientReserveTimes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClientReservedTimes> getClientReservedTimes(@QueryParam("clientID") int clientID) {
        return ReserveTimeService.getClientReservedTimes(clientID);
    }
}