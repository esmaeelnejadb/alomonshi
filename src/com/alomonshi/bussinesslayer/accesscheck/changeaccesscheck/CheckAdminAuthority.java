package com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck;

import com.alomonshi.datalayer.dataaccess.TableService;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.datalayer.dataaccess.TableUnitAdmins;
import com.alomonshi.datalayer.dataaccess.TableManager;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckAdminAuthority {

    public CheckAdminAuthority() {
    }


    /**
     * check the intended unit id is in the list of related user unit ids
     * @return true is yes
     */

    public boolean isUserUnitAuthorized(int userID, int unitID){
        try {
            List<Integer> unitIDs = TableUnitAdmins.getManagerUnits(userID);
            if (unitIDs.contains(unitID))
                return true;
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Error in authorization " + e);
        }
        return false;
    }

    /**
     * check if the user is manager of the intended company
     * @return true if yes
     */
    public boolean isUserCompanyAuthorized(int userID, int companyID){
        try {
            List<Integer> managers = TableManager.getManagerCompanies(userID);
            if (managers.contains(companyID))
                return true;
        }catch (Exception e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Error in authorization " + e);
        }
        return false;
    }

    /**
     * Check if unit belong to company  got from ui
     * @param unitID to be checked unit got from ui
     * @param companyID to be checked company got from ui
     * @return true if unit belongs to that company
     */
    public boolean isUnitBelongToCompany(int unitID, int companyID) {
        return TableUnit.getUnit(unitID).getCompanyID() == companyID;
    }

    /**
     * Check if service belong to unit got from ui
     * @param serviceID to be checked service
     * @param unitID to be checked unit
     * @return true if service belongs to unit
     */
    public boolean isServiceBelongToUnit(int serviceID, int unitID) {
        return TableService.getService(serviceID).getUnitID() == unitID;
    }
}