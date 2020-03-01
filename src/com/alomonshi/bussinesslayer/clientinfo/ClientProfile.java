package com.alomonshi.bussinesslayer.clientinfo;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.utility.CopyNotNullProperties;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientProfile {
    private Users user;
    private ServiceResponse serviceResponse;

    public ClientProfile(Users user
            , ServiceResponse serviceResponse) {
        this.user = user;
        this.serviceResponse = serviceResponse;
    }

    /**
     * Get user info
     * @param clientID to be got his/her info
     * @return user
     */
    public static Users getClientInfo (int clientID) {
        return TableClient.getUser(clientID);
    }

    /**
     * Update client inforamtion
     * @return service response
     */
    public ServiceResponse updateClientProfile() {
        this.deleteUnnecessaryProperties();
        this.user = getCopiedCommentProperties();
        if (TableClient.update(user))
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.FAULTMESSAGE);
    }


    /**
     * Change user password
     * @param newPassword to be set
     * @param oldPassword to be changed
     * @return service response object
     */

    public ServiceResponse changePassword(String newPassword, String oldPassword) {
        try {
            if (this.user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                if (TableClient.update(user))
                    return serviceResponse.setResponse(true)
                            .setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    return serviceResponse.setResponse(false)
                            .setMessage(ServerMessage.FAULTMESSAGE);
            }else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.INPUTCHECK);
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot change password " + e);
            serviceResponse.setResponse(false);
            serviceResponse.setMessage(ServerMessage.INTERNALERRORMESSAGE);
            return serviceResponse;
        }
    }

    /**
     * Copy properties of object got from ui to existed object
     * @return updated object
     */
    private Users getCopiedCommentProperties() {
        BeanUtilsBean utilsBean = new CopyNotNullProperties();
        Users toBeUpdated = TableClient.getUser(user.getClientID());
        //Check client id and reserve time id is the same from table and UI
        //Copying not null values into old comment object got from database to be updated
        try {
            utilsBean.copyProperties(toBeUpdated, user);
        }catch (IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not copy properties " + e);
        }
        return toBeUpdated;
    }

    /**
     * delete unnecessary data from object got from Ui
     */
    private void deleteUnnecessaryProperties() {
        user.setExpirationDate(null);
        user.setUserLevel(null);
        user.setToken(null);
    }
}
