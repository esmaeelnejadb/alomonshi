package com.alomonshi.restwebservices.servicesclient;

import com.alomonshi.bussinesslayer.company.ClientCompanyService;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.enums.FilterItem;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.tableobjects.CompanyCategories;
import com.alomonshi.object.tableobjects.FavoriteCompany;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.filters.authentication.RequestHeaderCheck;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/company")
public class CompanyWebService{

    @Context
    HttpServletResponse httpServletResponse;

    @Context
    ContainerRequestContext requestContext;

    private RequestHeaderCheck requestHeaderCheck;

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
    public Company getCompany(@QueryParam("companyID") int companyID,
                              @QueryParam("clientID") int clientID){
        try{
            return TableCompanies.getCompany(companyID, clientID);
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
    public List<Company> getDiscountList(@QueryParam("limitNumber") int limitNumber){
        try {
            return ClientCompanyService.getAllDiscountCompanies(limitNumber);
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
                                         @QueryParam("lon") float lon,
                                         @QueryParam("clientID")int clientID){
        try {
            return ClientCompanyService.getSearchedCompanies(companyName, serviceName, lat, lon, categoryID, clientID);
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
                                         @QueryParam("filterItem")FilterItem filterItem,
                                         @QueryParam("clientID")int clientID){
        try {
            return ClientCompanyService.getFilteredCompanies(lat, lon, categoryID, filterItem, clientID);
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
                                                 @QueryParam("filterItem")FilterItem filterItem,
                                                 @QueryParam("clientID")int clientID){
        try {
            return ClientCompanyService.getFilteredSearchedCompanies(companyName,
                    serviceName,
                    lat,
                    lon,
                    categoryID,
                    filterItem,
                    clientID);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     * Getting client favorite companies
     * @return list of companies
     */
    @ClientSecured
    @POST
    @Path("/getClientFavoriteCompanies")
    public List<Company> getFavoriteCompanies() {
        try {
            requestHeaderCheck = new RequestHeaderCheck(requestContext);
            return ClientCompanyService.getFavoriteClientCompanies(requestHeaderCheck.getClientIDFromRequestBody());
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return null;
        }
    }

    /**
     * handling client favorite companies
     * @param favoriteCompany to be handled
     * @return response
     */
    @ClientSecured
    @POST
    @Path("/favoriteCompany")
    public Response handleFavoriteCompany(FavoriteCompany favoriteCompany) {
        try {
            return ClientCompanyService.handleFavoriteCompany(favoriteCompany)
                    ?
                    Response.ok().build()
                    :
                    Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @OPTIONS
    @Path("/getClientFavoriteCompanies")
    public void doOptionsForgetClientFavoriteCompany() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/favoriteCompany")
    public void doOptionsForFavoriteCompany() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}