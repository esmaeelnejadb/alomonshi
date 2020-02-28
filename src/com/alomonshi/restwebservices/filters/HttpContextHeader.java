package com.alomonshi.restwebservices.filters;

import javax.servlet.http.HttpServletResponse;

/**
 * Cross check origin pass
 */
public class HttpContextHeader {

    public static void doOptions(HttpServletResponse httpServletResponse) {
        setAccessControlHeaders(httpServletResponse);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    private static void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
    }
}
