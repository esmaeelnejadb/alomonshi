package com.alomonshi.restwebservices.servicesadmin;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.dbchangeaccesscheck.CheckAdminAuthority;
import com.alomonshi.bussinesslayer.adminmanagement.AdminManagementService;
import com.alomonshi.object.uiobjects.AdminEditObject;
import com.alomonshi.object.uiobjects.CompanyAdmin;
import com.alomonshi.restwebservices.annotation.CompanyAdminSecured;
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

@Path("/adminManagement")
public class AdminManagementWebService {

    private ServiceResponse serviceResponse;
    private AdminManagementService adminManagementService;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * Getting company admins
     * @param adminEditObject data got from ui
     * @return service response
     */
    @JsonView(JsonViews.AdminViews.class)
    @CompanyAdminSecured
    @POST
    @Path("/getAdminList")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getAdminList(AdminEditObject adminEditObject) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserCompanyAuthorized(adminEditObject.getClientID()
                            , adminEditObject.getCompanyID())) {
                adminManagementService = new AdminManagementService(serviceResponse);
                return adminManagementService.getCompanyAdminList(adminEditObject.getCompanyID());
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Adding admin to a company
     * @param companyAdmin to be add
     * @return service response
     */
    @JsonView(JsonViews.AdminViews.class)
    @CompanyAdminSecured
    @POST
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse addNewAdmin(CompanyAdmin companyAdmin) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserCompanyAuthorized(companyAdmin.getClientID()
                    , companyAdmin.getCompanyID())) {
                adminManagementService = new AdminManagementService(companyAdmin, serviceResponse);
                return adminManagementService.addAdmin();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Adding admin to a company
     * @param companyAdmin to be add
     * @return service response
     */
    @JsonView(JsonViews.AdminViews.class)
    @CompanyAdminSecured
    @PUT
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse editAdminUnit(CompanyAdmin companyAdmin) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority
                    .isUserCompanyAuthorized(companyAdmin.getClientID(), companyAdmin.getCompanyID())) {
                if (CheckAdminAuthority
                        .isUserCompanyAuthorized(companyAdmin.getAdminID(), companyAdmin.getCompanyID())) {
                    adminManagementService = new AdminManagementService(companyAdmin, serviceResponse);
                    return adminManagementService.editAdminUnits();
                }else
                    return serviceResponse.setResponse(false).setMessage(ServerMessage.ADMINERROR_03);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Adding admin to a company
     * @param companyAdmin to be add
     * @return service response
     */
    @JsonView(JsonViews.AdminViews.class)
    @CompanyAdminSecured
    @DELETE
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse deleteAdmin(CompanyAdmin companyAdmin) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserCompanyAuthorized(companyAdmin.getClientID()
                    , companyAdmin.getCompanyID())) {
                if (CheckAdminAuthority.isUserCompanyAuthorized(companyAdmin.getAdminID()
                        , companyAdmin.getCompanyID())) {
                    adminManagementService = new AdminManagementService(companyAdmin, serviceResponse);
                    return adminManagementService.deleteAdmin();
                }else
                    return serviceResponse.setResponse(false).setMessage(ServerMessage.ADMINERROR_03);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/getAdminList")
    public void doOptionsForGetAdminList() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/admin")
    public void doOptionsForGetAdmin() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}
