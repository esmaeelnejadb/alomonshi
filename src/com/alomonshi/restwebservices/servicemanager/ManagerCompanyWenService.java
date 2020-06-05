package com.alomonshi.restwebservices.servicemanager;


import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.company.AdminCompanyService;
import com.alomonshi.bussinesslayer.company.ManagerCompanyService;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.uiobjects.AdminEditObject;
import com.alomonshi.restwebservices.annotation.CompanySubAdminSecured;
import com.alomonshi.restwebservices.annotation.SiteManagerSecured;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/managerCompany")
public class ManagerCompanyWenService {

    private ServiceResponse serviceResponse;
    private ManagerCompanyService managerCompanyService;

    @Context
    HttpServletResponse httpServletResponse;

    @JsonView(JsonViews.ManagerViews.class)
    @SiteManagerSecured
    @POST
    @Path("/company")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse insertNewCompany(Company company) {
        serviceResponse = new ServiceResponse();
        try {
            managerCompanyService = new ManagerCompanyService(serviceResponse);
            return managerCompanyService.insertNewCompany(company);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }
}
