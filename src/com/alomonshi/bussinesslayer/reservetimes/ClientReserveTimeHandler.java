package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.datalayer.dataaccess.TableReserveTime;
import com.alomonshi.datalayer.dataaccess.TableReserveTimeServices;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.ReserveTimeServices;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

class ClientReserveTimeHandler {

    /**
     * Reserved a time for client
     *
     * @return true if reserved correctly
     */
//////////////////////////////////////Inserting a new Time//////////////////////////////////////////////
    synchronized static boolean setNewReserveTime(ReserveTime reserveTime) {
        int unitDuration = reserveTime.getDuration();
        int serviceDuration = ReserveTimeUtils.getReserveTimeServiceDuration(reserveTime);
        prepareReserveTimeForRegister(reserveTime);

        // Generating reserve time services object to be inserted in the related table
        List<ReserveTimeServices> reserveTimeServices  = new ArrayList<>();
        fillReserveTimeServices(reserveTimeServices, reserveTime);

        //Defining hold time to be calculated and inserted if any
        ReserveTime holdReserveTime = new ReserveTime();

        //if selected services duration is different from unit time duration
        if(serviceDuration != unitDuration) {
            prepareHoldReserveTime(holdReserveTime, reserveTime);
            List<ReserveTime> oldReserveTimes = TableReserveTime
                    .getReserveTimeIDsFromMidday(reserveTime.getDateID()
                            , reserveTime.getUnitID()
                            , reserveTime.getMiddayID());
            if (reserveTime.getID() == oldReserveTimes.get(oldReserveTimes.size()-1).getID()) {
                completeHoldReserveTime(holdReserveTime
                        , unitDuration - serviceDuration
                        , reserveTime.getStartTime().plusMinutes(serviceDuration));
                ////////////////// Do reserve tasks ////////////////////
                return TableReserveTime.updateReserveTime(reserveTime)
                        && TableReserveTime.insertReserveTime(holdReserveTime)
                        && TableReserveTimeServices.insertList(reserveTimeServices);

            }else {
                //Getting times after intended reserve times
                List<ReserveTime> subOldReserveTimes = oldReserveTimes
                        .subList(ReserveTimeUtils.getReserveTimeIndex(oldReserveTimes, reserveTime) + 1
                                , oldReserveTimes.size());

                //If next time of intended reserve time not to be reserved time canceled time
                if (subOldReserveTimes.get(0).getStatus()
                        == ReserveTimeStatus.RESERVABLE
                        || subOldReserveTimes.get(0).getStatus()
                        == ReserveTimeStatus.HOLD) {
                    // Getting the end time which until it reserve times has been renewed.
                    ReserveTime endReserveTime = ReserveTimeUtils.getEndReservableIndexTimeFromList(subOldReserveTimes);
                    List<ReserveTime> shouldBeDeletedTimes = subOldReserveTimes
                            .subList(0, ReserveTimeUtils
                                    .getReserveTimeIndex(subOldReserveTimes, endReserveTime) + 1);
                    LocalTime startTime = reserveTime.getStartTime().plusMinutes(serviceDuration);

                    //Getting new reserve time from start time of
                    // reserved time to the next reserved time or
                    // last time of same day and mid day
                    List<ReserveTime> newReserveTimes = ReserveTimeGenerator
                            .generateMiddayReserveTimes(reserveTime.getUnitID()
                                    , reserveTime.getDateID()
                                    , reserveTime.getDateID()
                                    , reserveTime.getMiddayID()
                                    , startTime
                                    , endReserveTime.getStartTime().plusMinutes(endReserveTime.getDuration()));

                    ////////////////// Do reserve tasks ////////////////////
                    return TableReserveTime.updateReserveTime(reserveTime)
                            && TableReserveTime.insertReserveTimeList(newReserveTimes)
                            && TableReserveTime.deleteReserveTimeList(shouldBeDeletedTimes)
                            && TableReserveTimeServices.insertList(reserveTimeServices);
                }else { //If next time of intended reserve time be reserved time or canceled time
                    holdReserveTime.setDuration(unitDuration - serviceDuration);
                    holdReserveTime.setStartTime(reserveTime.getStartTime().plusMinutes(serviceDuration));
                    ////////////////// Do reserve tasks ////////////////////
                    return TableReserveTime.updateReserveTime(reserveTime)
                            && TableReserveTime.insertReserveTime(holdReserveTime)
                            && TableReserveTimeServices.insertList(reserveTimeServices);
                }
            }
        }else {
            ////////////////// Do reserve tasks ////////////////////
            return TableReserveTime.updateReserveTime(reserveTime)
                    && TableReserveTimeServices.insertList(reserveTimeServices);
        }
    }

