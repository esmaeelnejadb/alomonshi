package com.alomonshi.bussinesslayer.authentication;

import com.alomonshi.datalayer.dataaccess.TableClient;

import com.alomonshi.object.tableobjects.Users;

import java.time.LocalDateTime;

public class AdminAuthorization implements Authorization{
    private String token;
    private WebTokenHandler.WebTokenPayLoad webTokenPayLoad;

    /**
     * Constructor Authentication
     *
     * @param token injected to object
     */
    public AdminAuthorization(String token) {
        this.token = token;
        WebTokenHandler webTokenHandler = new WebTokenHandler().setToken(token);
        webTokenPayLoad = webTokenHandler.getWebTokenPayload();
    }

    @Override
    public boolean isAuthorized() {
        Users user = getUser();
        ClientPrimaryCheck clientPrimaryCheck = new ClientPrimaryCheck(user);
        return clientPrimaryCheck.isClientRegistered() && clientPrimaryCheck.isTokenValid();
    }

    private Users getUser(){
        return TableClient.getUser(webTokenPayLoad.getUserID());
    }

    private boolean checkAdminSecondaryCheck(Users user, WebTokenHandler.WebTokenPayLoad webTokenPayLoad){
        return user.getToken().equals(webTokenPayLoad.getToken());
    }

}
