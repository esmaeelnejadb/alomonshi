package com.alomonshi.bussinesslayer.accesscheck.changeaccesscheck;

import com.alomonshi.datalayer.dataaccess.*;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.ServiceDiscount;
import com.alomonshi.object.uiobjects.ServiceDiscountList;
import com.alomonshi.utility.DateTimeUtility;

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

    public static boolean isUserUnitAuthorized(int userID, int unitID){
        try {
            List<Integer> unitIDs = TableUnitAdmin.getAdminUnitIDs(userID);
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
    public static boolean isUserCompanyAuthorized(int userID, int companyID){
        try {
            List<Integer> managers = TableAdmin.getManagerCompanies(userID);
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
    public static boolean isUnitBelongToCompany(int unitID, int companyID) {
        return TableUnit.getUnit(unitID, false).getCompanyID() == companyID;
    }

    /**
     * Check if service belong to unit got from ui
     * @param serviceID to be checked service
     * @param unitID to be checked unit
     * @return true if service belongs to unit
     */
    public static boolean isServiceBelongToUnit(int serviceID, int unitID) {
        return TableService.getService(serviceID,
                DateTimeUtility.getCurrentGregorianDate()).getUnitID() == unitID;
    }

    /**
     * Check if admin can edit a user comment
     * @param comment to be checked
     * @return result
     */
    public static boolean canAdminEditComment(Comments comment) {
        //Getting reserve time of the comment to get unit id from that
        ReserveTime reserveTime = TableReserveTime.getReserveTime(comment.getReserveTimeID());
        return isUserUnitAuthorized(comment.getClientID(), reserveTime.getUnitID());
    }

    /**
     * Check if all service discount is correct
     * @param serviceDiscountList to be checked
     * @return result
     */
    public static boolean isServiceDiscountListEditAuthorized(ServiceDiscountList serviceDiscountList) {
        List<Integer> serviceIDs = TableService.getUnitServiceIDs(serviceDiscountList.getUnitID());
        if (isUserUnitAuthorized(serviceDiscountList.getClientID(), serviceDiscountList.getUnitID())) {
            for (ServiceDiscount serviceDiscount : serviceDiscountList.getServiceDiscounts()) {
                if (!serviceIDs.contains(serviceDiscount.getServiceID()))
                    return false;
            }
        }else
            return false;
        return true;
    }
}