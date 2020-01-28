package com.alomonshi.bussinesslayer.tableutils;

import java.util.ArrayList;

import java.util.List;

import com.alomonshi.datalayer.dataaccess.TableAdminUnit;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.tableobjects.Units;


public class AdminUnitUtils extends TableAdminUnit {
	
	public AdminUnitUtils()
	{}
	
	public List<Units> getAdminUnitForJson()
	{
		List<Integer> adminUnitIDs = getAdminUnitIDs();
		List<Units> units = new ArrayList<>();
		for (Integer id : adminUnitIDs)
		{
			units.add(TableUnit.getUnit(id));
		}
		return units;
	}
	

	public boolean deleteAdminUnit(int unitID)
	{
		return deleteUnit(unitID);
	}
	
	public boolean isEmpty()
	{
		return getAdminUnitIDs().isEmpty();
	}

}
