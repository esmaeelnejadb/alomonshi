package com.alomonshi.restwebservices.filters.authentication.dofilter;

import org.apache.http.HttpHeaders;
import org.json.JSONObject;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class RequestHeaderCheck {

    private static final int MAX_ENTITY_READ = 1024;
    private static final String REALM = " ";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private ContainerRequestContext requestContext;

    RequestHeaderCheck(ContainerRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Check if authorization header is valid
     * @param authorizationHeader to be checked
     * @return true if header is valid
     */
    private static boolean isTokenBasedAuthentication(String authorizationHeader){
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    /**
     * reject unauthorized users
     */

    void abortWithUnauthorized(){
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE
                                , AUTHENTICATION_SCHEME + " realm = \"" + REALM + "\" ")
                        .build());
    }

    /**
     * Getting request authorization token
     * @return authorization token
     */

    private String getAuthorizationHeaderFromRequest(){
        return requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    }

    /**
     * Getting json web token from authentication part of request
     * @return json web token
     */

    String getTokenFromRequest(){
        return getAuthorizationHeaderFromRequest().substring(AUTHENTICATION_SCHEME.length()).trim();
    }

    /**
     * Check if header contain authorization
     */
    boolean isAuthorizationHeaderValid(){
        if(!isTokenBasedAuthentication(getAuthorizationHeaderFromRequest())){
            abortWithUnauthorized();
            return false;
        }else return true;
    }

    /**
     * Getting client id if exists in body to check with client id in authorization header
     * @return client id
     */

    int getClientIDFromRequestBody() {
        if (requestContext.hasEntity() && isJson()) {
            InputStream inputStreamOriginal = requestContext.getEntityStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStreamOriginal, MAX_ENTITY_READ);
            bufferedInputStream.mark(MAX_ENTITY_READ);
            byte[] bytes = new byte[MAX_ENTITY_READ];
            int read = 0;
            try {
                read = bufferedInputStream.read(bytes, 0, MAX_ENTITY_READ);
                bufferedInputStream.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            requestContext.setEntityStream(bufferedInputStream);
            return new JSONObject(new String(bytes, StandardCharsets.UTF_8)).getInt("clientID");
        }
        return 0;
    }

    /**
     * is body request contained json messsage
     * @return true if contain json message
     */
    private boolean isJson() {
        // define rules when to read body
        return requestContext.getMediaType().toString().contains("application/json");
    }
}
