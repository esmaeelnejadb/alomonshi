package com.alomonshi.bussinesslayer.unit;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableManager;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.datalayer.dataaccess.TableUnitAdmins;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.UnitAdmins;
import com.alomonshi.object.tableobjects.Units;
import com.alomonshi.restwebservices.message.ServerMessage;
import com.alomonshi.utility.CopyNotNullProperties;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnitService {

    private ServiceResponse serviceResponse;
    private Units unit;

    /**
     * Constructor
     * @param serviceResponse injected object
     */
    public UnitService(Units unit, ServiceResponse serviceResponse) {
        this.unit = unit;
        this.serviceResponse = serviceResponse;
    }

    /**
     * Constructor
     * @param serviceResponse to be injected
     */
    public UnitService(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Getting company unit list from database
     * @param companyID intended company
     * @return service response
     */
    public ServiceResponse getCompanyUnit(int companyID) {
        List<Units> units =  TableUnit.getUnits(companyID);
        if (!units.isEmpty()) {
            return serviceResponse.setResponse(true)
                    .setMessage(ServerMessage.SUCCESSMESSAGE)
                    .setResponseData(Collections.singletonList(units));
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.UNITERROR_01);
    }

    /**
     * Inserting new unit
     * @return service response
     */
    public ServiceResponse insertNewUnit() {
        prepareUnitForInsertion();// Some action on unit object
        List<UnitAdmins> unitAdmins = new ArrayList<>();
        if (fillUnitAdmins(unitAdmins)) {//Filling unit admins to be inserted in database
            int unitID = TableUnit.insertUnit(unit);
            if (unitID > 0) {
                // Setting unit id for unit admins
                setUnitAdminsUnitID(unitAdmins, unitID);
                boolean result = TableUnitAdmins.insertUnitAdmins(unitAdmins);
                if (result) {
                    return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
                }else
                    return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.ACCESSFAULT);
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
        //Checking if there are reserve times in that unit
        List<ReserveTime> reserveTimes = TableReserveTime.getUnitFutureReserveTime(unit.getID());
        if (reserveTimes.isEmpty()) {
            boolean result = TableUnit.delete(unit) && TableUnitAdmins.deleteUnit(unit.getID());
            //Checking if any active reserved times is existed for that unit
            if (result) {
                return serviceResponse.setResponse(true).setMessage(ServerMessage.SUCCESSMESSAGE);
            }else
                return serviceResponse.setResponse(false).setMessage(ServerMessage.FAULTMESSAGE);
        }else
            return serviceResponse.setResponse(false).setMessage(ServerMessage.UNITERROR_02);
    }

    /**
     * Copy new updated fields into old object got from table
     * @return new updated unit
     */

    private Units getCopiedUnitProperties() {
        BeanUtilsBean utilsBean = new CopyNotNullProperties();
        Units newUnit = TableUnit.getUnit(unit.getID());
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
     * Filling list of unit admins to be inserted in database
     * @param unitAdmins to be filled
     * @return true if all admins be manager of intended company
     */

    private boolean fillUnitAdmins(List<UnitAdmins> unitAdmins) {
        List<Integer> managers = TableManager.getCompanyManagers(unit.getCompanyID());
        for(Integer managerID : unit.getManagerIDs()) {
            if (!managers.contains(managerID))
                return false;
            UnitAdmins unitAdmin = new UnitAdmins();
            unitAdmin.setActive(true);
            unitAdmin.setManagerID(managerID);
            unitAdmin.setUnitID(unit.getID());
            unitAdmins.add(unitAdmin);
        }
        return true;
    }

    /**
     * Prepare a unit for insertion
     */
    private void prepareUnitForInsertion(){
        if (unit.getManagerIDs() == null)
            unit.setManagerIDs(new HashSet<>());
        unit.getManagerIDs().add(unit.getClientID()); //Adding current company manager to unit admin list
        unit.setActive(true); // unit activation
        unit.setCreateDate(LocalDateTime.now());
    }

    /**
     * Prepare unit for update
     */
    private void prepareUnitForUpdate() {
        unit.setUpdateDate(LocalDateTime.now());
        unit = getCopiedUnitProperties(); // fill null properties which get from ui with properties get from database
    }

    /**
     * Setting unit id for each unit admin object
     * @param unitAdmins list which unit id to be set
     * @param unitID to be set
     */
    private void setUnitAdminsUnitID(List<UnitAdmins> unitAdmins, int unitID) {
        for (UnitAdmins unitAdmin : unitAdmins) {
            unitAdmin.setUnitID(unitID);
        }
    }
}