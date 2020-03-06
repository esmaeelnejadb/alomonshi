package com.alomonshi.bussinesslayer.comment;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableComment;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.util.Collections;
import java.util.List;

public class AdminCommentService {
     private ServiceResponse serviceResponse;
     private Comments comment;

    /**
     * Constructor for two class object injection
     * @param serviceResponse object to be inject
     * @param comment object to be inject
     */
    public AdminCommentService(Comments comment, ServiceResponse serviceResponse) {
        this.comment = comment;
        this.serviceResponse = serviceResponse;
    }

    /**
     * Constructor for one class object inject
     * @param serviceResponse object to be inject
     */
    public AdminCommentService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Getting comments in a unit
     * @param unitID to be got its comments
     * @return service response
     */
    public ServiceResponse getUnitAdminComments(int unitID) {
        List<Comments> comments = TableComment.getUnitComments(unitID);
        if (!comments.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(Collections.singletonList(comments));
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.COMMENTERROR_01);

    }

    /**
     * Inserting admin comments (update related client comment row in database)
     * @return service response
     */
    public ServiceResponse insertAdminComment() {
        prepareAdminCommentForInsert();
        if (TableComment.updateComment(comment))
            return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Prepare admin comment to be inserted(Update related client comment)
     */
    private void prepareAdminCommentForInsert() {
        CommentPropertiesPreparation.deleteAdminCommentUnnecessaryProperties(comment);
        comment = CommentPropertiesPreparation.getCopiedAdminCommentProperties(comment);
    }

}
