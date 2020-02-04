package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.tableobjects.ReserveTime;

import java.util.Collections;
import java.util.List;

class ReserveTimeDeletor {
    private ServiceResponse serviceResponse;

    ReserveTimeDeletor(ServiceResponse serviceResponse){
        this.serviceResponse = serviceResponse;
    }

    ServiceResponse deleteUnitReserveTimeBetweenDays(int unitID, int startDay, int endDay){
        List<ReserveTime> reserveTimes = checkReservedTimes(unitID, startDay, endDay);
        if (reserveTimes == null )
            return serviceResponse.setResponse(TableReserveTime.deleteBetweenDays(startDay,endDay, unitID));
        else
            return serviceResponse.setResponse(false)
                .setResponseData(Collections.singletonList(reserveTimes));
    }

    private static List<ReserveTime> checkReservedTimes(int unitID, int startDay, int endDay){
        return TableReserveTime.getUnitReservedtimesBetweenDays(unitID,startDay, endDay);
    }
}
