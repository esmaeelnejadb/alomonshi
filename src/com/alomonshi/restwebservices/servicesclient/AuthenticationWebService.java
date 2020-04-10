package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.ClientInformationCheck;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication.LoginAuthentication;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication.HandleRegistration;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.SMSMessage;
import com.alomonshi.utility.sendsms.SMSUtils;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/userLogin")
public class AuthenticationWebService {

   private LoginAuthentication authentication;
   private HandleRegistration handleRegistration;
   private ClientInformationCheck clientPrimaryCheck;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * check client authority for login
     * @return ok if user is valid and server error if not
     */

    @ClientSecured
    @POST
    @Path("/checkAuthority")
    public Response confirmLoginRequest(){
        return Response.ok().build();
    }

    @POST
    @Path("/clientPasswordLogin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmLoginRequest(@FormParam("phoneNumber") String phoneNumber
            , @FormParam("password") String password){
        try {
            Users user = TableClient.getUser(phoneNumber);
            authentication = new LoginAuthentication(user);
            String token = authentication.handleUserLogin(password);
            return token != null ? Response.ok(token).build()
                    : Response.status(Response.Status.FORBIDDEN).build();
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
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
    public Response registerRequest(@FormParam("phoneNumber") String phoneNumber,
                                    @FormParam("password") String password){
        try {
            Users user = TableClient.getUser(phoneNumber);
            clientPrimaryCheck = new ClientInformationCheck(user);
            if (clientPrimaryCheck.isClientRegistered())
                return Response.status(Response.Status.FORBIDDEN).build();
            else{
                user.setPassword(password);
                user.setPhoneNo(phoneNumber);
                handleRegistration = new HandleRegistration(user);
                return handleRegistration.handleVerification() ?
                        Response.ok().build() : Response.status(Response.Status.FORBIDDEN).build();
            }
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
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
    public Response registerVerification(@FormParam("verificationCode") String verificationCode,
                                         @FormParam("phoneNumber") String phoneNumber){
        try {
            Users user = TableClient.getUser(phoneNumber);
            handleRegistration = new HandleRegistration(user);
            if(handleRegistration.checkVerificationCode(verificationCode)) {
                user.setActive(true);
                user.setUserLevel(UserLevels.CLIENT);
                String token = handleRegistration.handleFinalRegistration();
                return token != null ? Response.ok(token).build() :
                        Response.status(Response.Status.NOT_ACCEPTABLE).build();
            }else
                return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    /**
     * check and verify user phone, send forgotten password to user
     * @param phoneNumber client phone number
     * @return ok if valid code had been sent to client
     */
    @POST
    @Path("/forgottenPassword")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response forgotPassword(@FormParam("phoneNumber") String phoneNumber){
        try {
            Users user = TableClient.getUser(phoneNumber);
            clientPrimaryCheck = new ClientInformationCheck(user);
            if (clientPrimaryCheck.isClientRegistered()) {
                return SMSUtils.sendSMS(SMSUtils.getSingleToNumber(phoneNumber),
                        SMSMessage.getPasswordRetriveMessage(user)) ?
                        Response.ok().build() : Response.status(Response.Status.EXPECTATION_FAILED).build();
            }else
                return Response.status(Response.Status.FORBIDDEN).build();
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @OPTIONS
    @Path("/checkAuthority")
    public void doOptionsForCheckAuthority() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}