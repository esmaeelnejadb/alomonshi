package com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck;

import com.alomonshi.datalayer.dataaccess.TableUnitAdmins;
import com.alomonshi.datalayer.dataaccess.TableManager;
import com.alomonshi.object.tableobjects.UnitAdmins;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckAdminAuthority {
    private int userID;
    private int toBeCheckedID;

    /**
     * Constructor
     * @param userID to injected
     * @param toBeCheckedID to be injected
     */
    public CheckAdminAuthority(int userID, int toBeCheckedID) {
        this.userID = userID;
        this.toBeCheckedID = toBeCheckedID;
    }


    /**
     * check the intended unit id is in the list of related user unit ids
     * @return true is yes
     */

    public boolean isUserUnitAuthorized(){
        try {
            List<UnitAdmins> unitAdmins = TableUnitAdmins.getManagerUnits(userID);
            for (UnitAdmins unitAdmin : unitAdmins) {
                if (unitAdmin.getUnitID() == toBeCheckedID)
                    return true;
            }
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error in authorization " + e);
        }
        return false;
    }

    /**
     * check if the user is manager of the intended company
     * @return true if yes
     */
    public boolean isUserCompanyAuthorized(){
        try {
            List<Integer> managers = TableManager.getManagerCompanies(userID);
            if (managers.contains(toBeCheckedID))
                return true;
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error in authorization " + e);
        }
        return false;
    }
}