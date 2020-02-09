package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.ClientInformationCheck;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication.LoginAuthentication;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication.HandleRegistration;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.enums.UserLevels;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/userLogin")
public class LoginWebService {

   private LoginAuthentication authentication;
   private HandleRegistration handleRegistration;
   private ClientInformationCheck clientPrimaryCheck;

    /**
     * check client request for login
     * @param phoneNumber entered phone by user
     * @param password entered password by user
     * @return ok if user is valid and server error if not
     */
    @POST
    @Path("/clientPasswordLogin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmLoginRequest(@FormParam("phoneNumber") String phoneNumber, @FormParam("password") String password){
        Users user = TableClient.getUser(phoneNumber);
        authentication = new LoginAuthentication(user);
        String token = authentication.handleUserLogin(password);
        return token != null ? Response.ok(token).build()
                : Response.status(Response.Status.FORBIDDEN).build();
    }

    /**
     * handle request for registration
     * @param phoneNumber entered phone number
     * @param password entered password
     * @return ok if verification code send to client phone
     */
    @POST
    @Path("/clientRegisterRequest")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response registerRequest(@FormParam("phoneNumber") String phoneNumber, @FormParam("password") String password){
        Users user = TableClient.getUser(phoneNumber);
        clientPrimaryCheck = new ClientInformationCheck(user);
        if (clientPrimaryCheck.isClientRegistered())
            return Response.status(Response.Status.FORBIDDEN).build();
        else{
            user.setPassword(password).setPhoneNo(phoneNumber);
            handleRegistration = new HandleRegistration(user);
            return handleRegistration.handleVerification() ? Response.ok().build() : Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     * check and verify user phone
     * @param verificationCode code entered by client
     * @param phoneNumber client phone number
     * @return ok if code is valid code had been sent to client
      */
    @POST
    @Path("/clientRegisterVerification")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response registerVerification(@FormParam("verificationCode") String verificationCode, @FormParam("phoneNumber") String phoneNumber){
        Users user = TableClient.getUser(phoneNumber);
        handleRegistration = new HandleRegistration(user);
        if(handleRegistration.checkVerificationCode(verificationCode)) {
            user.setActive(true).setUserLevel(UserLevels.CLIENT);
            String token = handleRegistration.handleFinalRegistration();
            return token != null ? Response.ok(token).build() :
                    Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }else
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    /**
     * Temporary function for deleting user
     * @param phoneNumber phone number
     * @return Response
     */
    @POST
    @Path("/deleteUser")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteUser(@FormParam("phoneNumber") String phoneNumber){
        return TableClient.deleteUserTemporary(phoneNumber) ? Response.ok().build() : Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}