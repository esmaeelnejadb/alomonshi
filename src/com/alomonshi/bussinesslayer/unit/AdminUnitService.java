package com.alomonshi.bussinesslayer.unit;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.*;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.UnitAdmin;
import com.alomonshi.object.tableobjects.Units;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.utility.CopyNotNullProperties;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminUnitService {

    private ServiceResponse serviceResponse;
    private Units unit;

    /**
     * Constructor
     * @param serviceResponse injected object
     */
    public AdminUnitService(Units unit, ServiceResponse serviceResponse) {
        this.unit = unit;
        this.serviceResponse = serviceResponse;
    }

    /**
     * Constructor
     * @param serviceResponse to be injected
     */
    public AdminUnitService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Getting company unit list from database
     * @param companyID intended company
     * @return service response
     */
    public ServiceResponse getCompanyUnit(int companyID) {
        List<Units> units =  TableUnit.getUnits(companyID, false);
        if (!units.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(units);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.UNITERROR_01);
    }

    /**
     * Getting company unit list from database
     * @param adminID intended admin
     * @return service response
     */
    public ServiceResponse getAdminUnit(int adminID) {
        List<Units> units =  TableUnit.getAdminUnit(adminID);
        if (!units.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(units);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.UNITERROR_01);
    }

    /**
     * Inserting new unit
     * @return service response
     */
    public ServiceResponse insertNewUnit() {
        prepareUnitForInsertion();// Some action on unit object
        UnitAdmin unitAdmin = new UnitAdmin();//For inserting company Manager as unit admin
        int unitID = TableUnit.insertUnit(unit);
        if (unitID > 0) {
            // Setting unit id for unit admins
            unit.setID(unitID);
            prepareUnitAdmin(unitAdmin);
            boolean result = TableUnitAdmin.insertUnitAdmin(unitAdmin);
            if (result) {
                return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
    }

    /**
     * Editing a unit
     * @return service response
     */
    public ServiceResponse updateUnit() {
        prepareUnitForUpdate();
        if (unit != null) {
            if (TableUnit.updateUnit(unit)) {
                return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);

    }

    /**
     * Delete a unit
     * @return service response
     */
    public ServiceResponse deleteUnit() {
        prepareUnitForDelete();
        //Checking if there are reserve times in that unit
        List<ReserveTime> reserveTimes = TableReserveTime.getUnitNotYetReservedTime(unit.getID());
        if (reserveTimes.isEmpty()) {
            boolean result = TableUnit.delete(unit)
                    && TableUnitAdmin.deleteUnit(unit.getID()) // Delete unit admins
                    && TableService.deleteUnitServices(unit.getID()) // Delete unit service
                    && TableReserveTime.deleteUnit(unit.getID()); // Delete unit reserve times
            //Checking if any active reserved times is existed for that unit
            if (result) {
                return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.INTERNALERRORMESSAGE);
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.UNITERROR_02)
                    .setResponseData(reserveTimes);
    }

    /**
     * Copy new updated fields into old object got from table
     * @return new updated unit
     */

    private Units getCopiedUnitProperties() {
        BeanUtilsBean utilsBean = new CopyNotNullProperties();
        Units newUnit = TableUnit.getUnit(unit.getID(), false);
        if (newUnit.getID() != unit.getID())
            return null;
        try {
            utilsBean.copyProperties(newUnit, unit);
        }catch (IllegalAccessException | InvocationTargetException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Can not copy properties " + e);
        }
        return newUnit;
    }

    /**
     * Prepare a unit for insertion
     */
    private void prepareUnitForInsertion(){
        unit.setActive(true); // unit activation
        unit.setCreateDate(LocalDateTime.now());
    }

    /**
     * Prepare unit for update
     */
    private void prepareUnitForUpdate() {
        unit.setUpdateDate(LocalDateTime.now());
        boolean onlineReserve  = unit.isOnlineReserve();
        // fill null properties which get from ui with properties get from database
        unit = getCopiedUnitProperties();
        assert unit != null;
        unit.setOnlineReserve(onlineReserve);
    }

    /**
     * Prepare unit for delete
     */
    private void prepareUnitForDelete() {
        // Setting properties null to be unchanged in database
        unit.setUnitName(null);
        unit.setUnitDuration(0);
        unit.setCompanyID(0);
        unit.setPictureURL(null);
        unit.setRemark(null);
        unit.setCreateDate(null);
        unit.setUpdateDate(null);
        unit.setRemoveDate(LocalDateTime.now());
        // fill null properties which get from ui with properties get from database
        unit = getCopiedUnitProperties();
    }

    /**
     * Prepare unit admin for insertion
     * @param unitAdmin to be inserted
     */
    private void prepareUnitAdmin(UnitAdmin unitAdmin) {
        unitAdmin.setUnitID(unit.getID());
        unitAdmin.setManagerID(unit.getClientID());
        unitAdmin.setActive(true);
    }

}
