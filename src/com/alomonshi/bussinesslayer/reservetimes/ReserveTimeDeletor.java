package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.ReserveTimeForm;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.util.Collections;
import java.util.List;

/**
 * deleting reserve times
 */
class ReserveTimeDeletor {
    private ServiceResponse serviceResponse;

    ReserveTimeDeletor(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    ServiceResponse deleteUnitReserveTimeBetweenDays(ReserveTimeForm reserveTimeForm) {
        List<ReserveTime> reserveTimes = getExistedReservedTimesBetweenDays(
                reserveTimeForm.getUnitID()
                , reserveTimeForm.getStartDate()
                , reserveTimeForm.getEndDate());
        if (reserveTimes.isEmpty())
            return serviceResponse.setResponse(TableReserveTime
                    .deleteBetweenDays(
                            reserveTimeForm.getStartDate()
                            , reserveTimeForm.getEndDate()
                            , reserveTimeForm.getUnitID()))
                    .setMessage(ServerMessage.SUCCESSMESSAGE);
        else
            return serviceResponse.setResponse(false)
                .setResponseData(Collections.singletonList(reserveTimes));
    }

    private static List<ReserveTime> getExistedReservedTimesBetweenDays(int unitID, int startDay, int endDay) {
        return TableReserveTime.getUnitReservedTimesBetweenDays(unitID,startDay, endDay);
    }
}
