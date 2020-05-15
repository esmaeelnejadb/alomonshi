package com.alomonshi.bussinesslayer.comment;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.object.uiobjects.ClientComment;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ClientCommentService {

    private ServiceResponse serviceResponse;
    private Comments comment;

    public ClientCommentService(Comments comment, ServiceResponse serviceResponse){
        this.serviceResponse = serviceResponse;
        this.comment = comment;
    }


    /**
     * Getting client comments
     * @return service response
     */
    public ServiceResponse getClientComments () {
        List<ClientComment> clientCommentList = TableComment.getClientComments(comment.getClientID());
        return serviceResponse
                .setResponse(true)
                .setResponseData(clientCommentList);

    }

    /**
     * Inserting new comment
     *
     * @return service response
     */
    public ServiceResponse insertNewComment() {
        prepareCommentForInsert();
        if (TableComment.insertComment(comment))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Updating comment object
     *
     * @return service response
     */

    public ServiceResponse updateComment() {
        CommentPropertiesPreparation.deleteClientCommentUnnecessaryProperties(comment);
        //Copying new properties into old properties saved in database
        comment = CommentPropertiesPreparation.getCopiedClientCommentProperties(comment);
        if (comment != null) {
            if (TableComment.updateComment(Objects.requireNonNull(comment)))
                return serviceResponse.setResponse(true)
                        .setMessage(ServerMessage.SUCCESSMESSAGE);
            else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Deleting a comment
     * @return service response
     */

    public ServiceResponse deleteComment() {
        CommentPropertiesPreparation.deleteClientCommentUnnecessaryProperties(comment);
        if (TableComment.delete(comment))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Preparing comment for insert to database
     * Setting comment time and eleting unnecessary properties got from ui
     */

    private void prepareCommentForInsert() {
        comment.setCommentDate(LocalDateTime.now());
        CommentPropertiesPreparation.deleteClientCommentUnnecessaryProperties(comment);
    }
}
