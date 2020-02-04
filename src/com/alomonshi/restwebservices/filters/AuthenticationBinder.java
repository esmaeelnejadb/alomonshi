package com.alomonshi.restwebservices.filters;

import com.alomonshi.restwebservices.annotation.AdminSecured;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;

@Provider
public class AuthenticationBinder implements DynamicFeature {

    @Override
    public void configure(ResourceInfo ri, FeatureContext fc) {
        Class<?> clazz = ri.getResourceClass();
        Method method = ri.getResourceMethod();
        if (method.isAnnotationPresent(AdminSecured.class)
                || clazz.isAnnotationPresent(AdminSecured.class)) {
            fc.register(new AdminAuthenticationFilter());
        }
    }
}