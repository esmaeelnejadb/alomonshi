package com.alomonshi.bussinesslayer.reservetimes;

import com.alomonshi.datalayer.dataaccess.TableCalendar;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.CalendarDate;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.GenerateReserveTimeForm;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReserveTimeGenerator {

    private GenerateReserveTimeForm generateReserveTimeForm;

    public ReserveTimeGenerator(GenerateReserveTimeForm generateReserveTimeForm){
        this.generateReserveTimeForm = generateReserveTimeForm;
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
                        generateMiddayReserveTimes(generateReserveTimeForm.getUnitID(),
                                generateReserveTimeForm.getStartDate(),
                                generateReserveTimeForm.getEndDate(),
                                MiddayID.MORNING.getValue(),
                                generateReserveTimeForm.getStartTime1(),
                                generateReserveTimeForm.getEndTime1()));
            if (checkAfternoonTime())
                reserveTimes.addAll(
                        generateMiddayReserveTimes(generateReserveTimeForm.getUnitID(),
                                generateReserveTimeForm.getStartDate(),
                                generateReserveTimeForm.getEndDate(),
                                MiddayID.AFTERNOON.getValue(),
                                generateReserveTimeForm.getStartTime2(),
                                generateReserveTimeForm.getEndTime2()));
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

    private static List<ReserveTime> generateMiddayReserveTimes(int unitID, int startDay, int endDay, int middayID, LocalTime startTime, LocalTime endTime)
    {
        // All duration including service, unit or reserve time duration
        // are in type of int and are considered as minutes
        List<ReserveTime> allReserveTimes = new ArrayList<>();
        List<CalendarDate> dates = TableCalendar.getDates(startDay, endDay);
        LocalTime startTimeOfButton = startTime;
        int duration = Objects.requireNonNull(TableUnit.getUnit(unitID)).getUnitStepTime();
        for(CalendarDate date : dates)
        {
            while(startTimeOfButton.isBefore(endTime.minusMinutes(duration)))
            {
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
            startTimeOfButton = startTime;
        }
        return allReserveTimes;
    }

    /**
     * Primary check of input generating time data
     * @return true if all input data be entered correctly
     */

    private boolean primaryCheck(){
        return generateReserveTimeForm.getUnitID() != 0
                && generateReserveTimeForm.getEndDate() > generateReserveTimeForm.getStartDate();
    }

    /**
     * checking morning input time
     * @return true if all input data be entered correctly
     */

    private boolean checkMorningTime(){
        if (generateReserveTimeForm.getStartTime1() != null
                && generateReserveTimeForm.getEndTime1() != null)
            return generateReserveTimeForm.getEndTime1().isAfter(generateReserveTimeForm.getStartTime1());
        return false;
    }

    /**
     * checking afternoon input time
     * @return true if all input data be entered correctly
     */

    private boolean checkAfternoonTime(){
        if (generateReserveTimeForm.getStartTime2() != null
                && generateReserveTimeForm.getEndTime2() != null)
            return generateReserveTimeForm.getEndTime2().isAfter(generateReserveTimeForm.getStartTime2());
        return false;
    }
}