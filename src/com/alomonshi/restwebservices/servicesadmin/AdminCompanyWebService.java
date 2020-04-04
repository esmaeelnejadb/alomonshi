package com.alomonshi.restwebservices.servicesadmin;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.company.AdminCompanyService;
import com.alomonshi.object.uiobjects.AdminEditObject;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;
import com.alomonshi.restwebservices.filters.HttpContextHeader;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public ServiceResponse getCompanyUnitList(AdminEditObject adminEditObject) {
        serviceResponse = new ServiceResponse();
        try {
            adminCompanyService = new AdminCompanyService(serviceResponse);
            return adminCompanyService.getAdminCompany(adminEditObject.getClientID());
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/getAdminCompanies")
    public void doOptionsForGetAdminCompanies() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}
