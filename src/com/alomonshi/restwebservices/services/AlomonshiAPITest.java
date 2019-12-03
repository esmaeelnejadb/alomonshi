package com.alomonshi.restwebservices.services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/alomonshiAPITest")
public class AlomonshiAPITest {
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMethod() {
        return "Get Method";
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    public String postMethod() {
        return "Post Method";
    }
}
