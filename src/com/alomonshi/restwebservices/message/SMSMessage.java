package com.alomonshi.restwebservices.message;

import com.alomonshi.object.uiobjects.ClientReservedTime;
import com.alomonshi.utility.DateTimeUtility;

public class SMSMessage {

    public static String getAdminCancelTimeMessage(ClientReservedTime clientReservedTime) {
        return " باسلام، \n" +
                "متأسفانه نوبت شما در مجموعه ی " +
                clientReservedTime.getCompanyName() +
                " در واحد " +
                clientReservedTime.getUnitName() +
                "، در روز " +
                DateTimeUtility.beautifyPersianDateID(clientReservedTime.getDayID()) +
                " و در ساعت " +
                clientReservedTime.getStartTime() +
                " توسط مدیر مجموعه لغو گردید." +
                "\n" +
                "جهت اطلاع از وقت های رزرو شده خود می توانید به وب سایت الومنشی مراجعه نمایید: " +
                "\n" +
                "www.alomonshi.ir";
    }
}