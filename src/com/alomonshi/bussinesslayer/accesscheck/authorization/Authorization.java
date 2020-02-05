package com.alomonshi.bussinesslayer.accesscheck.authorization;

import com.alomonshi.bussinesslayer.accesscheck.ClientInformationCheck;
import com.alomonshi.bussinesslayer.accesscheck.WebTokenHandler;
import com.alomonshi.datalayer.dataaccess.TableClient;

import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Users;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Authorization {
    private WebTokenHandler.WebTokenPayLoad webTokenPayLoad;
    private Users user;
    private ClientInformationCheck clientPrimaryCheck;
    private UserLevels userLevel;

    /**
     * Constructor Authentication
     *
     * @param token injected to object
     */
    public Authorization(String token, UserLevels userLevel) {
        WebTokenHandler webTokenHandler = new WebTokenHandler().setToken(token);
        webTokenPayLoad = webTokenHandler.getWebTokenPayload();
        this.user = getUser();
        this.userLevel = userLevel;
        clientPrimaryCheck = new ClientInformationCheck(user);
    }

    /**
     * check client registration, client be activate and client token
     * @return true if all checks have been passed
     */
    private boolean isPrimaryAuthorized() {
        return clientPrimaryCheck.isPrimaryCheckPass() && isTokenVerified(user, webTokenPayLoad );
    }

    /**
     * getting user from web token user id
     * @return user got from web token
     */

    private Users getUser(){
        try {
            return TableClient.getUser(webTokenPayLoad.getUserID());
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Cannot get user from token " + e.getMessage());
            return null;
        }
    }

    /**
     * Checking client token web token
     * @param user user to be checked
     * @param webTokenPayLoad to be checked by client token
     * @return true if is passed
     */
    private boolean isTokenVerified(Users user, WebTokenHandler.WebTokenPayLoad webTokenPayLoad){
        return user.getToken().equals(webTokenPayLoad.getToken());
    }

    /**
     * Getting user level from user id
     * @return user level
     */
    private UserLevels getUserLevel(){
        return user.getUserLevel();
    }

    /**
     * Checking user level for related access
     * @return true if is passed
     */
    public boolean isNotAuthorized(){
        return !isPrimaryAuthorized() || getUserLevel().getValue() < userLevel.getValue();
    }

}