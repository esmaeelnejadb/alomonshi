package com.alomonshi.bussinesslayer.tableutils;

import com.alomonshi.datalayer.dataaccess.TableClient;

public class ClientUtils extends TableClient {
		
	public ClientUtils() { }
	
	
	public ClientUtils setUserID(int userID)
	{
		super.user_id = userID;
		return this;
	}


	public boolean isRegistered()
	{
		return getStatus() == 1 ? true : false;
	}
}
