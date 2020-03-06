package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck.CheckClientAuthority;
import com.alomonshi.bussinesslayer.comment.ClientCommentService;
import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/clientComment")
public class CommentWebService {

    private ClientCommentService clientCommentService;
    private ServiceResponse serviceResponse;
    private CheckClientAuthority checkClientAuthority;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * Inserting new comment
     * @param comment to be inserted
     * @return service response
     */
    @JsonView(JsonViews.ClientViews.class)
    @ClientSecured
    @POST
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse insertNewComment(Comments comment) {
        serviceResponse = new ServiceResponse();
        try {
            checkClientAuthority = new CheckClientAuthority(comment.getClientID(), comment.getReserveTimeID());
            if (checkClientAuthority.isAuthorizedToChangeComment()) {
                clientCommentService = new ClientCommentService(comment, serviceResponse);
                return clientCommentService.insertNewComment();
            }else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.ACCESSFAULT);

        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot post a comment " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Updating a comment
     * @param comment to be updated
     * @return service response
     */
    @JsonView(JsonViews.ClientViews.class)
    @ClientSecured
    @PUT
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse updateClientComment(Comments comment){
        serviceResponse = new ServiceResponse();
        try {
            checkClientAuthority = new CheckClientAuthority(comment.getClientID(), comment.getReserveTimeID());
            if (checkClientAuthority.isAuthorizedToChangeComment()) {
                clientCommentService = new ClientCommentService(comment, serviceResponse);
                return clientCommentService.updateComment();
            }else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot update a comment " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Deleting a comment
     * @param comment to be deleted
     * @return service response
     */
    @JsonView(JsonViews.ClientViews.class)
    @ClientSecured
    @DELETE
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse deleteComment(Comments comment) {
        serviceResponse = new ServiceResponse();
        try {
            checkClientAuthority = new CheckClientAuthority(comment.getClientID(), comment.getReserveTimeID());
            if (checkClientAuthority.isAuthorizedToChangeComment()) {
                //Getting to be deleted comment from database to set only is_active field to false
                Comments toBeDeleted = TableComment.getComment(comment.getID());
                clientCommentService = new ClientCommentService(toBeDeleted, serviceResponse);
                return clientCommentService.deleteComment();
            }else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot delete a comment " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/comment")
    public void doOptionsForClientComment() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}