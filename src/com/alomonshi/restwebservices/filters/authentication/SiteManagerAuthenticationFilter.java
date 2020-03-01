package com.alomonshi.restwebservices.filters.authentication;

import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authorization.Authorization;
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
        RequestHeaderCheck requestHeaderCheck = new RequestHeaderCheck(requestContext);
        if(requestHeaderCheck.isAuthorizationHeaderValid())
        {
            Authorization authorization = new Authorization(requestHeaderCheck
                    .getTokenFromRequest(), UserLevels.SITE_MANAGER);
            if(authorization.isNotAuthorized()
                    || authorization.isNotWebTokenBelongedToRequestedUser
                    (requestHeaderCheck.getClientIDFromRequestBody()))
                requestHeaderCheck.abortWithUnauthorized();
        }
    }
}
