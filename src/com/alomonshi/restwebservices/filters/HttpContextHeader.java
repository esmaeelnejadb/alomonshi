package com.alomonshi.restwebservices.filters;

import javax.servlet.http.HttpServletResponse;

/**
 * Cross check origin pass
 */
public class HttpContextHeader {

    /**
     * Options for preflight check in authorization requests
     * @param httpServletResponse to be sent to client
     */
    public static void doOptions(HttpServletResponse httpServletResponse) {
        setAccessControlHeaders(httpServletResponse);
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * set header for pass Cross Origin Resource Check of browser
     * @param resp to be sent to client
     */
    private static void setAccessControlHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
    }
}
