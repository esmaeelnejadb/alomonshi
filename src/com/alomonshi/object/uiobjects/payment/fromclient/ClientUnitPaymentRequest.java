package com.alomonshi.object.uiobjects.payment.fromclient;

import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class ClientUnitPaymentRequest {
    private int clientID;
    private int unitID;
    private String amount;
    private boolean requestType;
    private int reserveTimeID;
    private List<Services> servicesList;

    @JsonView(JsonViews.ClientViews.class)
    public int getClientID() {
        return clientID;
    }

    @JsonView(JsonViews.ClientViews.class)
    public int getUnitID() {
        return unitID;
    }

    @JsonView(JsonViews.ClientViews.class)
    public String getAmount() {
        return amount;
    }

    @JsonView(JsonViews.ClientViews.class)
    public boolean getRequestType() {
        return requestType;
    }

    @JsonView(JsonViews.ClientViews.class)
    public int getReserveTimeID() {
        return reserveTimeID;
    }

    @JsonView(JsonViews.ClientViews.class)
    public List<Services> getServicesList() {
        return servicesList;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setRequestType(boolean requestType) {
        this.requestType = requestType;
    }

    public void setReserveTimeID(int reserveTimeID) {
        this.reserveTimeID = reserveTimeID;
    }

    public void setServicesList(List<Services> servicesList) {
        this.servicesList = servicesList;
    }
}
