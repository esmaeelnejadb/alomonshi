package com.alomonshi.restwebservices.servicesadmin;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication.LoginAuthentication;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/adminLogin")
public class AdminAuthenticationWebService {

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
}