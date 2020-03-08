package com.alomonshi.object.tableobjects;

import com.alomonshi.object.enums.UserLevels;

import java.util.List;

public class Admin {
    private int ID;
    private int managerID;
    private int companyID;
    private List<Units> units;
    private UserLevels managerLevel;
    private boolean isActive;

    public int getID() {
        return ID;
    }

    public int getManagerID() {
        return managerID;
    }

    public int getCompanyID() {
        return companyID;
    }

    public List<Units> getUnits() {
        return units;
    }

    public UserLevels getManagerLevel() {
        return managerLevel;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public void setUnits(List<Units> units) {
        this.units = units;
    }

    public void setManagerLevel(UserLevels managerLevel) {
        this.managerLevel = managerLevel;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        else if (!Admin.class.isAssignableFrom(object.getClass()))
            return false;
        else {
            Admin admin = (Admin) object;
            return admin.getManagerID() == this.managerID
                    && admin.getCompanyID() == this.getCompanyID();
        }

    }
}