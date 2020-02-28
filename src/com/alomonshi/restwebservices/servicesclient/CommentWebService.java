package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.comment.CommentService;
import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/clientComment")
public class CommentWebService {

    private CommentService commentService;
    private ServiceResponse serviceResponse;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * Inserting new comment
     * @param comment to be inserted
     * @return service response
     */
    @ClientSecured
    @POST
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse insertNewComment(Comments comment) {
        serviceResponse = new ServiceResponse();
        try {
            commentService = new CommentService(comment, serviceResponse);
            return commentService.insertNewComment();
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
    @ClientSecured
    @PUT
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse updateClientComment(Comments comment){
        serviceResponse = new ServiceResponse();
        try {
            commentService = new CommentService(comment, serviceResponse);
            return commentService.updateComment();
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
    @ClientSecured
    @DELETE
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse deleteComment(Comments comment) {
        serviceResponse = new ServiceResponse();
        try {
            //Getting to be deleted comment from database to set only is_active field to false
            Comments toBeDeleted = TableComment.getComment(comment.getID());
            commentService = new CommentService(toBeDeleted, serviceResponse);
            return commentService.deleteComment();
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