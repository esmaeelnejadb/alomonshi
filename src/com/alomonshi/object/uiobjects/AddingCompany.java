package com.alomonshi.object.uiobjects;

import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

public class AddingCompany {

    int clientID;
    private Company company;
    private Users user;

    @JsonView(JsonViews.ClientViews.class)
    public Company getCompany() {
        return company;
    }

    @JsonView(JsonViews.ClientViews.class)
    public int getClientID() {
        return clientID;
    }

    @JsonView(JsonViews.ClientViews.class)
    public Users getUser() {
        return user;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
