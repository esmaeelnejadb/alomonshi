package com.alomonshi.utility;
import com.alomonshi.datalayer.dataaccess.TableClient;

import java.security.SecureRandom;
public class UtilityFunctions {
	
	public static String convertToEnglishDigits(String value)
	{
	     String newValue = value.replace("١", "1").replace("٢", "2").replace("٣", "3").replace("٤", "4").replace("٥", "5")
	             .replace("٦", "6").replace("٧", "7").replace("٨", "8").replace("٩", "9").replace("٠", "0")
	             .replace("۱", "1").replace("۲", "2").replace("۳", "3").replace("۴", "4").replace("۵", "5")
	             .replace("۶", "6").replace("۷", "7").replace("۸", "8").replace("۹", "9").replace("۰", "0");

	     return newValue;
	}
	
	public static int phoneEncryption(String phoneNo)
	{
		return (int)Long.parseLong(phoneNo);
	}

	public static int cityNameToNumber(String name) {
		
		switch (name) {
		case "تهران" :
			return 1;
			default :
				return 0;
		}			
	}
	
	public static int districtNameToNumber(String name) {
		switch (name) {
		case "منطقه ۱" :

			return 1;
		case "منطقه ۲" :
			return 2;
		case "منطقه ۳" :
			return 3;
		case "منطقه ۴" :
			return 4;
		case "منطقه ۵" :
			return 5;
		case "منطقه ۶" :
			return 6;
		case "منطقه ۷" :
			return 7;
		case "منطقه ۸" :
			return 8;
		case "منطقه ۹" :
			return 9;
		case "منطقه ۱۰" :
			return 10;
		case "منطقه ۱۱" :
			return 11;
		case "منطقه ۱۲"	 :
			return 12;
		case "منطقه ۱۳" :
			return 13;
		case "منطقه ۱۴" :
			return 14;
		case "منطقه ۱۵" :
			return 15;
		case "منطقه ۱۶" :
			return 16;
		case "منطقه ۱۷" :
			return 17;			
		case "منطقه ۱۸" :
			return 18;
		case "منطقه ۱۹" :
			return 19;
		case "منطقه ۲۰" :
			return 20;
		case "منطقه ۲۱" :
			return 21;
		case "منطقه ۲۲" :
			return 22;			
			default :
				return 0;
		}
	}
	public static String generateFiveDigitRandomNum() {
		//Generating Security Code
		SecureRandom random = new SecureRandom();
		int num = random.nextInt(100000);
		return String.format("%05d", num);
	}
}
