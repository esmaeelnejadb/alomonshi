package com.alomonshi.restwebservices.servicesadmin;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck.CheckAdminAuthority;
import com.alomonshi.bussinesslayer.service.ServicesService;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.object.uiobjects.AdminEditObject;
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

@Path("/adminService")
public class AdminServiceWebService {

    private ServicesService servicesService;
    private ServiceResponse serviceResponse;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * Getting list of unit services
     * @param adminEditObject got from ui
     * @return service response
     */
    @JsonView(JsonViews.SubAdminViews.class)
    @CompanyAdminSecured
    @POST
    @Path("/getUnitService")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getUnitServices(AdminEditObject adminEditObject) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserUnitAuthorized(adminEditObject.getClientID(), adminEditObject.getUnitID())) {
                servicesService = new ServicesService(serviceResponse);
                return servicesService.getUnitServices(adminEditObject.getUnitID());
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @JsonView(JsonViews.SubAdminViews.class)
    @CompanyAdminSecured
    @POST
    @Path("/service")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse insertNewService(Services service) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserUnitAuthorized(service.getClientID(), service.getUnitID())) {
                servicesService = new ServicesService(service, serviceResponse);
                return servicesService.insertNewService();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @JsonView(JsonViews.SubAdminViews.class)
    @CompanyAdminSecured
    @PUT
    @Path("/service")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse updateService(Services service) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserUnitAuthorized(service.getClientID(), service.getUnitID())
                    && CheckAdminAuthority.isServiceBelongToUnit(service.getID(), service.getUnitID())) {
                servicesService = new ServicesService(service, serviceResponse);
                return servicesService.updateService();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @JsonView(JsonViews.SubAdminViews.class)
    @CompanyAdminSecured
    @DELETE
    @Path("/service")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse deleteService(Services service) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserUnitAuthorized(service.getClientID(), service.getUnitID())
                    && CheckAdminAuthority.isServiceBelongToUnit(service.getID(), service.getUnitID())) {
                servicesService = new ServicesService(service, serviceResponse);
                return servicesService.deleteService();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/getUnitService")
    public void doOptionsForAdminGettingService() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/service")
    public void doOptionsForAdminService() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}