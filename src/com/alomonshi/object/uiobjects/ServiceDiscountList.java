package com.alomonshi.object.uiobjects;

import com.alomonshi.object.tableobjects.ServiceDiscount;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class ServiceDiscountList {

    private int clientID;
    private int unitID;
    private List<ServiceDiscount> serviceDiscounts;

    @JsonView(JsonViews.SubAdminViews.class)
    public int getClientID() {
        return clientID;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getUnitID() {
        return unitID;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public List<ServiceDiscount> getServiceDiscounts() {
        return serviceDiscounts;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setServiceDiscounts(List<ServiceDiscount> serviceDiscounts) {
        this.serviceDiscounts = serviceDiscounts;
    }
}
