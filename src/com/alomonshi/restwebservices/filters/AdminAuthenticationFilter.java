package com.alomonshi.restwebservices.filters;

import com.alomonshi.bussinesslayer.authentication.AdminAuthorization;
import com.alomonshi.restwebservices.annotation.AdminSecured;
import org.apache.http.HttpHeaders;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


@AdminSecured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AdminAuthenticationFilter implements ContainerRequestFilter {

    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    /**
     * Filer authorized admins
     * @param requestContext inject to filter method
     */
    @Override
    public void filter(ContainerRequestContext requestContext){
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if(!isTokenBasedAuthentication(authorizationHeader))
            abortWithUnauthorized(requestContext);
        else {
            String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
            AdminAuthorization adminAuthorization = new AdminAuthorization(token);
            if(!adminAuthorization.isAuthorized())
                abortWithUnauthorized(requestContext);
        }
    }

    /**
     * Check if authorization header is valid
     * @param authorizationHeader to be checked
     * @return true if header is valid
     */
    private boolean isTokenBasedAuthentication(String authorizationHeader){
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    /**
     * reject unauthorized users
     * @param requestContext inject to method
     */

    private void abortWithUnauthorized(ContainerRequestContext requestContext){
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE
                                , AUTHENTICATION_SCHEME + " realm = \"" + REALM + "\" ")
                        .build());
    }
}
