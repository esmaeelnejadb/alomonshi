package com.alomonshi.restwebservices.services;

import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.entity.Users;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;

@Path("/login")
public class LoginWebService {

    @POST
    @Path("/clientPasswordLogin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmLoginRequest(@FormParam("phoneNumber") String phoneNumber, @FormParam("password") String password){
        return null;
    }

    public static void generateNewToken(Users user){
    }

}