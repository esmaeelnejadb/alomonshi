package com.alomonshi.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.regex.Pattern;

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
    public static int getCurrentPersianDateID(){
        Calendar cal = Calendar.getInstance();
        JalaliCalendar jalaliDate = new JalaliCalendar(cal.getTime());
        return Integer.parseInt(jalaliDate.getYear() +
                "" +
                (Integer.toString(jalaliDate.getMonth()).length() == 2
                        ?
                        jalaliDate.getMonth()
                        :
                        "0" + jalaliDate.getMonth()) +
                "" +
                (Integer.toString(jalaliDate.getDay()).length() == 2
                        ?
                        jalaliDate.getDay()
                        :
                        "0" + jalaliDate.getDay()));
    }

    /**
     * Getting gregorian date time from reserve time information
     * @param dayID reserve time day id
     * @param time time of reserve time
     * @return local date time
     */
    public static LocalDateTime getGregorianReservedTimeDatetime(int dayID, LocalTime time) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(getYearFromDateID(dayID), getMonthFromDateID(dayID), getDayFromDateID(dayID));
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
        JalaliCalendar jalaliCalendar = new JalaliCalendar(getYearFromDateID(dayID), getMonthFromDateID(dayID), getDayFromDateID(dayID));
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
        JalaliCalendar jalaliCalendar = new JalaliCalendar(getYearFromDateID(dayID), getMonthFromDateID(dayID), getDayFromDateID(dayID));
        return DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(jalaliCalendar.toGregorian()
                        .toZonedDateTime()
                        .toLocalDate());
    }


    /**
     * convert gregorian to persian date
     * @param localDate to be converted
     * @return converted date
     */
    public static String convertGregorianToPersianDate(LocalDate localDate) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(localDate);
        return jalaliCalendar.getYear() +
                "/" +
                (Integer.toString(jalaliCalendar.getMonth()).length() == 2
                        ?
                        jalaliCalendar.getMonth()
                        :
                        "0" + jalaliCalendar.getMonth()) +
                "/" +
                (Integer.toString(jalaliCalendar.getDay()).length() == 2
                        ?
                        jalaliCalendar.getDay()
                        :
                        "0" + jalaliCalendar.getDay());
    }

    /**
     * Convert gregorian local date time to persian local date time
     * @param localDateTime to be converted
     * @return converted date time
     */
    public static String convertGregorianToPersianDateTime(LocalDateTime localDateTime) {
        JalaliCalendar jalaliCalendar = new JalaliCalendar(localDateTime.toLocalDate());
        return jalaliCalendar.getYear() +
                "/" +
                jalaliCalendar.getMonth() +
                "/" +
                jalaliCalendar.getDay() +
                " " +
                localDateTime.toLocalTime();
    }

    /**
     * Converting persian date format to date id
     * @param date to be converted
     * @return converted date id
     */
    public static int convertPersianDateToPersianDateID (String date) {
        String[] splitDate = date.split(Pattern.quote("/"));
        return Integer.parseInt(splitDate[0] + splitDate[1] + splitDate[2]);
    }

    /**
     * Getting current Gregorian date as string
     * @return current Gregorian date
     */
    public static String getCurrentGregorianDate () {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
    }

    public static int getYearFromDateID(int dayID) {
        return Integer.parseInt(Integer.toString(dayID).substring(0,4));
    }

    public static int getMonthFromDateID(int dayID) {
        return Integer.parseInt(Integer.toString(dayID).substring(4,6));
    }

    private static int getDayFromDateID(int dayID) {
        return Integer.parseInt(Integer.toString(dayID).substring(6,8));
    }
}
