package com.alomonshi.restwebservices.clientservices;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.tableobjects.Units;
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

    /**
     * Getting list of units in a single company
     * @param companyID ID of intended company
     * @return list of units
     */

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