package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.clientinfo.ClientProfile;
import com.alomonshi.bussinesslayer.reservetimes.ReserveTimeService;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.uiobjects.ChangePassword;
import com.alomonshi.object.uiobjects.ClientReservedTimes;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/user")
public class ClientProfileWebService {

    private ServiceResponse serviceResponse;
    private ClientProfile clientProfile;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * Get client profile info
     * @param clientID to be got information from
     * @return client information
     */
    @JsonView(JsonViews.ClientViews.class)
    @ClientSecured
    @GET
    @Path("/clientProfile")
    @Produces(MediaType.APPLICATION_JSON)
    public Users getClientProfile(@QueryParam("clientID") int clientID) {
        return ClientProfile.getClientInfo(clientID);
    }

    /**
     * Updating user profile
     * @param user to be updated
     * @return update result
     */
    @ClientSecured
    @PUT
    @Path("/clientProfile")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse updateClientProfile(Users user) {
        serviceResponse = new ServiceResponse();
        try {
            clientProfile = new ClientProfile(user, serviceResponse);
            return clientProfile.updateClientProfile();
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot update client profile " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @ClientSecured
    @PUT
    @Path("/changePassword")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse changePassword(ChangePassword changePassword) {
        serviceResponse = new ServiceResponse();
        try {
            Users user = TableClient.getUser(changePassword.getClientID());
            clientProfile = new ClientProfile(user, serviceResponse);
            return clientProfile.changePassword(changePassword.getNewPassword()
                    , changePassword.getOldPassword());
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot update change password " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
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

    @OPTIONS
    @Path("/clientProfile")
    public void doOptionsForClientProfile() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/changePassword")
    public void doOptionsForChangePass() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/getClientReserveTimes")
    public void doOptionsForClientReserveTime() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}