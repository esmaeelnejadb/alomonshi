package com.alomonshi.restwebservices.filters;

import org.apache.http.HttpHeaders;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

public class RequestHeaderCheck {

    private static final String REALM = " ";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

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
     * @param requestContext inject to method
     */

    static void abortWithUnauthorized(ContainerRequestContext requestContext){
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE
                                , AUTHENTICATION_SCHEME + " realm = \"" + REALM + "\" ")
                        .build());
    }

    /**
     * Getting request authorization token
     * @param requestContext context to be processed
     * @return authorization token
     */

    private static String getAuthorizationHeaderFromRequest(ContainerRequestContext requestContext){
        return requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    }

    public String getTokenFromRequest(ContainerRequestContext requestContext){
        return getAuthorizationHeaderFromRequest(requestContext).substring(AUTHENTICATION_SCHEME.length()).trim();
    }

    /**
     *
     */
    public boolean isAuthorizationHeaderValid(ContainerRequestContext requestContext){
        if(!isTokenBasedAuthentication(getAuthorizationHeaderFromRequest(requestContext))){
            abortWithUnauthorized(requestContext);
            return false;
        }else return true;
    }

}
