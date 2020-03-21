package com.alomonshi.object.tableobjects;

import com.alomonshi.restwebservices.adaptors.LocalDateAdaptor;
import com.alomonshi.restwebservices.adaptors.LocalDateTimeAdaptor;
import com.alomonshi.restwebservices.views.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ServiceDiscount {
    private int ID;
    private int clientID;// An admin who can edit the service count
    private int unitID;
    private int discount;
    private int serviceID;
    private LocalDate createDate;
    private LocalDate expireDate;
    private LocalDateTime removeDate;
    private boolean isActive;

    @JsonView(JsonViews.SubAdminViews.class)
    public int getID() {
        return ID;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getClientID() {
        return clientID;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getUnitID() {
        return unitID;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getDiscount() {
        return discount;
    }

    @JsonView(JsonViews.SubAdminViews.class)
    public int getServiceID() {
        return serviceID;
    }

    @XmlJavaTypeAdapter(LocalDateAdaptor.class)
    @JsonView(JsonViews.SubAdminViews.class)
    public LocalDate getCreateDate() {
        return createDate;
    }

    @XmlJavaTypeAdapter(LocalDateAdaptor.class)
    @JsonView(JsonViews.SubAdminViews.class)
    public LocalDate getExpireDate() {
        return expireDate;
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdaptor.class)
    @JsonView(JsonViews.ManagerViews.class)
    public LocalDateTime getRemoveDate() {
        return removeDate;
    }

    @JsonView(JsonViews.ManagerViews.class)
    public boolean isActive() {
        return isActive;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public void setRemoveDate(LocalDateTime removeDate) {
        this.removeDate = removeDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}