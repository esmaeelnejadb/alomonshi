package com.alomonshi.bussinesslayer.tableutils;
import java.util.List;
import java.util.Objects;
import com.alomonshi.datalayer.dataaccess.TableService;
public class ServiceUtils extends TableService {
	public static boolean isEmpty(int unitID)
	{
		return Objects.requireNonNull(getUnitServices(unitID)).isEmpty();
	}
	
	public static boolean isExistedClientServices(List<Integer> serviceIDs, int unitID)
	{
		List<Integer> unitServices = getUnitServicesIDs(unitID);
		for (int i = 0 ; i<serviceIDs.size(); i++)
		{
			if(!unitServices.contains(serviceIDs.get(i)))
				return false;
		}
		return true;
	}
	
	public static String[] getServicesNameFromIDs(List<Integer> serviceIDs)
	{
		String[] serviceNames = new String[serviceIDs.size()];
		for(int i = 0; i< serviceIDs.size(); i++)
			serviceNames[i] = TableService.getName(serviceIDs.get(i));
		return serviceNames;
	}
}
