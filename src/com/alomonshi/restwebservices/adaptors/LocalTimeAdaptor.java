package com.alomonshi.restwebservices.adaptors;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class LocalTimeAdaptor extends XmlAdapter<String, LocalTime> {
    @Override
    public LocalTime unmarshal(String timString) {
        try{
            return LocalTime.parse(timString, DateTimeFormatter.ISO_TIME);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public String marshal(LocalTime localTime) {
        try {
            return DateTimeFormatter.ofPattern("HH:mm").format(localTime);
        }catch (Exception e){
            return null;
        }
    }
}
