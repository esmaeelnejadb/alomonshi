package com.alomonshi.bussinesslayer.calendar;

import com.alomonshi.datalayer.dataaccess.TableCalendar;
import com.alomonshi.object.uiobjects.reservetimecalendar.ReserveTimesOfMonth;
import com.alomonshi.utility.DateTimeUtility;

import java.time.LocalDate;
import java.util.List;

public class CalendarService {

    /**
     * Getting reservetime number in days of month
     * @param unitID intended unit
     * @return list of calendar dates
     */
    public static List<ReserveTimesOfMonth> getReserveTimesDaysofMonth(int unitID) {
        int currentDateID = DateTimeUtility.getCurrentPersianDateID();
        String currentMonth =
                Integer.toString(DateTimeUtility.getMonthFromDateID(currentDateID)).length() == 2
                ?
                Integer.toString(DateTimeUtility.getMonthFromDateID(currentDateID))
                :
                "0" + DateTimeUtility.getMonthFromDateID(currentDateID);
        int fromDateID = //First date of current month
                Integer.parseInt(DateTimeUtility.getYearFromDateID(currentDateID) +
                        currentMonth +
                        "01");
        String toDate = DateTimeUtility.convertGregorianToPersianDate(LocalDate.now().plusYears(1));
        int toDateID = DateTimeUtility.convertPersianDateToPersianDateID(toDate);
        return TableCalendar.getMonthDays(unitID, fromDateID, toDateID, currentDateID);
    }
}
