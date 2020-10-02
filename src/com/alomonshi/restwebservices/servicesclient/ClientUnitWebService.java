package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.unit.ClientUnitService;
import com.alomonshi.object.tableobjects.Units;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/unit")
public class ClientUnitWebService {

    private ClientUnitService clientUnitService;

    /**
     * Getting service by id
     * @param unit to be got from database
     * @return service object
     */
    @ClientSecured
    @JsonView(JsonViews.ClientViews.class)
    @POST
    @Path("/unit")
    @Produces(MediaType.APPLICATION_JSON)
    public Units getUnit(Units unit){
        clientUnitService = new ClientUnitService();
        try {
            return  clientUnitService.getUnitByID(unit.getID());
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}
