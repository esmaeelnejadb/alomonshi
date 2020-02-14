package com.alomonshi.utility;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtility {
    public static LocalTime StringtoLocalTime(String localTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(localTime, dateTimeFormatter);
    }

    public static int getTimeMinutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }
}
