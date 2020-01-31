package com.alomonshi.bussinesslayer.authentication;

import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Users;
import org.json.JSONObject;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * this class perform all actions related to authentication process including
 * - check user registration
 *  - check user token is expired or not
 *  - generate new token for user
 *  - check admin authentication
 */

public class Authentication {

    private Users user;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    /**
     * Constructor Authentication
     * @param user injected to object
     */
    public Authentication(Users user){
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
    private boolean isTokenValid(){
            return user.getExpirationDate() != null && user.getExpirationDate().isAfter(LocalDateTime.now());
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
     * new token code generation
     * @return generated new token code
     */

    static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    /**
     * Generating json web token
     * @return json web token as base url64 encoded string
     */

    String generateWebToken(){
        JSONObject header = new JSONObject();
        JSONObject payLoad = new JSONObject();
        header.put("typ", "JWT");
        payLoad.put("uID", user.getID());
        payLoad.put("token", user.getToken());
        payLoad.put("exp", user.getExpirationDate());
        payLoad.put("phoneNumber", user.getPhoneNo());
        payLoad.put("name", user.getName());
        payLoad.put("email", user.getEmail());
        return base64Encoder.encodeToString(header.toString().getBytes()) + "."
                + base64Encoder.encodeToString(payLoad.toString().getBytes());
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
                user.setToken(generateNewToken()).setExpirationDate(generateExpirationDate());
                if(!TableClient.update(user))
                    return null;
            }
            return generateWebToken();
        }else
            return null;
    }

    /**
     * Getting level of user
     * @return level of user
     */
    public UserLevels getUserLevels(){
        return user.getUserLevel();
    }
}
