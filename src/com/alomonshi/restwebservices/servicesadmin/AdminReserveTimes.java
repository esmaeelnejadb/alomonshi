package com.alomonshi.restwebservices.servicesadmin;
import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.changeaccesscheck.CheckAuthority;
import com.alomonshi.bussinesslayer.reservetimes.ReserveTimeService;
import com.alomonshi.object.uiobjects.GenerateReserveTimeForm;
import com.alomonshi.restwebservices.annotation.CompanyAdminSecured;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;
import com.alomonshi.restwebservices.message.ServerMessage;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/adminReserveTime")
public class AdminReserveTimes {

    private CheckAuthority checkAuthority;
    private ReserveTimeService reserveTimeService;

    /**
     * Getting input form data for generating new reserve times
     * @param generateReserveTimeForm input data
     * @return true id all time generated truly
     */
    @CompanySubAdminSecured
    @POST
    @Path("/generateReserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceResponse generateReserveTimes(@NotNull GenerateReserveTimeForm generateReserveTimeForm){
        checkAuthority = new CheckAuthority(generateReserveTimeForm.getUserID(), generateReserveTimeForm.getUnitID());
        ServiceResponse serviceResponse = new ServiceResponse();
        if (checkAuthority.isUserUnitAuthorized()) {
            reserveTimeService = new ReserveTimeService(generateReserveTimeForm, serviceResponse);
            return reserveTimeService.handleGeneratingReserveTime();
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
    }
}