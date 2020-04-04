package com.alomonshi.restwebservices.servicesadmin;


import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.bussinesslayer.accesscheck.dbchangeaccesscheck.CheckAdminAuthority;
import com.alomonshi.bussinesslayer.report.ReportService;
import com.alomonshi.object.uiobjects.ReserveTimeForm;
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

@Path("/adminReport")
public class AdminReportWebService {

    private ServiceResponse serviceResponse;
    private ReportService reportService;

    @Context
    HttpServletResponse httpServletResponse;


    @JsonView(JsonViews.SubAdminViews.class)
    @CompanySubAdminSecured
    @POST
    @Path("/report")
    @Produces(MediaType.APPLICATION_JSON)
    public ServiceResponse getUnitServices(ReserveTimeForm reserveTimeForm) {
        serviceResponse = new ServiceResponse();
        try {
            if (CheckAdminAuthority.isUserUnitAuthorized(reserveTimeForm.getClientID(),
                    reserveTimeForm.getUnitID())) {
                reportService = new ReportService(serviceResponse);
                return reportService.getAdminReport(reserveTimeForm);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error : " + e);
            return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }
    }

    @OPTIONS
    @Path("/report")
    public void doOptionsForReport() {
        HttpContextHeader.doOptions(httpServletResponse);
    }
}
