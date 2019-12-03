package com.alomonshi.bussinesslayer.tableutils;
import com.alomonshi.datalayer.dataaccess.TableUnit;
public class UnitUtils extends TableUnit {
	public static boolean isExisted(int compID, int unitID)
	{
		return getUnitIDs(compID).contains(unitID) ? true : false;
	}
	public static boolean isRepeatedName(String Name, int compID)
	{
		return getUnitNames(compID).contains(Name) ? true : false;
	}
}
