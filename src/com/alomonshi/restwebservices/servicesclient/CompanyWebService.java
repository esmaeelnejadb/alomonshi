package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.company.ClientCompanyService;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.enums.FilterItem;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.tableobjects.CompanyCategories;
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

@Path("/company")
public class CompanyWebService{

    /**
     * getting list of companies in a specified category
     * @param categoryID intended category id for getting related companies
     * @return list of companies
     */
    @JsonView(JsonViews.NormalViews.class)
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

    @JsonView(JsonViews.ClientViews.class)
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
     * return list of best companies
     *
     */
    @JsonView(JsonViews.ClientViews.class)
    @GET
    @Path("/getBestList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CompanyCategories> getBestList(){
        try {
            return ClientCompanyService.getBestCompaniesInCategories();
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     * @return list of newest company list
     */
    @JsonView(JsonViews.ClientViews.class)
    @GET
    @Path("/getNewestList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CompanyCategories> getNewestList(){
        try {
            return ClientCompanyService.getNewestCompaniesInCategories();
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     *
     * @return list of discount company list
     */
    @JsonView(JsonViews.ClientViews.class)
    @GET
    @Path("/getDiscountList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getDiscountList(){
        try {
            return ClientCompanyService.getAllDiscountCompanies();
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
    @JsonView(JsonViews.NormalViews.class)
    @GET
    @Path("/getSearchedList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getSearchedList(@QueryParam("categoryID") int categoryID,
                                         @QueryParam("companyName") String companyName,
                                         @QueryParam("serviceName") String serviceName,
                                         @QueryParam("lat") float lat,
                                         @QueryParam("lon") float lon){
        try {
            return ClientCompanyService.getSearchedCompanies(companyName, serviceName, lat, lon, categoryID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     * Getting filtered companies in a category
     * @param categoryID selected category
     * @param lat latitude
     * @param lon longitude
     * @param filterItem item to be filtered (Refer to Enum FilterItem in package enums)
     * @return list of filtered companies
     */

    @JsonView(JsonViews.NormalViews.class)
    @GET
    @Path("/getFilteredList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getFilteredList(@QueryParam("categoryID") int categoryID,
                                         @QueryParam("lat") float lat,
                                         @QueryParam("lon") float lon,
                                         @QueryParam("filterItem")FilterItem filterItem){
        try {
            return ClientCompanyService.getFilteredCompanies(lat, lon, categoryID, filterItem);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     * Getting filtered searched companies in a category
     * @param categoryID selected category
     * @param serviceName searched service
     * @param companyName searched company
     * @param lat latitude
     * @param lon longitude
     * @param filterItem item to be filtered (Refer to Enum FilterItem in package enums)
     * @return list of filtered companies
     */

    @JsonView(JsonViews.NormalViews.class)
    @GET
    @Path("/getFilteredSearchedList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> getFilteredSearchedList(@QueryParam("categoryID") int categoryID,
                                                 @QueryParam("companyName") String companyName,
                                                 @QueryParam("serviceName") String serviceName,
                                                 @QueryParam("lat") float lat,
                                                 @QueryParam("lon") float lon,
                                                 @QueryParam("filterItem")FilterItem filterItem){
        try {
            return ClientCompanyService.getFilteredSearchedCompanies(companyName,
                    serviceName,
                    lat,
                    lon,
                    categoryID,
                    filterItem);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }
}