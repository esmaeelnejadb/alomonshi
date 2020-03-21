package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.ReserveTimeForm;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.util.List;

/**
 * deleting reserve times
 */
class ReserveTimeDeletor {
    private ServiceResponse serviceResponse;

    ReserveTimeDeletor(ServiceResponse serviceResponse) {
        this.serviceResponse = serviceResponse;
    }

    /**
     * Deleting all days unit reserve time
     * @param reserveTimeForm information for delete times
     * @return service response
     */
    ServiceResponse deleteUnitDayReserveTimeBetweenDays(ReserveTimeForm reserveTimeForm) {
        List<ReserveTime> reserveTimes = getExistedReservedTimesBetweenDays(
                reserveTimeForm.getUnitID()
                , reserveTimeForm.getStartDate()
                , reserveTimeForm.getEndDate());
        //Check if any reserved time is existed in that period or not
        if (reserveTimes.isEmpty()) {
            //If all days between start and end date should be deleted
            if (reserveTimeForm.getDayNumbers() == null || reserveTimeForm.getDayNumbers().isEmpty()) {
                boolean result = TableReserveTime
                        .deleteAllDayReserveTimesBetweenDays(
                                reserveTimeForm.getStartDate(),
                                reserveTimeForm.getEndDate(),
                                reserveTimeForm.getUnitID());
                if (result)
                    return serviceResponse.setResponse(true)
                            .setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    return serviceResponse.setResponse(false)
                            .setMessage(ServerMessage.FAULTMESSAGE);
            }
            //If some days between start and end date should be deleted
            else {
                boolean result = TableReserveTime
                        .deleteADayListReserveTimesBetweenDays(reserveTimeForm.getStartDate(),
                                reserveTimeForm.getEndDate(),
                                reserveTimeForm.getUnitID(),
                                reserveTimeForm.getDayNumbers());
                if (result)
                    return serviceResponse.setResponse(true)
                            .setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    return serviceResponse.setResponse(false)
                            .setMessage(ServerMessage.FAULTMESSAGE);
            }
        } else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.RESERVETIMEERROR_01)
                    .setResponseData(reserveTimes);
    }

    /**
     * Deleting midday reserve times
     * @param reserveTimeForm information for delete times
     * @return service response
     */
    ServiceResponse deleteUnitMiddayReserveTimeBetweenDays(ReserveTimeForm reserveTimeForm) {
        List<ReserveTime> reserveTimes = getExistedMiddayReservedTimesBetweenDays(
                reserveTimeForm.getUnitID()
                , reserveTimeForm.getStartDate()
                , reserveTimeForm.getEndDate()
                , reserveTimeForm.getMidday());
        //Check if any reserved time is existed in that period or not
        if (reserveTimes.isEmpty()) {
            //If all days midday times to be deleted
            if (reserveTimeForm.getDayNumbers() == null || reserveTimeForm.getDayNumbers().isEmpty()) {
                boolean result = TableReserveTime
                        .deleteMiddayReserveTimesBetweenDays(
                                reserveTimeForm.getStartDate()
                                , reserveTimeForm.getEndDate()
                                , reserveTimeForm.getUnitID()
                                , reserveTimeForm.getMidday());
                if (result)
                    return serviceResponse.setResponse(true)
                            .setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    return serviceResponse.setResponse(false)
                            .setMessage(ServerMessage.FAULTMESSAGE);
            }
            //If all days midday times to be deleted
            else {
                boolean result = TableReserveTime
                        .deleteMiddayADayListReserveTimesBetweenDays(
                                reserveTimeForm.getStartDate(),
                                reserveTimeForm.getEndDate(),
                                reserveTimeForm.getUnitID(),
                                reserveTimeForm.getDayNumbers(),
                                reserveTimeForm.getMidday());
                if (result)
                    return serviceResponse.setResponse(true)
                            .setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    return serviceResponse.setResponse(false)
                            .setMessage(ServerMessage.FAULTMESSAGE);
            }
        } else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.RESERVETIMEERROR_01)
                    .setResponseData(reserveTimes);
    }

    /**
     * Getting all day reserved time from database
     * @param unitID input
     * @param startDay input
     * @param endDay input
     * @return list of reserve time in that period
     */

    private static List<ReserveTime> getExistedReservedTimesBetweenDays(int unitID, int startDay, int endDay) {
        return TableReserveTime.getUnitReservedTimesBetweenDays(unitID,startDay, endDay);
    }

    /**
     * Getting midday reserved time from database
     * @param unitID input
     * @param startDay input
     * @param endDay input
     * @param middayID input
     * @return list of reserve time in that period
     */

    private static List<ReserveTime> getExistedMiddayReservedTimesBetweenDays(int unitID
            , int startDay
            , int endDay
            , MiddayID middayID) {
        return TableReserveTime.getUnitMiddayReservedTimesBetweenDays(unitID,startDay, endDay, middayID);
    }
}
