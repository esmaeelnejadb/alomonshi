package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.tableobjects.ReserveTime;

import java.util.Collections;
import java.util.List;

public class ReserveTimeDeletor {
    private ServiceResponse serviceRespone;

    public ReserveTimeDeletor(ServiceResponse serviceRespone){
        this.serviceRespone = serviceRespone;
    }

    public ServiceResponse deleteUnitReserveTimeBetweenDays(int unitID, int startDay, int endDay){
        List<ReserveTime> reserveTimes = checkReservedTimes(unitID, startDay, endDay);
        return reserveTimes == null ? serviceRespone.setResponse(true) : serviceRespone.setResponse(false).setResponseData(Collections.singletonList(reserveTimes));
    }

    private static List<ReserveTime> checkReservedTimes(int unitID, int startDay, int endDay){
        return TableReserveTime.getUnitReservedtimesBetweenDays(unitID,startDay, endDay);
    }
}
