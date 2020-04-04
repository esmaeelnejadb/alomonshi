package com.alomonshi.restwebservices.servicesadmin;


import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.dbchangeaccesscheck.CheckAdminAuthority;
import com.alomonshi.bussinesslayer.comment.AdminCommentService;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.object.uiobjects.AdminEditObject;
import com.alomonshi.restwebservices.annotation.CompanyAdminSecured;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/adminComment")
public class AdminCommentWebService {

    private ServiceResponse serviceResponse;
    private AdminCommentService adminCommentService;
    
    @Context
    HttpServletResponse httpServletResponse;


    /**
     * Getting unit comments
     * @param adminEditObject data got from UI
     * @return service response
     */
    @JsonView(JsonViews.SubAdminViews.class)
    @CompanyAdminSecured
    @POST
    @Path("/getAdminUnitComments")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getAdminUnitComments (AdminEditObject adminEditObject) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserUnitAuthorized(
                    adminEditObject.getClientID(), adminEditObject.getUnitID())) {
                adminCommentService = new AdminCommentService(serviceResponse);
                return adminCommentService.getUnitAdminComments(adminEditObject.getUnitID());
            } else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        } catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @JsonView(JsonViews.SubAdminViews.class)
    @CompanySubAdminSecured
    @POST
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse insertAdminComments (Comments comment) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.canAdminEditComment(comment)) {
                adminCommentService = new AdminCommentService(comment, serviceResponse);
                return adminCommentService.insertAdminComment();
            } else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        } catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/getAdminUnitComments")
    public void doOptionsForGetAdminComments() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/comment")
    public void doOptionsForComment() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}