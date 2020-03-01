package com.alomonshi.restwebservices.message;

import com.alomonshi.object.uiobjects.ClientReservedTime;

public class SMSMessage {

    public static String getAdminCancelTimeMessage(ClientReservedTime clientReservedTime) {
        return " باسلام،" +
                "متأسفانه نوبت شما در مجموعه ی " +
                clientReservedTime.getCompanyName() +
                " در واحد " +
                clientReservedTime.getUnitName() +
                " و در ساعت " +
                clientReservedTime.getStartTime() +
                " توسط مدیر مجموعه لغو گردید.";
    }
}
