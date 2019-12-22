package com.alomonshi.bussinesslayer.tableutils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alomonshi.datalayer.dataaccess.TableClient;
import com.alomonshi.datalayer.dataaccess.TableReserveTimeServices;
import com.alomonshi.object.entity.Users;

public class RestimeServiceUtils extends TableReserveTimeServices {
	
	public Map<Users,Integer> getUnitClientListInUnits(List<Integer> unitIDs) {
		List<Integer> clientIDs = new ArrayList<Integer>();
		Set<Integer> hs = new LinkedHashSet<Integer>();
		for(int i = 0; i < unitIDs.size(); i++)
			hs.addAll(getClientInAUnit(unitIDs.get(i)));
		clientIDs.addAll(hs);
		Map<Users,Integer> clientLists = new LinkedHashMap<Users,Integer>();
		for (int i = 0; i < clientIDs.size(); i++)
			clientLists.put(TableClient.getUser(clientIDs.get(i)),getClientReservedTimesInUnits(unitIDs, clientIDs.get(i)).size());
		return clientLists;
	}
	
	public Map<Users,Integer> getUnitClientListInServices(List<Integer> serviceIDs) {
		List<Integer> clientIDs = new ArrayList<Integer>();
		Set<Integer> hs = new LinkedHashSet<Integer>();
		for(int i = 0; i < serviceIDs.size(); i++)
			hs.addAll(getClientInAService(serviceIDs.get(i)));
		clientIDs.addAll(hs);
		Map<Users,Integer> clientLists = new LinkedHashMap<Users,Integer>();
		for (int i = 0; i < clientIDs.size(); i++)
			clientLists.put(TableClient.getUser(clientIDs.get(i)),getClientReservedTimesInServices(serviceIDs, clientIDs.get(i)).size());
		return clientLists;	
	}
	
	public List<Integer> getClientReservedTimesInUnits(List<Integer> unitIDs, int clientID) {
		List<Integer> rescodeIDs = new ArrayList<Integer>();
		Set<Integer> hs = new LinkedHashSet<Integer>();
		for(int i = 0; i < unitIDs.size(); i++)
			hs.addAll(getClientReservedTimesInAUnit(unitIDs.get(i), clientID));
		rescodeIDs.addAll(hs);
		return rescodeIDs;
	}
	
	
	public List<Integer> getClientReservedTimesInServices(List<Integer> serviceIDs, int clientID) {
		List<Integer> rescodeIDs = new ArrayList<Integer>();
		Set<Integer> hs = new LinkedHashSet<Integer>();
		for(int i = 0; i < serviceIDs.size(); i++)
			hs.addAll(getClientReservedTimesInAService(serviceIDs.get(i), clientID));
		rescodeIDs.addAll(hs);
		return rescodeIDs;
	}
		
}
