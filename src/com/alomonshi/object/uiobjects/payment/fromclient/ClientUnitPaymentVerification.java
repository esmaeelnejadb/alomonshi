package com.alomonshi.object.uiobjects.payment.fromclient;

import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

public class ClientUnitPaymentVerification {
    private String authorityCode;
    private String status;
    private int clientID;

    @JsonView(JsonViews.ClientViews.class)
    public String getAuthorityCode() {
        return authorityCode;
    }

    @JsonView(JsonViews.ClientViews.class)
    public String getStatus() {
        return status;
    }

    @JsonView(JsonViews.ClientViews.class)
    public int getClientID() {
        return clientID;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
}
