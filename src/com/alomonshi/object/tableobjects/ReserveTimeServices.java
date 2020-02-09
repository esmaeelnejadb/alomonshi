package com.alomonshi.object.tableobjects;

public class ReserveTimeServices {
    private int id;
    private int reserveTimeID;
    private int serviceID;
    private int unitID;
    private boolean isActive;
    private int clientID;

    public int getId() {
        return id;
    }

    public ReserveTimeServices setId(int id) {
        this.id = id;
        return this;
    }

    public int getReserveTimeID() {
        return reserveTimeID;
    }

    public ReserveTimeServices setReserveTimeID(int reserveTimeID) {
        this.reserveTimeID = reserveTimeID;
        return this;
    }

    public int getServiceID() {
        return serviceID;
    }

    public ReserveTimeServices setServiceID(int serviceID) {
        this.serviceID = serviceID;
        return this;
    }

    public int getUnitID() {
        return unitID;
    }

    public ReserveTimeServices setUnitID(int unitID) {
        this.unitID = unitID;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public ReserveTimeServices setActive(boolean active) {
        isActive = active;
        return this;
    }

    public int getClientID() {
        return clientID;
    }

    public ReserveTimeServices setClientID(int clientID) {
        this.clientID = clientID;
        return this;
    }
}
