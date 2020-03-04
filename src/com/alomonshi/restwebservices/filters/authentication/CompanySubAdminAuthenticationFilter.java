package com.alomonshi.restwebservices.filters.authentication;

import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authorization.Authorization;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@CompanySubAdminSecured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class CompanySubAdminAuthenticationFilter implements ContainerRequestFilter {

    /**
     * Filer authorized admins
     * @param requestContext inject to filter method
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        RequestHeaderCheck requestHeaderCheck = new RequestHeaderCheck(requestContext);
        if(requestHeaderCheck.isAuthorizationHeaderValid()) {
            Authorization authorization = new Authorization(requestHeaderCheck
                    .getTokenFromRequest(), UserLevels.COMPANY_SUB_ADMIN);
            if(authorization.isNotAuthorized()
                    || (authorization
                    .isNotWebTokenBelongedToRequestedUser(requestHeaderCheck.getClientIDFromRequestBody())
                    && requestHeaderCheck.getClientIDFromRequestBody() != 0))
                requestHeaderCheck.abortWithUnauthorized();
        }
    }
}