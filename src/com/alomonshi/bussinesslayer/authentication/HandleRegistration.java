package com.alomonshi.bussinesslayer.authentication;

import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.entity.Users;
import com.alomonshi.utility.UtilityFunctions;
import com.alomonshi.utility.sendsms.SMSUtils;

public class HandleRegistration {
    private Users newUser;
    public HandleRegistration(Users user){
        this.newUser = user;
    }

    private static String generateVerificationCode(){
        return UtilityFunctions.generateFiveDigitRandomNum();
    }

    private static String generateVerificationMessage(String verificationCode){
        return "به وب سایت الومنشی خوش آمدید. \n" + "کد امنیتی شما: " + verificationCode;
    }

    private boolean sendVerificationCodeMessage(String verificationCode){
        String[] toNumbers = {newUser.getPhoneNo()};
        return SMSUtils.sendSMS(toNumbers, generateVerificationMessage(verificationCode));
    }

    public boolean handleRegistration(){
        String verificationCode = generateVerificationCode();
        if(newUser.getUserID() == 0) {
            if(!TableClient.insert(newUser.setVerificationCode(Integer.parseInt(verificationCode))))
                return false;
        }else if (!TableClient.update(newUser.setVerificationCode(Integer.parseInt(verificationCode))))
            return false;
        return sendVerificationCodeMessage(verificationCode);
    }
}
