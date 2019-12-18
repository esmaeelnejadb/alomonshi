package com.alomonshi.object.entity;

import java.util.List;

public class ServicePicture {
    private int ID;
    private int serviceID;
    private List<String> picURL;
    private boolean isActive;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public List<String> getPicURL() {
        return picURL;
    }

    public void setPicURL(List<String> picURL) {
        this.picURL = picURL;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
