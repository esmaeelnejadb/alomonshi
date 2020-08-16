package com.alomonshi.restwebservices.message;

import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.object.uiobjects.ClientReservedTime;
import com.alomonshi.utility.DateTimeUtility;

public class SMSMessage {

    /**
     * admin cancel client time sms
     * @param clientReservedTime canceled time information
     * @return message
     */
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
                "'alomonshi.ir/panel/ProfileInformation'";
    }

    /**
     * Client cancel reserve time sms
     * @param clientReservedTime canceled time sms
     * @return message
     */
    public static String getClientCancelTimeMessage(ClientReservedTime clientReservedTime) {
        return " باسلام، \n" +
                "کاربر گرامی نوبت شما در مجموعه ی " +
                clientReservedTime.getCompanyName() +
                " در واحد " +
                clientReservedTime.getUnitName() +
                "، در روز " +
                DateTimeUtility.beautifyPersianDateID(clientReservedTime.getDayID()) +
                " و در ساعت " +
                clientReservedTime.getStartTime() +
                " لغو گردید." +
                "\n" +
                "جهت اطلاع از وقت های رزرو شده خود می توانید به وب سایت الومنشی مراجعه نمایید: " +
                "\n" +
                "'alomonshi.ir/panel/ProfileInformation'";
    }

    /**
     * Getting password retrieve message
     * @param user to be got password
     * @return message
     */
    public static String getPasswordRetrieveMessage(Users user) {
        return " باسلام، \n" +
                "پسورود شما: \n" +
                user.getPassword() +
                "\n" +
                "جهت ورود به سایت الومنشی به آدرس 'alomonshi.ir/login' مراجعه فرمایید. \n" +
                "";
    }
}