package com.alomonshi.restwebservices.services;

import com.alomonshi.bussinesslayer.authentication.Authentication;
import com.alomonshi.bussinesslayer.authentication.HandleRegistration;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.entity.Users;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginWebService {

   private Authentication authentication;

    @POST
    @Path("/clientPasswordLogin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmLoginRequest(@FormParam("phoneNumber") String phoneNumber, @FormParam("password") String password){
        Users user = TableClient.getUser(phoneNumber, password);
        authentication = new Authentication(user);
        return authentication.handleUserLogin() != null ? Response.ok(authentication.handleUserLogin()).build()
                : Response.serverError().build();
    }

    @POST
    @Path("/clientRegisterRequest")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response registerRequest(@FormParam("phoneNumber") String phoneNumber, @FormParam("password") String password){
        Users user = TableClient.getUser(phoneNumber, password);
        authentication = new Authentication(user);
        if (authentication.isClientRegistered())
            return Response.serverError().build();
        else{
            user.setPassword(password).setPhoneNo(phoneNumber);
            HandleRegistration handleRegistration = new HandleRegistration(user);
            return handleRegistration.handleRegistration() ? Response.ok().build() : Response.serverError().build();
        }
    }
}