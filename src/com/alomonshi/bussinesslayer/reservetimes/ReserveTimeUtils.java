package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.datalayer.dataaccess.TableService;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.utility.DateTimeUtility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

class ReserveTimeUtils {

    /**
     * Getting index of reserve time from list
     * @param reserveTimes list to be investigated
     * @param reserveTime reserve time to be investigated
     * @return index number
     */

    static int getReserveTimeIndex(List<ReserveTime> reserveTimes, ReserveTime reserveTime) {
        for (int i = 0; i < reserveTimes.size(); i++) {
            if(reserveTimes.get(i).getID() == reserveTime.getID())
                return i;
        }
        return 0;
    }

    /**
     * Getting service duration
     *
     * @return service duration
     */

    static int getReserveTimeServiceDuration(ReserveTime reserveTime) {
        int duration = 0;
        for (Integer serviceID : reserveTime.getServiceIDs()) {
            duration += Objects.requireNonNull(TableService.getService(serviceID,
                    DateTimeUtility.getCurrentGregorianDate())).getServiceDuration();
        }
        return duration;
    }

    /**
     * Getting last reservable or hold time from reserve time list
     * @param reserveTimes to be checked
     * @return last reservable or hold time
     */

    static ReserveTime getEndReservableIndexTimeFromList(List<ReserveTime> reserveTimes) {
        for (int i = 0; i < reserveTimes.size(); i++ ) {
            if(reserveTimes.get(i).getStatus() == ReserveTimeStatus.RESERVED
                    || reserveTimes.get(i).getStatus() == ReserveTimeStatus.CANCELED) {
                // End reservable time is one time before reserved or canceled time
                if (i > 0)
                    return reserveTimes.get(i - 1);
                else
                    return reserveTimes.get(0);
            }
        }
        return reserveTimes.get(reserveTimes.size() - 1);
    }
}