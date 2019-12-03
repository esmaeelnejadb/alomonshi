package com.alomonshi.bussinesslayer.tableutils;

import com.alomonshi.datalayer.dataaccess.TableAdmin;

public class AdminUtils extends TableAdmin {
	
	public static boolean isRegistered(int userID)
	{
		return getStatus(userID) == 1 || getStatus(userID) == 2 ? true : false;
	}
	
	public static boolean isManager(int userID)
	{
		return getStatus(userID) == 1 ? true : false;
	}
	
	public static boolean isAdmin(int userID)
	{
		return getStatus(userID) == 2 ? true : false;
	}
	
	public static boolean setManager(int userID)
	{
		return setAdminState(1, userID);
	}
	public static boolean setAdmin(int userID)
	{
		return setAdminState(2, userID);
	}
		
	
}
