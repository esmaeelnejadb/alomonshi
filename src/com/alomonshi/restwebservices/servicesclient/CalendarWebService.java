package com.alomonshi.restwebservices.servicesclient;


import com.alomonshi.bussinesslayer.calendar.CalendarService;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.uiobjects.reservetimecalendar.ReserveTimesOfMonth;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/calendar")
public class CalendarWebService {

    @JsonView(JsonViews.NormalViews.class)
    @GET
    @Path("/getReserveTimeOfMonth")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReserveTimesOfMonth> getReserveTimeOfMonthList(@QueryParam("unitID") int unitID){
        try{
            return CalendarService.getReserveTimesDaysofMonth(unitID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}