    /**
     * Cancel reserve time
     * @param reserveTime to be canceled
     * @return true if canceled truly
     */

    //////////////////////////////////////Canceling a Time//////////////////////////////////////////////
    static synchronized boolean cancelClientReserveTime(ReserveTime reserveTime)
    {
        List<ReserveTime> newReserveTimes, shouldBeDeletedTimes;

        //Getting all the times in that midday and day
        List<ReserveTime> oldReserveTimes = TableReserveTime
                .getReserveTimeIDsFromMidday(
                        reserveTime.getDateID()
                        , reserveTime.getUnitID()
                        , reserveTime.getMiddayID());
        int reserveTimeIndex = ReserveTimeUtils.getReserveTimeIndex(oldReserveTimes, reserveTime);
        //Change reserved time status to reservable to
        oldReserveTimes.get(reserveTimeIndex).setStatus(ReserveTimeStatus.RESERVABLE);
        ReserveTime endReserveTime = ReserveTimeUtils.getEndReservableIndexTimeFromList(
                oldReserveTimes.subList(reserveTimeIndex, oldReserveTimes.size())
        );
        int endReserveTimeIndex = ReserveTimeUtils.getReserveTimeIndex(oldReserveTimes, endReserveTime);

        if(ReserveTimeUtils.getReserveTimeIndex(oldReserveTimes, reserveTime) != 0
        && oldReserveTimes.get(reserveTimeIndex - 1).getStatus() == ReserveTimeStatus.HOLD) {

            newReserveTimes = ReserveTimeGenerator.generateMiddayReserveTimes(
                    reserveTime.getUnitID(),
                    reserveTime.getDateID(),
                    reserveTime.getDateID(),
                    reserveTime.getMiddayID(),
                    oldReserveTimes.get(reserveTimeIndex - 1).getStartTime(),
                    endReserveTime.getStartTime().plusMinutes(endReserveTime.getDuration()));
            shouldBeDeletedTimes = oldReserveTimes
                    .subList(reserveTimeIndex - 1, endReserveTimeIndex + 1);
        }else {
            newReserveTimes = ReserveTimeGenerator.generateMiddayReserveTimes(
                    reserveTime.getUnitID(),
                    reserveTime.getDateID(),
                    reserveTime.getDateID(),
                    reserveTime.getMiddayID(),
                    reserveTime.getStartTime(),
                    endReserveTime.getStartTime().plusMinutes(endReserveTime.getDuration()));
            shouldBeDeletedTimes = oldReserveTimes.subList(reserveTimeIndex, endReserveTimeIndex + 1);
        }

        ////////////////////////////////Do tasks////////////////////////////////////
        //Delete old times
        //delete old reserve time services
        //Insert new times
        return  TableReserveTime.deleteReserveTimeList(shouldBeDeletedTimes) &&
                TableReserveTime.insertReserveTimeList(newReserveTimes) &&
                TableReserveTimeServices.deleteReserveTimeServices(reserveTime);
    }

    private static void prepareReserveTimeForRegister(ReserveTime reserveTime) {
        reserveTime.setStatus(ReserveTimeStatus.RESERVED);
        reserveTime.setResCodeID(Integer.toString(reserveTime.getID()));
        reserveTime.setDuration(ReserveTimeUtils.getReserveTimeServiceDuration(reserveTime));
    }

    private static void prepareHoldReserveTime(ReserveTime holdReserveTime, ReserveTime reserveTime) {
        holdReserveTime.setStatus(ReserveTimeStatus.HOLD);
        holdReserveTime.setUnitID(reserveTime.getUnitID());
        holdReserveTime.setDateID(reserveTime.getDateID());
        holdReserveTime.setMiddayID(reserveTime.getMiddayID());
    }

    private static void completeHoldReserveTime (ReserveTime holdReserveTime, int duration, LocalTime startTime) {
        holdReserveTime.setDuration(duration);
        holdReserveTime.setStartTime(startTime);
    }

    private static void fillReserveTimeServices(List<ReserveTimeServices> reserveTimeServices, ReserveTime reserveTime) {
        for (Integer serviceID : reserveTime.getServiceIDs()) {
            ReserveTimeServices reserveTimeService = new ReserveTimeServices();
            reserveTimeService.setActive(true);
            reserveTimeService.setUnitID(reserveTime.getUnitID());
            reserveTimeService.setServiceID(serviceID);
            reserveTimeService.setReserveTimeID(reserveTime.getID());
            reserveTimeService.setClientID(reserveTime.getClientID());
            reserveTimeServices.add(reserveTimeService);
        }
    }

}
