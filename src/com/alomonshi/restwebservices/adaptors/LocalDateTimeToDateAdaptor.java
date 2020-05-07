package com.alomonshi.restwebservices.adaptors;

import com.alomonshi.utility.DateTimeUtility;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

public class LocalDateTimeToDateAdaptor extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String v){
        return null;
    }

    @Override
    public String marshal(LocalDateTime localDateTime){
        try {
            return DateTimeUtility.convertGregorianToPersianDate(localDateTime.toLocalDate());
        }catch (Exception e){
            return null;
        }
    }
}
