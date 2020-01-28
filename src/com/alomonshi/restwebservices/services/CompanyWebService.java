package com.alomonshi.restwebservices.services;

import com.alomonshi.bussinesslayer.tableutils.CompanyUtils;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.tableobjects.Company;
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

    /**
     * getting list of companies in a specified category
     * @param categoryID intended category id for getting related companies
     * @return list of companies
     */
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

    /**
     * Getting single company all properties
     * @param companyID intended company
     * @return company object with all related properties
     */

    @GET
    @Path("/getCompany")
    @Produces(MediaType.APPLICATION_JSON)
    public Company getCompany(@QueryParam("companyID") int companyID){
        try{
            return TableCompanies.getCompany(companyID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     *
     * return list of best companies
     */

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

    /**
     *
     * @return list of newest company list
     */

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

    /**
     * Getting list of searched companies
     * @param categoryID intended category
     * @param companyName intended company
     * @param serviceName intended service
     * @param lat latitude of user
     * @param lon longitude of user
     * @return list of searched companies
     */

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