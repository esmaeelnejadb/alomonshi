package com.alomonshi.bussinesslayer.authentication;

import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.entity.Users;
import org.json.JSONObject;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * this class perform all actions related to authentication process including
 * - check user registration
 *  - check user token is expired or not
 *  - generate new token for user
 */

public class Authentication {

    private Users user;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public Authentication(Users user){
        this.user = user;
    }

    /**
     * check if user is registered
     * @return true if user is registered
     */
    public boolean isClientRegistered(){
        return user.getUserID() != 0 && user.isActive();
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
        return user.getPassword().equals(password);
    }

    /**
     * new token code generation
     * @return generated new token code
     */

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    /**
     * Generating json web token
     * @return json web token as base url64 encoded string
     */

    private String generateWebToken(){
        JSONObject header = new JSONObject();
        JSONObject payLoad = new JSONObject();
        header.put("typ", "JWT");
        payLoad.put("uID", user.getUserID());
        payLoad.put("token", user.getToken());
        payLoad.put("exp", user.getExpirationDate());
        return base64Encoder.encodeToString(header.toString().getBytes()) + "."
                + base64Encoder.encodeToString(payLoad.toString().getBytes());
    }


    /**
     * Generating new expiration date
     * @return new expiration date
     */

    private LocalDateTime generateExpirationDate(){
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
}
