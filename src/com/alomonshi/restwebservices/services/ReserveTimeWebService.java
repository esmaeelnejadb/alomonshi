package com.alomonshi.restwebservices.services;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.entity.ReserveTime;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/reserveTime")
public class ReserveTimeWebService {

    @GET
    @Path("/getUnitReserveTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Enum, List<ReserveTime>> getReserveTime(@QueryParam("dateID") int dateID, @QueryParam("unitID") int unitID) {
        try {
            return TableReserveTime.getClientUnitReserveTimeInADay(dateID, unitID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}