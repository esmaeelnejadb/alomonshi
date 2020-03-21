package com.alomonshi.utility.sendsms;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.configuration.ConfigurationParameter;
import org.tempuri.ISendServiceProxy;

import com.microsoft.schemas._2003._10.Serialization.Arrays.holders.ArrayOflongHolder;

import javax.xml.rpc.holders.ByteArrayHolder;

public class SMSUtils {
	
	public static boolean sendSMS(String[] toNumbers , String messageContent) {
		//Sending SMS
		ISendServiceProxy sendSMS  = new ISendServiceProxy();
		//boolean isFlash = false;
		ArrayOflongHolder recId = new ArrayOflongHolder();
		ByteArrayHolder smsStatus = new ByteArrayHolder();
		
		try {
			int result = sendSMS.sendSMS(ConfigurationParameter.smsPanelUserName,
					ConfigurationParameter.smsPanelPassword,
					ConfigurationParameter.smsPanelFromNumber,
					toNumbers,
					messageContent,
					false,
					recId,
					smsStatus);
			//if successfully sent
			if (result == 0) {
				return true;
			}else {
				Logger.getLogger("Exception").log(Level.SEVERE, "Can not sent sms with result code: " + result);
				return false;
        	}
		} catch (RemoteException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}
	}
}
