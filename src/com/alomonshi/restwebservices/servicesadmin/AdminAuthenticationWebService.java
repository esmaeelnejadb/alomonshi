package com.alomonshi.restwebservices.servicesadmin;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication.LoginAuthentication;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/adminLogin")
public class AdminAuthenticationWebService {

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * check client authority for login
     * @return ok if user is valid and server error if not
     */
    @CompanySubAdminSecured
    @POST
    @Path("/checkAuthority")
    public Response confirmLoginRequest(){
        return Response.ok().build();
    }

    /**
     * check admin request for login
     * @param phoneNumber entered phone by admin
     * @param password entered password by admin
     * @return ok if admin is valid and server error if not
     */
    @POST
    @Path("/adminPasswordLogin")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response confirmLoginRequest(@FormParam("phoneNumber") String phoneNumber
            , @FormParam("password") String password){
        Users user = TableClient.getUser(phoneNumber);
        LoginAuthentication authentication = new LoginAuthentication(user);
        String token = authentication.handleAdminLogin(password);
        return token != null ? Response.ok(token).build()
                : Response.status(Response.Status.FORBIDDEN).build();
    }

    @OPTIONS
    @Path("/checkAuthority")
    public void doOptionsForCheckAuthority() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}