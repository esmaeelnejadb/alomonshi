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

    public static String getPersianCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        JalaliCalendar jalaliDate = new JalaliCalendar(cal.getTime());
        return jalaliDate.toString() + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    public static String getCurrentPersianDate(){
        Calendar cal = Calendar.getInstance();
        JalaliCalendar jalaliDate = new JalaliCalendar(cal.getTime());
        return jalaliDate.getYear() + "" + jalaliDate.getMonth() + "" + jalaliDate.getDay();
    }

    public static LocalDateTime getGregorianReservedTimeDatetime(ClientReservedTime clientReservedTime) {
        int year = Integer.parseInt(Integer.toString(clientReservedTime.getDayID()).substring(0,4));
        int month = Integer.parseInt(Integer.toString(clientReservedTime.getDayID()).substring(4,6));
        int day = Integer.parseInt(Integer.toString(clientReservedTime.getDayID()).substring(6,8));
        JalaliCalendar jalaliCalendar = new JalaliCalendar(year, month, day);
        String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(jalaliCalendar.toGregorian()
                        .toZonedDateTime()
                        .toLocalDate()) + " " + clientReservedTime.getStartTime();
        return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
