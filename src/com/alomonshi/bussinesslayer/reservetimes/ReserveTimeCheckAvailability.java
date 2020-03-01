package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.restwebservices.message.ServerMessage;

import java.time.LocalTime;
import java.util.List;

/**
 * Check reserve time availability for reservation process
 */

class ReserveTimeCheckAvailability {
    private ReserveTime reserveTime;
    private ServiceResponse serviceResponse;

    ReserveTimeCheckAvailability(ReserveTime reserveTime, ServiceResponse serviceResponse){
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
        return time1.isBefore(time2) || time1 == time2;
    }
}