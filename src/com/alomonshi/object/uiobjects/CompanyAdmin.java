package com.alomonshi.object.uiobjects;

import com.alomonshi.object.tableobjects.Units;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class CompanyAdmin {
    private int clientID; // ID if company manager who can edit admin list
    private int adminID; // ID of admin to be inserted
    private int companyID;
    private String adminName;
    private String adminPhone;
    private List<Units> adminUnit;

    @JsonView(JsonViews.AdminViews.class)
    public int getClientID() {
        return clientID;
    }

    @JsonView(JsonViews.AdminViews.class)
    public int getAdminID() {
        return adminID;
    }

    @JsonView(JsonViews.AdminViews.class)
    public int getCompanyID() {
        return companyID;
    }

    @JsonView(JsonViews.AdminViews.class)
    public String getAdminName() {
        return adminName;
    }

    @JsonView(JsonViews.AdminViews.class)
    public String getAdminPhone() {
        return adminPhone;
    }

    @JsonView(JsonViews.AdminViews.class)
    public List<Units> getAdminUnit() {
        return adminUnit;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public void setAdminUnit(List<Units> adminUnit) {
        this.adminUnit = adminUnit;
    }
}
