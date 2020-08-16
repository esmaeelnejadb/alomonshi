package com.alomonshi.bussinesslayer.company;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableAdmin;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.util.List;

public class AdminCompanyService {

    private ServiceResponse serviceResponse;

    public AdminCompanyService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Getting manager company
     * @param adminID to be got cmopany from
     * @return service response including company list
     */
    public ServiceResponse getAdminCompany (int adminID){
        List<Company> companies = TableAdmin.getAdminCompanies(adminID);

        if (!companies.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(companies);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.COMPANYERROR_01);
    }

    /**
     * Getting manager company including inactive companies
     * @param adminID to be got cmopany from
     * @return service response including company list
     */
    public ServiceResponse getAdminCompanyIncludingInactive (int adminID){
        List<Company> companies = TableAdmin.getAdminCompaniesIncludingInactive(adminID);
        if (!companies.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(companies);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.COMPANYERROR_01);
    }

}
