package com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck;

import com.alomonshi.object.tableobjects.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class WebTokenHandler {
    private Users user;
    private String token;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    private static final Base64.Decoder base64Decoder = Base64.getUrlDecoder(); //threadsafe

    /**
     * Constructor
     */
    public WebTokenHandler() {}

    public WebTokenHandler setUser(Users user){
        this.user = user;
        return this;
    }

    public WebTokenHandler setToken(String token){
        this.token = token;
        return this;
    }

    /**
     * Generating web token from user information
     * @return generated web token
     */
    public String generateWebToken() {
        try {
            String header = objectMapper.writeValueAsString(new WebTokenHeader());
            String payLoad = objectMapper.writeValueAsString(new WebTokenPayLoad(user));
            return base64Encoder.encodeToString(header.getBytes()) + "."
                    + base64Encoder.encodeToString(payLoad.getBytes());
        }catch (IOException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not generate web token" + e);
            return null;
        }
    }

    /**
     * New token generator
     * @return generated random token
     */
    public static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }


    /**
     * Getting webtoken payload from web token
     *
     * @return parsed object WebTokenPayload
     */
    public WebTokenPayLoad getWebTokenPayload(){
        WebTokenParser webTokenParser = new WebTokenParser(token);
        try {
            return objectMapper.readValue(webTokenParser.parsPayload(), WebTokenPayLoad.class);
        }catch (IOException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not parse token payload " + e);
            return null;
        }
    }

    /**
     * parse web token header
     *
     * @return token header
     */

    WebTokenHeader getWebTokenHeader(){
        WebTokenParser webTokenParser = new WebTokenParser(token);
        try {
            return objectMapper.readValue(webTokenParser.parsHeader(), WebTokenHeader.class);
        }catch (IOException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not parse token header " + e);
            return null;
        }
    }

    /**
     * web token header object to map into json
     */
    public static class WebTokenHeader{
        String type = "JWT";

        /**
         * Default constructor
         */
        WebTokenHeader() {}

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /**
     * web token payload object to map into json
     */

    public static class WebTokenPayLoad{
        int userID;
        String token;
        String exp;
        String phoneNumber;
        String email;

        /**
         * Default constructor
         */
        public WebTokenPayLoad() {
        }

        private WebTokenPayLoad(Users user){
            this.userID = user.getClientID();
            this.token = user.getToken();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            this.exp = user.getExpirationDate().format(formatter);
            this.phoneNumber = user.getPhoneNo();
            this.email = user.getEmail();
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExp() {
            return exp;
        }

        public void setExp(String exp) {
            this.exp = exp;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    static class WebTokenParser{
        String token;
        WebTokenParser(String token){
            this.token = token;
        }

        String parsHeader(){
            return new String(base64Decoder.decode(token.split(Pattern.quote("."))[0]), StandardCharsets.UTF_8);
        }

        String parsPayload(){
            return new String(base64Decoder
                    .decode(token.split(Pattern.quote("."))[1])
                    , StandardCharsets.UTF_8);
        }
    }
}
