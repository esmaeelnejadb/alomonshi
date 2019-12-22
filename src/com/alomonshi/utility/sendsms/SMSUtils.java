package com.alomonshi.utility.sendsms;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tempuri.ISendServiceProxy;

import com.alomonshi.server.AlomonshiServer;
import com.microsoft.schemas._2003._10.Serialization.Arrays.holders.ArrayOflongHolder;

import javax.xml.rpc.holders.ByteArrayHolder;

public class SMSUtils {
	
	public static boolean sendSMS(String[] toNumbers , String messageContent) {
		//Sending SMS
		ISendServiceProxy ss  = new ISendServiceProxy();
		String userName = "esmaeelnejadb";
		String smsPass = "0000";
		String fromNumber = "Simcard";
		boolean isFlash = false;
		ArrayOflongHolder recId = new ArrayOflongHolder();
		ByteArrayHolder smsstatus = new ByteArrayHolder();
		
		try {
			int result = ss.sendSMS(userName, smsPass, fromNumber, toNumbers, messageContent, isFlash, recId, null);
			//if successfully sent
			if (result == 0) {
				return true;
			}else {
				Logger.getLogger(AlomonshiServer.class.getName()).log(Level.SEVERE, "SMS result: " + result);
				return false;
        	}
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}
}
