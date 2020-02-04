package com.alomonshi.bussinesslayer.authentication;

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

public class LoginAuthentication extends ClientPrimaryCheck{

    private WebTokenHandler webTokenHandler;

    /**
     * Constructor Authentication
     * @param user injected to object
     */
    public LoginAuthentication(Users user){
        super(user);
        webTokenHandler = new WebTokenHandler().setUser(user);
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

    static LocalDateTime generateExpirationDate(){
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.plusYears(1);
    }

    /**
     * Checking token is valid or not then generating json web token
     * @return json web token
     */
    public String handleUserLogin(String password){
        if (isClientRegistered() && isPasswordValid(password)) {
            if (!isTokenValid()) {
                user.setToken(WebTokenHandler.generateNewToken()).setExpirationDate(generateExpirationDate());
                if(!TableClient.update(user))
                    return null;
            }
            return webTokenHandler.generateWebToken();
        }else
            return null;
    }
}
