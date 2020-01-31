package com.alomonshi.restwebservices.adminservices;
import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.reservetimes.ReserveTimeService;
import com.alomonshi.object.uiobjects.GenerateReserveTimeForm;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/adminPanel/adminReserveTime")
public class AdminReserveTimes {

    private ReserveTimeService reserveTimeService;

    /**
     * Getting input form data for generating new reserve times
     * @param generateReserveTimeForm input data
     * @return true id all time generated truly
     */

    @POST
    @Path("/generateReserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceResponse generateReserveTimes(GenerateReserveTimeForm generateReserveTimeForm){
        ServiceResponse serviceResponse = new ServiceResponse();
        reserveTimeService = new ReserveTimeService(generateReserveTimeForm, serviceResponse);
        return reserveTimeService.handleGeneratingReserveTime();
    }
}