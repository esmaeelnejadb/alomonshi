package com.alomonshi.object.uiobjects;


import java.util.List;

public class ReserveTimeList {
    private int clientID;
    private int unitID;
    private List<Integer> reserveTimeIDs;

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public List<Integer> getReserveTimeIDs() {
        return reserveTimeIDs;
    }

    public void setReserveTimeIDs(List<Integer> reserveTimeIDs) {
        this.reserveTimeIDs = reserveTimeIDs;
    }
}
