package com.alomonshi.restwebservices.filters;

import com.alomonshi.bussinesslayer.accesscheck.authorization.Authorization;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.restwebservices.annotation.SiteManagerSecured;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@SiteManagerSecured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class SiteManagerAuthenticationFilter implements ContainerRequestFilter {

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
                    .getTokenFromRequest(requestContext), UserLevels.SITE_MANAGER);
            if(adminAuthorization.isNotAuthorized())
                RequestHeaderCheck.abortWithUnauthorized(requestContext);
        }
    }
}
