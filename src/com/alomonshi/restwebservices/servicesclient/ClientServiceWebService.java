package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.service.ServicesService;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/service")
public class ClientServiceWebService {

    private ServicesService servicesService;

    /**
     * Getting service by id
     * @param serviceID to be got from database
     * @return service object
     */
    @JsonView(JsonViews.NormalViews.class)
    @GET
    @Path("/getCategoryList")
    @Produces(MediaType.APPLICATION_JSON)
    public Services getCategoryList(@QueryParam("serviceID") int serviceID){
        servicesService = new ServicesService();
        return servicesService.getServiceByIdForClient(serviceID);
    }
}
