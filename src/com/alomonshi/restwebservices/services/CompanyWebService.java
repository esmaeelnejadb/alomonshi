package com.alomonshi.restwebservices.services;

import com.alomonshi.bussinesslayer.tableutils.CompanyUtils;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.Company;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/company")
public class CompanyWebService{

    public CompanyWebService(){}

    @GET
    @Path("/getCategoryList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getCategoryList(@QueryParam("categoryID") int categoryID){
        try{
            return TableCompanies.getCompanies(categoryID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    @GET
    @Path("/getBestList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getBestList(){
        try {
            return TableCompanies.getBestCompanies(10);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    @GET
    @Path("/getNewestList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getNewestList(){
        try {
            return TableCompanies.getNewestCompanies(10);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    @GET
    @Path("/getSearchedList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getSearchedList(@QueryParam("categoryID") int categoryID, @QueryParam("companyName") String companyName,
                                         @QueryParam("serviceName") String serviceName, @QueryParam("lat") float lat,
                                         @QueryParam("lon") float lon){
        try {
            List<Company> companies = CompanyUtils.getSearchedCompanies(categoryID, companyName, serviceName);
            if(lat != 0 && lon != 0)
                companies = CompanyUtils.getNearestCompanies(companies, lat, lon);
            return companies;
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}