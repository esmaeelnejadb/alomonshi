package com.alomonshi.restwebservices.filters.authentication;

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
        DoAuthenticationFilter.doFilter(requestContext, UserLevels.COMPANY_ADMIN);
    }
}
