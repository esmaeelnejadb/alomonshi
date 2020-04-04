package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.object.enums.FilterItem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/filter")
public class CompanyFilterWebServices {

    /**
     * Getting filter items
     * @return filter items
     */

    @GET
    @Path("/getFilterItems")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<FilterItem, String> getFilterItems(){
        try {
            return FilterItem.getFilterItemMap();
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}