package com.alomonshi.restwebservices.servicesadmin;
import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.dbchangeaccesscheck.CheckAdminAuthority;
import com.alomonshi.bussinesslayer.unit.AdminUnitService;
import com.alomonshi.object.tableobjects.Units;
import com.alomonshi.object.uiobjects.AdminEditObject;
import com.alomonshi.restwebservices.annotation.CompanyAdminSecured;
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

@Path("/adminUnit")
public class AdminUnitWebService {

    private AdminUnitService unitService;
    private ServiceResponse serviceResponse;

    @Context
    HttpServletResponse httpServletResponse;

    /**
     * Getting unit list of a company
     * @param adminEditObject information got from ui
     * @return List of units
     */

    @JsonView(JsonViews.AdminViews.class)
    @CompanySubAdminSecured
    @POST
    @Path("/companyUnits")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getCompanyUnitList(AdminEditObject adminEditObject) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserCompanyAuthorized(adminEditObject.getClientID()
                    , adminEditObject.getCompanyID())) {
                unitService = new AdminUnitService(serviceResponse);
                return unitService.getCompanyUnit(adminEditObject.getCompanyID());
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Getting unit list of a company
     * @param adminEditObject information got from ui
     * @return List of units
     */

    @JsonView(JsonViews.AdminViews.class)
    @CompanySubAdminSecured
    @POST
    @Path("/units")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getAdminUnitList(AdminEditObject adminEditObject) {
        serviceResponse = new ServiceResponse();
        try {
            unitService = new AdminUnitService(serviceResponse);
            return unitService.getAdminUnit(adminEditObject.getClientID());
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Inserting new units
     * @param unit to be inserted
     * @return service response
     */

    @JsonView(JsonViews.AdminViews.class)
    @CompanyAdminSecured
    @POST
    @Path("/unit")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse insertNewUnit(Units unit) {
        serviceResponse = new ServiceResponse();
        try {
            unitService = new AdminUnitService(unit, serviceResponse);
            if (CheckAdminAuthority.isUserCompanyAuthorized(unit.getClientID(), unit.getCompanyID())) {
                return unitService.insertNewUnit();
            }else
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @JsonView(JsonViews.AdminViews.class)
    @CompanyAdminSecured
    @PUT
    @Path("/unit")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse updateUnit(Units unit) {
        serviceResponse = new ServiceResponse();
        try {
            unitService = new AdminUnitService(unit, serviceResponse);
            if (CheckAdminAuthority.isUserCompanyAuthorized(unit.getClientID(), unit.getCompanyID())
                    && CheckAdminAuthority.isUnitBelongToCompany(unit.getID(), unit.getCompanyID())) {
                return unitService.updateUnit();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    /**
     * Deleting a unit
     * @param unit to be delete
     * @return service response
     */
    @JsonView(JsonViews.AdminViews.class)
    @CompanyAdminSecured
    @DELETE
    @Path("/unit")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse deleteUnit(Units unit) {
        serviceResponse = new ServiceResponse();
        try {
            unitService = new AdminUnitService(unit, serviceResponse);
            if (CheckAdminAuthority.isUserCompanyAuthorized(unit.getClientID(), unit.getCompanyID())
                    && CheckAdminAuthority.isUnitBelongToCompany(unit.getID(), unit.getCompanyID())) {
                return unitService.deleteUnit();
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/companyUnits")
    public void doOptionsForCompanyUnits() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/units")
    public void doOptionsForAdminUnits() {
        HttpContextHeader.doOptions(httpServletResponse);
    }

    @OPTIONS
    @Path("/unit")
    public void doOptionsForAdminUnit() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}
