package com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck;

import com.alomonshi.object.tableobjects.Users;

import java.time.LocalDateTime;

public class ClientInformationCheck {

    private Users user;

    public ClientInformationCheck(Users user) {
        this.user = user;
    }

    public boolean isPrimaryCheckPass(){
        return isClientRegistered() && isTokenValid();
    }

    /**
     * check if user is registered
     * @return true if user is registered
     */
    public boolean isClientRegistered(){
        return user.getId() != 0 && user.isActive();
    }

    /**
     * Check if user token is valid or not
     * @return true if user token is valid
     */
    public boolean isTokenValid(){
        return user.getExpirationDate() != null && user.getExpirationDate().isAfter(LocalDateTime.now());
    }

}
