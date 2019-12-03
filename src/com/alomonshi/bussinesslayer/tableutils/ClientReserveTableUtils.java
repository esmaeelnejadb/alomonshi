package com.alomonshi.bussinesslayer.tableutils;

import com.alomonshi.datalayer.dataaccess.TableClientReserve;

public class ClientReserveTableUtils extends TableClientReserve {
	
	public boolean cancelReserveData(String resCodeID)
	{
		int ID = getID(resCodeID);		
		return setReserveCode(null, ID) && setReserveStatus(ID, 2);		
	}	
}
