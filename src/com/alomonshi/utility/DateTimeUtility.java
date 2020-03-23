package com.alomonshi.utility;

import java.time.LocalDate;
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

    /**
     * Getting gregorian date time from reserve time information
     * @param dayID reserve time day id
     * @param time time of reserve time
     * @return local date time
     */
    public static LocalDateTime getGregorianReservedTimeDatetime(int dayID, LocalTime time) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(getYear(dayID), getMonth(dayID), getDay(dayID));
        String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(jalaliCalendar.toGregorian()
                        .toZonedDateTime()
                        .toLocalDate()) + " " + time;
        return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * Beautifying persian date
     * @param dateID to be beautify
     * @return beautified persian date
     */
    public static String beautifyPersianDateID(int dateID) {
        String stringDateID = Integer.toString(dateID);
        return stringDateID.substring(0,4) +
                "/" +
                stringDateID.substring(4,6) +
                "/" +
                stringDateID.substring(6,8);

    }

    /**
     * convert persian to gregorian date
     * @param dayID to be converted
     * @return converted date
     */
    public static LocalDate convertPersianToGregorianDate(int dayID) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(getYear(dayID), getMonth(dayID), getDay(dayID));
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(jalaliCalendar.toGregorian()
                        .toZonedDateTime()
                        .toLocalDate());
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * convert persian to gregorian date
     * @param dayID to be converted
     * @return converted date
     */
    public static String convertPersianToGregorianStringDate(int dayID) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(getYear(dayID), getMonth(dayID), getDay(dayID));
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(jalaliCalendar.toGregorian()
                        .toZonedDateTime()
                        .toLocalDate());
        return date;
    }


    /**
     * convert gregorian to persian date
     * @param localDate to be converted
     * @return converted date
     */
    public static String convertGregorianToPersianDate(LocalDate localDate) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(localDate);
        return jalaliCalendar.getYear() + "/" + jalaliCalendar.getMonth() + "/" + jalaliCalendar.getDay();
    }

    /**
     * Getting current Gregorian date as string
     * @return current Gregorian date
     */
    public static String getCurrentGregorianDate () {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
    }

    private static int getYear (int dayID) {
        return Integer.parseInt(Integer.toString(dayID).substring(0,4));
    }

    private static int getMonth (int dayID) {
        return Integer.parseInt(Integer.toString(dayID).substring(4,6));
    }

    private static int getDay(int dayID) {
        return Integer.parseInt(Integer.toString(dayID).substring(6,8));
    }
}
