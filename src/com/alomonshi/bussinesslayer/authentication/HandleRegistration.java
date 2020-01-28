package com.alomonshi.bussinesslayer.authentication;
import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Users;
import com.alomonshi.utility.UtilityFunctions;
import com.alomonshi.utility.sendsms.SMSUtils;

public class HandleRegistration {
    private Users newUser;
    private Authentication authentication;
    public HandleRegistration(Users user){
        this.newUser = user;
        authentication = new Authentication(user);
    }

    /**
     * Generating verification code for registration
     * @return five random digits verification code
     */
    private static String generateVerificationCode(){
        return UtilityFunctions.generateFiveDigitRandomNum();
    }

    /**
     *  Generating verification  message to be send to client phone
     * @param verificationCode used to the message send to client
     * @return sent message
     */
    private static String generateVerificationMessage(String verificationCode){
        return "به وب سایت الومنشی خوش آمدید. \n" + "کد امنیتی شما: " + verificationCode;
    }

    /**
     * sending verification message to client phone
     * @param verificationCode code sent to client
     * @return true if message has been sent
     */
    private boolean sendVerificationCodeMessage(String verificationCode){
        String[] toNumbers = {newUser.getPhoneNo()};
        return SMSUtils.sendSMS(toNumbers, generateVerificationMessage(verificationCode));
    }

    /**
     * checking registered client and then sending message to client
     * @return true if message send to client
     */
    public boolean handleVerification(){
        String verificationCode = generateVerificationCode();
        if(newUser.getID() == 0) {
            newUser.setUserLevel(UserLevels.CLIENT.getValue());
            if(!TableClient.insert(newUser.setVerificationCode(Integer.parseInt(verificationCode))))
                return false;
        }else if (!TableClient.update(newUser.setVerificationCode(Integer.parseInt(verificationCode))))
            return false;
        return sendVerificationCodeMessage(verificationCode);
    }

    /**
     * checking entered verification code
     * @param verificationCode entered verification code by user
     * @return true if verification code is equal to code sent to user
     */
    public boolean checkVerificationCode(String verificationCode){
        return Integer.parseInt(verificationCode) == newUser.getVerificationCode();
    }

    /**
     * Checking token is valid or not then generating json web token
     * @return json web token
     */
    public String handleFinalRegistration(){
        newUser.setToken(Authentication.generateNewToken()).setExpirationDate(Authentication.generateExpirationDate());
        if(!TableClient.update(newUser))
            return null;
        return authentication.generateWebToken();
    }
}
