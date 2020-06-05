package com.alomonshi.bussinesslayer.company;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.restwebservices.message.ServerMessage;

public class ManagerCompanyService {

    private ServiceResponse serviceResponse;

    public ManagerCompanyService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Inserting new company
     * @param company to be inserted
     * @return result
     */
    public ServiceResponse insertNewCompany (Company company) {

        if (TableCompanies.insert(company)) {
            serviceResponse.setResponse(true);
            serviceResponse.setMessage(ServerMessage.SUCCESSMESSAGE);
        }else {
            serviceResponse.setResponse(false);
            serviceResponse.setMessage(ServerMessage.FAULTMESSAGE);
        }
        return serviceResponse;
    }
}
