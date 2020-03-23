package com.alomonshi.restwebservices.filters.authentication.dofilter;

import com.alomonshi.bussinesslayer.accesscheck.webrequestaccesscheck.authorization.Authorization;
import com.alomonshi.object.enums.UserLevels;

import javax.ws.rs.container.ContainerRequestContext;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DoAuthenticationFilter {

    /**
     * Doing filter for every user type
     * @param requestContext fill filter response
     * @param userLevel to be authenticated
     */
    public static void doFilter(ContainerRequestContext requestContext, UserLevels userLevel){
        RequestHeaderCheck requestHeaderCheck = new RequestHeaderCheck(requestContext);
        try {
            if(requestHeaderCheck.isAuthorizationHeaderValid()) {
                Authorization authorization = new Authorization(requestHeaderCheck
                        .getTokenFromRequest(), userLevel);
                if(authorization.isNotAuthorized()
                        || (authorization
                        .isNotWebTokenBelongedToRequestedUser(requestHeaderCheck.getClientIDFromRequestBody())
                        && requestHeaderCheck.getClientIDFromRequestBody() != 0))
                    requestHeaderCheck.abortWithUnauthorized();
            }
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
            requestHeaderCheck.abortWithUnauthorized();
        }
    }
}
