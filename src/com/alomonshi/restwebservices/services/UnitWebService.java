package com.alomonshi.restwebservices.services;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.entity.Units;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/unit")
public class UnitWebService{

    @GET
    @Path("/getCompanyUnitList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Units> getCompanyUnitList(@QueryParam("companyID") int companyID) {
        try {
            return TableUnit.getUnits(companyID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}