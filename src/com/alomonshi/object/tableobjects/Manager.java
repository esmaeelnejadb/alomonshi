package com.alomonshi.object.tableobjects;

import com.alomonshi.object.enums.UserLevels;

public class Manager {
    private int ID;
    private int managerID;
    private int companyID;
    private UserLevels managerLevel;
    private boolean isActive;

    public int getID() {
        return ID;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public UserLevels getManagerLevel() {
        return managerLevel;
    }

    public void setManagerLevel(UserLevels managerLevel) {
        this.managerLevel = managerLevel;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
