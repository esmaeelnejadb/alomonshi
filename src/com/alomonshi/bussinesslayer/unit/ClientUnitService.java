package com.alomonshi.bussinesslayer.unit;

import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.tableobjects.Units;

public class ClientUnitService {

    /**
     * Getting  a unit by its id including services
     * @param unitID to be got
     * @return object which should be returned
     */
    public Units getUnitByID (int unitID) {
        return TableUnit.getUnitWithServices(unitID);
    }

}
