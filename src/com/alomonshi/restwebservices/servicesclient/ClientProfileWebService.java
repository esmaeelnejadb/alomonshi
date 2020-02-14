package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.clientinfo.ClientProfile;
import com.alomonshi.bussinesslayer.reservetimes.ReserveTimeService;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.uiobjects.ClientReservedTimes;
import com.alomonshi.restwebservices.annotation.ClientSecured;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
public class ClientProfileWebService {

    private ServiceResponse serviceResponse;
    private ClientProfile clientProfile;

    /**
     * Get client profile info
     * @param clientID to be got information from
     * @return client information
     */
    @ClientSecured
    @POST
    @Path("/getClientProfile")
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
    @Path("/updateProfile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse updateClientProfile(Users user) {
        serviceResponse = new ServiceResponse();
        clientProfile = new ClientProfile(user, serviceResponse);
        return clientProfile.updateClientProfile();
    }

    @ClientSecured
    @PUT
    @Path("/changePassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public ServiceResponse changePassword(@FormParam("clientID") int clientID
            , @FormParam("newPassword") String newPassword
            , @FormParam("oldPassword") String oldPassword) {
        serviceResponse = new ServiceResponse();
        Users user = TableClient.getUser(clientID);
        ClientProfile clientProfile = new ClientProfile(user, serviceResponse);
        return clientProfile.changePassword(newPassword, oldPassword);
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