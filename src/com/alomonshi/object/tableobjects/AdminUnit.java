package com.alomonshi.object.tableobjects;

public class AdminUnit {
    private int id;
    private int managerID;
    private int unitID;
    private boolean isActive;

    public int getId() {
        return id;
    }

    public int getManagerID() {
        return managerID;
    }

    public int getUnitID() {
        return unitID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
