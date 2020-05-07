package com.alomonshi.restwebservices.adaptors;
import com.alomonshi.utility.DateTimeUtility;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PersianDateIDAdaptor extends XmlAdapter<String, Integer> {

    @Override
    public Integer unmarshal(String date){
        try {
            return DateTimeUtility.convertPersianDateToPersianDateID(date);
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public String marshal(Integer dateID){
        try {
            return DateTimeUtility.beautifyPersianDateID(dateID);
        }catch (Exception e){
            return null;
        }
    }
}
