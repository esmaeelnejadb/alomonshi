package com.alomonshi.restwebservices.servicesadmin;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.company.AdminCompanyService;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.uiobjects.AdminEditObject;
import com.alomonshi.restwebservices.annotation.ClientSecured;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/adminCompany")
public class AdminCompanyWebService {
    private AdminCompanyService adminCompanyService;
    private ServiceResponse serviceResponse;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * Getting list of a company of admin
     * @param adminEditObject information got from ui
     * @return List of companies
     */
    @JsonView(JsonViews.SubAdminViews.class)
    @CompanySubAdminSecured
    @POST
    @Path("/getAdminCompanies")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getCompanyCompaniesList(AdminEditObject adminEditObject) {
        serviceResponse = new ServiceResponse();
        try {
            adminCompanyService = new AdminCompanyService(serviceResponse);
            return adminCompanyService.getAdminCompany(adminEditObject.getClientID());
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Getting admin registered companies by his phone
     * @param client to be checked
     * @return service response including admin companies list
     */

    @POST
    @Path("/getAdminCompaniesByPhone")
    @ClientSecured
    @Consumes(MediaType.APPLICATION_JSON)
    public ServiceResponse getCompanyCompaniesListIncludingInactive
    (Users client) {
        serviceResponse = new ServiceResponse();
        try {
            Users user = TableClient.getUser(client.getPhoneNo());
            if (user.getUserLevel().getValue() >= UserLevels.COMPANY_ADMIN.getValue()) {
                adminCompanyService = new AdminCompanyService(serviceResponse);
                return adminCompanyService.getAdminCompanyIncludingInactive(user.getClientID());
            }else {
                return serviceResponse.setResponse(false);
            }
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error " + e);
            return serviceResponse.setResponse(false);
        }
    }

    @OPTIONS
    @Path("/getAdminCompanies")
    public void doOptionsForGetAdminCompanies() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/getAdminCompaniesByPhone")
    public void doOptionsForGetAdminCompaniesByPhone() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}
