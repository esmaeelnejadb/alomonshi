package com.alomonshi.restwebservices.filters;

import com.alomonshi.bussinesslayer.accesscheck.authorization.Authorization;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.restwebservices.annotation.ClientSecured;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@ClientSecured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class ClientAuthenticationFilter implements ContainerRequestFilter {

    /**
     * Filer authorized admins
     * @param requestContext inject to filter method
     */
    @Override
    public void filter(ContainerRequestContext requestContext){
        RequestHeaderCheck requestHeaderCheck = new RequestHeaderCheck();
        if(requestHeaderCheck.isAuthorizationHeaderValid(requestContext))
        {
            Authorization adminAuthorization = new Authorization(requestHeaderCheck
                    .getTokenFromRequest(requestContext), UserLevels.CLIENT);
            if(adminAuthorization.isNotAuthorized())
                RequestHeaderCheck.abortWithUnauthorized(requestContext);
        }
    }
}
