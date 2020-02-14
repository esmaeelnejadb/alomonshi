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
    private CheckClientAuthority checkClientAuthority;
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
        checkClientAuthority = new CheckClientAuthority(comment.getClientID(), comment.getReserveTimeID());
        if (checkClientAuthority.isAuthorizedToChangeComment()) {
            if (TableComment.insertComment(comment))
                return serviceResponse.setResponse(true)
                        .setMessage(ServerMessage.SUCCESSMESSAGE);
            else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.ACCESSFAULT);
    }

    /**
     * Updating comment object
     *
     * @return service response
     */

    public ServiceResponse updateComment() {
        this.deleteUnnecessaryProperties();
        checkClientAuthority = new CheckClientAuthority(comment.getClientID(), comment.getReserveTimeID());
        if (checkClientAuthority.isAuthorizedToChangeComment()) {
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

        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.ACCESSFAULT);

    }

    /**
     * Deleting a comment
     *
     * @return service response
     */

    public ServiceResponse deleteComment() {
        this.deleteUnnecessaryProperties();
        checkClientAuthority = new CheckClientAuthority(comment.getClientID(), comment.getReserveTimeID());
        if (checkClientAuthority.isAuthorizedToChangeComment()) {
            //Copying new properties into old properties saved in database
            if (TableComment.delete(comment))
                return serviceResponse.setResponse(true)
                        .setMessage(ServerMessage.SUCCESSMESSAGE);
            else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.ACCESSFAULT);
    }

    /**
     * Copy new updated fields into old object got from table
     *
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
