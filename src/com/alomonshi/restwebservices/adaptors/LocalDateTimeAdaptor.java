package com.alomonshi.restwebservices.adaptors;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LocalDateTimeAdaptor extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String timString) {
        try{
            return LocalDateTime.parse(timString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public String marshal(LocalDateTime localDateTime) {
        try {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(localDateTime);
        }catch (Exception e){
            return null;
        }
    }
}
