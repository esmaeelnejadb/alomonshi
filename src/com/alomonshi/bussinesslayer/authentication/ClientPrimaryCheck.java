package com.alomonshi.bussinesslayer.authentication;

import com.alomonshi.object.tableobjects.Users;

import java.time.LocalDateTime;

public class ClientPrimaryCheck {

    Users user;

    ClientPrimaryCheck(Users user) {
        this.user = user;
    }

    /**
     * check if user is registered
     * @return true if user is registered
     */
    public boolean isClientRegistered(){
        return user.getID() != 0 && user.isActive();
    }

    /**
     * Check if user token is valid or not
     * @return true if user token is valid
     */
    boolean isTokenValid(){
        return user.getExpirationDate() != null && user.getExpirationDate().isAfter(LocalDateTime.now());
    }

}
