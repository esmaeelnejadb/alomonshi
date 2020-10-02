package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
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
        List<ReserveTime> reserveTimes = getExistedReservedTimesBetweenDays(reserveTimeForm);
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
        List<ReserveTime> reserveTimes = getExistedMiddayReservedTimesBetweenDays(reserveTimeForm);
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
     * @param reserveTimeForm input
     * @return list of reserve time in that period
     */

    private static List<ReserveTime> getExistedReservedTimesBetweenDays(ReserveTimeForm reserveTimeForm) {
        return TableReserveTime.getUnitReservedTimesBetweenDays(reserveTimeForm);
    }

    /**
     * Getting midday reserved time from database
     * @param reserveTimeForm input
     * @return list of reserve time in that period
     */

    private static List<ReserveTime> getExistedMiddayReservedTimesBetweenDays(ReserveTimeForm reserveTimeForm) {
        return TableReserveTime.getUnitMiddayReservedTimesBetweenDays(reserveTimeForm);
    }
}
