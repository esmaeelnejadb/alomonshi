package com.alomonshi.restwebservices.filters.authentication;

import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.filters.authentication.dofilter.DoAuthenticationFilter;

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
     * Filer authorized users
     * @param requestContext inject to filter method
     */
    @Override
    public void filter(ContainerRequestContext requestContext){
        DoAuthenticationFilter.doFilter(requestContext, UserLevels.CLIENT);
    }
}
