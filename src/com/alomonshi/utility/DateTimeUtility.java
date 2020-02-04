package com.alomonshi.utility;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTimeUtility {
    public static LocalTime StringtoLocalTime(String localTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(localTime, dateTimeFormatter);
    }
}
