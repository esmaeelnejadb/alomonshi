package com.alomonshi.object.tableobjects;

public class FavoriteCompany {
    private int ID;
    private int clientID;
    private int companyID;
    private boolean isActive;

    public int getID() {
        return ID;
    }

    public int getClientID() {
        return clientID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
