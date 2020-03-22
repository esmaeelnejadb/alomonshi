package com.alomonshi.bussinesslayer.report;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableService;
import com.alomonshi.object.uiobjects.AdminReport;
import com.alomonshi.object.uiobjects.ReserveTimeForm;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.util.List;

public class ReportService {

    private ServiceResponse serviceResponse;

    public ReportService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Getting admin report
     * @param reserveTimeForm information got from UI
     * @return service response
     */
    public ServiceResponse getAdminReport (ReserveTimeForm reserveTimeForm) {
        List<AdminReport> adminReportList = TableService.getAdminReport(
                reserveTimeForm.getUnitID(),
                reserveTimeForm.getStartDate(),
                reserveTimeForm.getEndDate());
        if (!adminReportList.isEmpty())
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(adminReportList);
        else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.REPORTERROR_01);
    }
}
