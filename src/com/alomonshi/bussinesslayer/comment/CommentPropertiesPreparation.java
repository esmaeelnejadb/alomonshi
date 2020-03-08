package com.alomonshi.bussinesslayer.comment;
import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.utility.CopyNotNullProperties;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

class CommentPropertiesPreparation {

    /**
     * Copy new updated fields into old object got from table
     * @return new updated comment
     */
    static Comments getCopiedClientCommentProperties(Comments comment) {
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
     * Copy new updated fields into old object got from table
     * There is one difference related to copy client object and is that client id got from database
     * and got from ui does not check to be equal in this method
     * @return new updated comment
     */
    static Comments getCopiedAdminCommentProperties(Comments comment) {
        BeanUtilsBean utilsBean = new CopyNotNullProperties();
        Comments newComment = TableComment.getComment(comment.getID());
        //Check client id and reserve time id is the same from table and UI
        if (newComment.getReserveTimeID() != comment.getReserveTimeID())
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
    static void deleteClientCommentUnnecessaryProperties(Comments comment) {
        comment.setReplyDate(null);
        comment.setReplyComment(null);
    }

    /**
     * Deleting unwanted properties got from ui to not change this fields in database
     */
    static void deleteAdminCommentUnnecessaryProperties(Comments comment) {
        comment.setReplyDate(LocalDateTime.now());
        comment.setCommentDate(null);
        comment.setComment(null);
        comment.setClientName(null);
        comment.setServiceRate(0);
        comment.setClientID(0);
    }
}