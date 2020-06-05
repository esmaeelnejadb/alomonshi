package com.alomonshi.restwebservices.servicemanager;

import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication.LoginAuthentication;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.restwebservices.annotation.SiteManagerSecured;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/managerLogin")
public class ManagerAuthenticationWebService {

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * check admin authority for login
     * @return ok if user is valid and server error if not
     */
    @SiteManagerSecured
    @POST
    @Path("/checkAuthority")
    public Response checkIsLogin(){
        return Response.ok().build();
    }

    /**
     * check manager request for login
     * @param phoneNumber entered phone by admin
     * @param password entered password by admin
     * @return ok if admin is valid and server error if not
     */
    @POST
    @Path("/managerPasswordLogin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmLoginRequest(@FormParam("phoneNumber") String phoneNumber
            , @FormParam("password") String password){
        Users user = TableClient.getUser(phoneNumber);
        LoginAuthentication authentication = new LoginAuthentication(user);
        String token = authentication.handleAdminLogin(password);
        return token != null ? Response.ok(token).build()
                : Response.status(Response.Status.FORBIDDEN).build();
    }
}
