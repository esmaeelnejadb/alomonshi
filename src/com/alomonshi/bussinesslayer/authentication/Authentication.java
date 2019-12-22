package com.alomonshi.bussinesslayer.authentication;

import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.entity.Users;
import org.json.JSONObject;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;

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
            return user.getExpirationDate() != null && user.getExpirationDate().after(Calendar.getInstance().getTime());
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

    public String generateWebToken(){
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
     * Generating json web token
     * @return json web token
     */
    public String handleUserLogin(){
        if (!isTokenValid()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1); // Set expiration time 1 year after now
            user.setToken(generateNewToken()).setExpirationDate(calendar.getTime());
            if(!TableClient.update(user))
                return null;
        }
        return generateWebToken();
    }
}
