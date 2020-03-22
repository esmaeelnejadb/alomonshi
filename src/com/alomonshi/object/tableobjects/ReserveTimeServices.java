package com.alomonshi.object.tableobjects;

public class ReserveTimeServices {
    private int id;
    private int reserveTimeID;
    private int serviceID;
    private int unitID;
    private boolean isActive;
    private int clientID;
    private int servicePrice;
    private int discountID;

    public int getId() {
        return id;
    }

    public int getReserveTimeID() {
        return reserveTimeID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public int getUnitID() {
        return unitID;
    }

    public int getClientID() {
        return clientID;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getServicePrice() {
        return servicePrice;
    }

    public int getDiscountID() {
        return discountID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReserveTimeID(int reserveTimeID) {
        this.reserveTimeID = reserveTimeID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void setServicePrice(int servicePrice) {
        this.servicePrice = servicePrice;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }
}