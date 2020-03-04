package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.datalayer.dataaccess.TableService;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Check reserve time availability for reservation process
 */

class ReserveTimeCheck {
    private ReserveTime reserveTime;
    private ServiceResponse serviceResponse;

    ReserveTimeCheck(ReserveTime reserveTime, ServiceResponse serviceResponse){
        this.reserveTime = reserveTime;
        this.serviceResponse = serviceResponse;
    }

    /**
     * Check if time is available
     * @return service response based on different states
     */

    ServiceResponse checkTimeAvailability() {
        if (isTimeAvailable()) {
            if (isTimeDurationPassServiceDuration())
                return serviceResponse.setResponse(true);
            else {
                return serviceResponse.setResponse(false)
                        .setMessage(ServerMessage.RESERVETIMEERROR_03);
            }
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.RESERVETIMEERROR_04);
    }

    /**
     * Check if time is reservable
     * @return true if yes
     */
    private boolean isTimeAvailable(){
        return reserveTime.getStatus() == ReserveTimeStatus.RESERVABLE;
    }

    /**
     * check if time is cancelable
     * @return service response
     */
    ServiceResponse isTimeCancelable(){
        return reserveTime.getStatus() == ReserveTimeStatus.RESERVED
                ? serviceResponse.setResponse(true)
                : serviceResponse.setResponse(false);
    }

    /**
     * Checking is service duration less than available empty times
     * @return true if yes
     */

    private boolean isTimeDurationPassServiceDuration(){
        List<ReserveTime> oldReserveTimes = TableReserveTime
                .getReserveTimeIDsFromMidday(reserveTime.getDateID()
                        , reserveTime.getUnitID()
                        , reserveTime.getMiddayID());
        int serviceDuration = ReserveTimeUtils.getReserveTimeServiceDuration(reserveTime);
        ///Getting time list after the intended time
        List<ReserveTime> subOldReserveTimes = oldReserveTimes
                .subList(ReserveTimeUtils.getReserveTimeIndex(oldReserveTimes, reserveTime)
                        , oldReserveTimes.size());
        ReserveTime endTime = ReserveTimeUtils.getEndReservableIndexTimeFromList(subOldReserveTimes);

        // if empty time after intended time start time is more than service duration
        LocalTime time1 = reserveTime.getStartTime().plusMinutes(serviceDuration);
        LocalTime time2  = endTime.getStartTime().plusMinutes(endTime.getDuration());
        return time1.isBefore(time2) || time1.equals(time2);
    }

    /**
     * Check if selected services belongs to reserve time unit
     * @return service response
     */

    ServiceResponse isServicesBelongToReserveTimeUnit(){
        List<Integer> serviceIDs = TableService.getUnitServiceIDs(reserveTime.getUnitID());
        if (serviceIDs != null) {
            for (Integer serviceID : reserveTime.getServiceIDs()) {
                if (!serviceIDs.contains(serviceID))
                    return serviceResponse.setResponse(false)
                            .setMessage(ServerMessage.INTERNALERRORMESSAGE);
            }
        }else
            return serviceResponse.setResponse(false)
                    .setMessage(ServerMessage.INTERNALERRORMESSAGE);
        return serviceResponse.setResponse(true);
    }

}