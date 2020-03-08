package com.alomonshi.object.uiobjects;

import com.alomonshi.object.tableobjects.Admin;

public class AdminEditObject {
    private int clientID;
    private int companyID;
    private int unitID;
    private int serviceID;
    private Admin companyAdmins;

    public int getClientID() {
        return clientID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public int getUnitID() {
        return unitID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public Admin getCompanyAdmins() {
        return companyAdmins;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setCompanyAdmins(Admin companyAdmins) {
        this.companyAdmins = companyAdmins;
    }
}
