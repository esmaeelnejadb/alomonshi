package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.datalayer.dataaccess.TableCalendar;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.CalendarDate;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.ReserveTimeForm;
import com.alomonshi.utility.DateTimeUtility;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ReserveTimeGenerator {

    private ReserveTimeForm reserveTimeForm;

    ReserveTimeGenerator setReserveTimeForm(ReserveTimeForm reserveTimeForm) {
        this.reserveTimeForm = reserveTimeForm;
        return this;
    }

    /**
     * Generating morning and afternoon reserve time
     * @return List of generated reserve time
     */

    List<ReserveTime> generateAllDayReserveTimes(){
        List<ReserveTime> reserveTimes = new ArrayList<>();
        if (primaryCheck()){
            if (checkMorningTime())
                reserveTimes.addAll(
                        generateMiddayReserveTimes(reserveTimeForm.getUnitID(),
                                reserveTimeForm.getStartDate(),
                                reserveTimeForm.getEndDate(),
                                MiddayID.MORNING.getValue(),
                                reserveTimeForm.getMorningStartTime(),
                                reserveTimeForm.getMorningEndTime()));
            if (checkAfternoonTime())
                reserveTimes.addAll(
                        generateMiddayReserveTimes(reserveTimeForm.getUnitID(),
                                reserveTimeForm.getStartDate(),
                                reserveTimeForm.getEndDate(),
                                MiddayID.AFTERNOON.getValue(),
                                reserveTimeForm.getAfternoonStartTime(),
                                reserveTimeForm.getAfternoonEndTime()));
        }
        return reserveTimes;
    }

    /**
     * Generate reserve times of a unit
     * @param unitID intended unit id
     * @param startDay start date of devoting reserve times
     * @param endDay end date of devoting reserve times
     * @param middayID middle date of reserve times
     * @param startTime start time of reserve times
     * @param endTime end time of reserve times
     * @return list of generated reserve times to be inserted in database
     */

    static List<ReserveTime> generateMiddayReserveTimes(int unitID, int startDay, int endDay, int middayID, LocalTime startTime, LocalTime endTime)
    {
        // All duration including service, unit or reserve time duration
        // are in type of int and are considered as minutes
        List<ReserveTime> allReserveTimes = new ArrayList<>();
        List<CalendarDate> dates = TableCalendar.getDates(startDay, endDay);
        LocalTime startTimeOfButton = startTime;
        int duration = Objects.requireNonNull(TableUnit.getUnit(unitID)).getUnitDuration();
        for(CalendarDate date : dates)
        {
            ReserveTime holdReserveTime = new ReserveTime();
            while(startTimeOfButton.isBefore(endTime.minusMinutes(duration))
                    || startTimeOfButton ==  endTime.minusMinutes(duration)) {
                ReserveTime reservetime = new ReserveTime();
                reservetime.setUnitID(unitID);
                reservetime.setStartTime(startTimeOfButton);
                reservetime.setDuration(duration);
                reservetime.setStatus(ReserveTimeStatus.RESERVABLE);
                reservetime.setMiddayID(middayID);
                reservetime.setDateID(date.getID());
                startTimeOfButton = startTimeOfButton.plusMinutes(duration);
                allReserveTimes.add(reservetime);
            }

            // Detecting and hold reminded time to be modified later in reserving time
            if (startTimeOfButton.isAfter(endTime.minusMinutes(duration))
                && startTimeOfButton.isBefore(endTime)) {
                holdReserveTime.setStatus(ReserveTimeStatus.HOLD);
                holdReserveTime.setDuration(DateTimeUtility.getTimeMinutes(endTime)
                                - DateTimeUtility.getTimeMinutes(startTimeOfButton));
                holdReserveTime.setStartTime(startTimeOfButton);
                holdReserveTime.setMiddayID(middayID);
                holdReserveTime.setUnitID(unitID);
                holdReserveTime.setDateID(date.getID());
                allReserveTimes.add(holdReserveTime);
            }
            startTimeOfButton = startTime;
        }
        return allReserveTimes;
    }

    /**
     * Primary check of input generating time data
     * @return true if all input data be entered correctly
     */

    private boolean primaryCheck(){
        return reserveTimeForm.getUnitID() != 0
                && reserveTimeForm.getEndDate() >= reserveTimeForm.getStartDate();
    }

    /**
     * checking morning input time
     * @return true if all input data be entered correctly
     */

    private boolean checkMorningTime(){
        if (reserveTimeForm.getMorningStartTime() != null
                && reserveTimeForm.getMorningEndTime() != null)
            return reserveTimeForm.getMorningEndTime().isAfter(reserveTimeForm.getMorningStartTime());
        return false;
    }

    /**
     * checking afternoon input time
     * @return true if all input data be entered correctly
     */

    private boolean checkAfternoonTime(){
        if (reserveTimeForm.getAfternoonStartTime() != null
                && reserveTimeForm.getAfternoonEndTime() != null)
            return reserveTimeForm.getAfternoonEndTime().isAfter(reserveTimeForm.getAfternoonStartTime());
        return false;
    }
}