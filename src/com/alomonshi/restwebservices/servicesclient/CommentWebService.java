package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.comment.CommentService;
import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.restwebservices.annotation.ClientSecured;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/clientComment")
public class CommentWebService {

    private CommentService commentService;
    private ServiceResponse serviceResponse;

    /**
     * Inserting new comment
     * @param comment to be inserted
     * @return service response
     */
    @ClientSecured
    @POST
    @Path("/insertComment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse insertNewComment(Comments comment) {
        serviceResponse = new ServiceResponse();
        commentService = new CommentService(comment, serviceResponse);
        return commentService.insertNewComment();
    }

    /**
     * Updating a comment
     * @param comment to be updated
     * @return service response
     */
    @ClientSecured
    @PUT
    @Path("/updateComment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse updateClientComment(Comments comment){
        serviceResponse = new ServiceResponse();
        commentService = new CommentService(comment, serviceResponse);
        return commentService.updateComment();
    }

    /**
     * Deleting a comment
     * @param comment to be deleted
     * @return service response
     */
    @ClientSecured
    @DELETE
    @Path("/deleteComment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse deleteComment(Comments comment) {
        serviceResponse = new ServiceResponse();
        //Getting to be deleted comment from database to set only is_active field to false
        Comments toBeDeleted = TableComment.getComment(comment.getID());
        commentService = new CommentService(toBeDeleted, serviceResponse);
        return commentService.deleteComment();
    }
}