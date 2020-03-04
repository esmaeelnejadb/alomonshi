package com.alomonshi.utility;

import com.alomonshi.object.uiobjects.ClientReservedTime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateTimeUtility {

    public static int getTimeMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }

    /**
     * Getting current persian date time
     * @return current date time
     */

    public static String getPersianCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        JalaliCalendar jalaliDate = new JalaliCalendar(cal.getTime());
        return jalaliDate.toString() + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    /**
     * Getting current persian day id
     * @return current persian day id
     */

    public static int getCurrentPersianDate(){
        Calendar cal = Calendar.getInstance();
        JalaliCalendar jalaliDate = new JalaliCalendar(cal.getTime());
        return Integer.parseInt(jalaliDate.getYear()
                + "" + jalaliDate.getMonth()
                + "" + jalaliDate.getDay());
    }

    public static LocalDateTime getGregorianReservedTimeDatetime(int dayID, LocalTime time) {
        int year = Integer.parseInt(Integer.toString(dayID).substring(0,4));
        int month = Integer.parseInt(Integer.toString(dayID).substring(4,6));
        int day = Integer.parseInt(Integer.toString(dayID).substring(6,8));
        JalaliCalendar jalaliCalendar = new JalaliCalendar(year, month, day);
        String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(jalaliCalendar.toGregorian()
                        .toZonedDateTime()
                        .toLocalDate()) + " " + time;
        return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String beautifyPersianDateID(int dateID) {
        String stringDateID = Integer.toString(dateID);
        return stringDateID.substring(0,4) +
                "/" +
                stringDateID.substring(4,6) +
                "/" +
                stringDateID.substring(6,8);

    }
}
