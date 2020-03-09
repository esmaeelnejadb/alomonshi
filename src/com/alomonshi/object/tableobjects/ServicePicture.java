package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
//import org.codehaus.jackson.annotate.JsonIgnore;

public class ServicePicture {
    private int ID;
    private int serviceID;
    private String picURL;
    private boolean isActive;

    @JsonView(JsonViews.AdminViews.class)
    public int getID() {
        return ID;
    }

    @JsonView(JsonViews.AdminViews.class)
    public int getServiceID() {
        return serviceID;
    }

    @JsonView(JsonViews.NormalViews.class)
    public String getPicURL() {
        return picURL;
    }

    @JsonView(JsonViews.ManagerViews.class)
    public boolean isActive() {
        return isActive;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setPicURL(String picURL) {
        this.picURL = picURL;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
