package com.alomonshi.restwebservices.filters;

import com.alomonshi.bussinesslayer.webrequestaccesscheck.authorization.Authorization;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.restwebservices.annotation.CompanyAdminSecured;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@CompanyAdminSecured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class CompanyAdminAuthenticationFilter implements ContainerRequestFilter {

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
                    .getTokenFromRequest(requestContext), UserLevels.COMPANY_ADMIN);
            if(adminAuthorization.isNotAuthorized())
                RequestHeaderCheck.abortWithUnauthorized(requestContext);
        }
    }
}
