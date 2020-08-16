package com.alomonshi.bussinesslayer.company;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableCompanies;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.uiobjects.AddingCompany;
import com.alomonshi.restwebservices.message.ServerMessage;

public class ManagerCompanyService {

    private ServiceResponse serviceResponse;

    public ManagerCompanyService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Inserting new company
     * @param addingCompany to be inserted
     * @return result
     */
    public ServiceResponse insertNewCompany (AddingCompany addingCompany) {
        return ClientCompanyService.registerCompany(addingCompany, serviceResponse);
    }
}
