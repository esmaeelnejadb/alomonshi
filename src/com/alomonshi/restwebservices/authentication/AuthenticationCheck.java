package com.alomonshi.restwebservices.authentication;

import com.alomonshi.object.entity.Users;

import java.util.Calendar;

/**
 * this class perform all actions related to authentication process including
 * - check user registration
 *  - check user token is expired or not
 *  - generate new token for user
 */

public class AuthenticationCheck {

    /**
     * check if user is registered
     * @param user to be checked for registration
     * @return true if user is registered
     */

    public static boolean isClientAuthenticated(Users user){
        return user.getUserID() != 0;
    }
    public static boolean isTokenValid(Users user){
        return user.getExpirationDate().after(Calendar.getInstance().getTime());
    }
}
