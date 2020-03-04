package com.alomonshi.bussinesslayer.comment;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck.CheckClientAuthority;
import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.utility.CopyNotNullProperties;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommentService {

    private ServiceResponse serviceResponse;
    private Comments comment;

    public CommentService(Comments comment, ServiceResponse serviceResponse){
        this.serviceResponse = serviceResponse;
        this.comment = comment;
    }

    /**
     * Inserting new comment
     *
     * @return service response
     */
    public ServiceResponse insertNewComment() {
        this.deleteUnnecessaryProperties();
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
        this.deleteUnnecessaryProperties();
        //Copying new properties into old properties saved in database
        comment = this.getCopiedCommentProperties();
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
        this.deleteUnnecessaryProperties();
        if (TableComment.delete(comment))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Copy new updated fields into old object got from table
     * @return new updated comment
     */

    private Comments getCopiedCommentProperties() {
        BeanUtilsBean utilsBean = new CopyNotNullProperties();
        Comments newComment = TableComment.getComment(comment.getID());
        //Check client id and reserve time id is the same from table and UI
        if (newComment.getReserveTimeID() != comment.getReserveTimeID()
                || newComment.getClientID() != comment.getClientID() )
            return null;
        //Copying not null values into old comment object got from database to be updated
        try {
            utilsBean.copyProperties(newComment, comment);
        }catch (IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not copy properties " + e);
        }
        return newComment;
    }

    /**
     * deleting admin field to prevent from update
     */
    private void deleteUnnecessaryProperties() {
        comment.setReplyDate(null);
        comment.setReplyComment(null);
    }
}
