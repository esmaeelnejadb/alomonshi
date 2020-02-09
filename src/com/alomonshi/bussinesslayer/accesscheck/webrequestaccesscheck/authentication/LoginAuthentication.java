package com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authentication;

import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.ClientInformationCheck;
import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.WebTokenHandler;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.tableobjects.Users;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class perform all actions related to authentication process including
 * - check user registration
 *  - check user token is expired or not
 *  - generate new token for user
 *  - check admin authentication
 */

public class LoginAuthentication {
    private Users user;
    private WebTokenHandler webTokenHandler;
    private ClientInformationCheck clientPrimaryCheck;

    /**
     * Constructor Authentication
     * @param user injected to object
     */
    public LoginAuthentication(Users user){
        this.user = user;
        webTokenHandler = new WebTokenHandler().setUser(user);
        clientPrimaryCheck = new ClientInformationCheck(user);
    }

    /**
     * Verifying password
     * @param password password entered by user
     * @return true if entered password is valid
     */

    private boolean isPasswordValid(String password){
        try{
            return user.getPassword().equals(password);
        }catch (NullPointerException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
            return false;
        }
    }

    /**
     * Generating new expiration date
     * @return new expiration date
     */

    public static LocalDateTime generateExpirationDate(){
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.plusYears(1);
    }

    /**
     * Checking token is valid or not then generating json web token
     * @return json web token
     */
    public String handleUserLogin(String password){
        if (clientPrimaryCheck.isClientRegistered() && isPasswordValid(password)) {
            if (!clientPrimaryCheck.isTokenValid()) {
                user.setToken(WebTokenHandler.generateNewToken()).setExpirationDate(generateExpirationDate());
                if(!TableClient.update(user))
                    return null;
            }
            return webTokenHandler.generateWebToken();
        }else
            return null;
    }
}
