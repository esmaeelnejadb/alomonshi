package com.alomonshi.restwebservices.adaptors;

import com.alomonshi.utility.DateTimeUtility;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;


public class LocalDateAdaptor extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String dateString) {
        try{
            return DateTimeUtility.convertPersianToGregorianDate(Integer.parseInt(dateString));
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public String marshal(LocalDate localDate) {
        try {
            return DateTimeUtility.convertGregorianToPersianDate(localDate);
        }catch (Exception e){
            return null;
        }
    }
}
