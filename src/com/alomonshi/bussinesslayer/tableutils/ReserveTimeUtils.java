package com.alomonshi.bussinesslayer.tableutils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alomonshi.datalayer.dataaccess.TableReserveTime;

import com.alomonshi.datalayer.dataaccess.TableService;
import com.alomonshi.datalayer.dataaccess.TableUnit;
import com.alomonshi.object.*;

public class ReserveTimeUtils extends TableReserveTime {
	
	public static List<ReserveTime> generateReserveTime(int unitID, int startDay, int endDay, int middayID, String startTime, String endTime)
	{
		List<ReserveTime> reserveTimes = new ArrayList<>();
		UnitUtils unit = new UnitUtils();
		long startTimeofButton = CalendarUtils.stringToTime(startTime);
		int stDay = startDay;
		long duration = CalendarUtils.stringToTime(TableUnit.getStepTime(unitID));
		while (stDay <= endDay)
		{
			while(startTimeofButton <= CalendarUtils.stringToTime(endTime) - duration)
			{
				
				ReserveTime reservetime = new ReserveTime();
				reservetime.setUnitID(unitID);
				reservetime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(startTimeofButton))));
				reservetime.setDuration(new Time(CalendarUtils.stringToTimeForDB(unit.getStepTime(unitID))));
				reservetime.setDateID(stDay);
				reservetime.setStatus(1);
				reservetime.setMiddayID(middayID);
				startTimeofButton += duration;		
				reserveTimes.add(reservetime);
			}
			stDay += 1;
			startTimeofButton = CalendarUtils.stringToTime(startTime);
			
		}
		return reserveTimes;
	}
	
	public static boolean insertReserveTimes(List<ReserveTime> reservetimes)
	{
		for (int i = 0 ; i < reservetimes.size() ; i++)
		{
			if(insertReserveTime(reservetimes.get(i)));
			else
				return false;
		}
		return true;
	}
	
	public static Map<String[][],Integer> getReserveTimeBottunInfo(List<ReserveTime> reservetimes)
	{ 
		List<String> morning_data=new ArrayList<String>();
		List<String> morning_name=new ArrayList<String>();
		List<String> afternoon_data=new ArrayList<String>();
		List<String> afternoon_name=new ArrayList<String>();		
		CalendarUtils dateAction = new CalendarUtils();
		int dateID = reservetimes.get(0).getDateID();		
		Map<String[][],Integer> inlineKyes =new LinkedHashMap<String[][],Integer>();
		String[][] date = {{dateAction.getDayName(dateID) + " " + dateAction.getDate(dateID)},{Integer.toString(dateID)}};
		inlineKyes.put(date, 1);
		String butName = new String();
		for (int i = 0 ;i < reservetimes.size(); i++)
		{
			if(reservetimes.get(i).getMiddayID() == 1)
			{
				morning_data.add("hour" + "*" + reservetimes.get(i).getID());
				Calendar calendar = GregorianCalendar.getInstance();						
				calendar.setTime(reservetimes.get(i).getStarttime());
				switch(reservetimes.get(i).getStatus())
				{
				case 1:
					String min = Integer.toString(calendar.get(Calendar.MINUTE)).length() == 1 ? 
							"0" + Integer.toString(calendar.get(Calendar.MINUTE)) : Integer.toString(calendar.get(Calendar.MINUTE));
					butName = calendar.get(Calendar.HOUR_OF_DAY) + " : " + min;
					break;
				case 2:
					butName = "رزرو شده";
					break;
				case 3:
					butName = "کنسل";
					break;
				}
				
				morning_name.add(butName);
			}
			else if(reservetimes.get(i).getMiddayID() == 2)
			{
				afternoon_data.add("hour" + "*" + reservetimes.get(i).getID());
				Calendar calendar = GregorianCalendar.getInstance();						
				calendar.setTime(reservetimes.get(i).getStarttime());
				switch(reservetimes.get(i).getStatus())
				{
				case 1:
					String min = Integer.toString(calendar.get(Calendar.MINUTE)).length() == 1 ? 
							"0" + Integer.toString(calendar.get(Calendar.MINUTE)) : Integer.toString(calendar.get(Calendar.MINUTE));
					butName = calendar.get(Calendar.HOUR_OF_DAY) + " : " + min;
					break;
				case 2:
					butName = "رزرو شده";
					break;
				case 3:
					butName = "کنسل";
					break;
				}
				afternoon_name.add(butName);
			}
		}
		if(!morning_data.isEmpty() && !afternoon_data.isEmpty())
		{
			String morning = "نوبت صبح ";
			String[][] morning_info = {{morning},{morning}};
			inlineKyes.put(morning_info, 1);
			String[][] morning_buttons = {morning_name.toArray(new String[0]),morning_data.toArray(new String[0])};
			inlineKyes.put(morning_buttons, 3);
			String afternoon = "نوبت عصر ";
			String[][] afternoon_info = {{afternoon},{afternoon}};
			inlineKyes.put(afternoon_info, 1);
			String[][] afternoon_buttons = {afternoon_name.toArray(new String[0]),afternoon_data.toArray(new String[0])};
			inlineKyes.put(afternoon_buttons, 3);
		}
		else if(!morning_data.isEmpty())
		{
			String morning = "نوبت صبح ";
			String[][] morning_info = {{morning},{morning}};
			inlineKyes.put(morning_info, 1);
			String[][] morning_buttons = {morning_name.toArray(new String[0]),morning_data.toArray(new String[0])};
			inlineKyes.put(morning_buttons, 3);
		}
		else if(!afternoon_data.isEmpty())
		{
			String afternoon = "نوبت عصر ";
			String[][] afternoon_info = {{afternoon},{afternoon}};
			inlineKyes.put(afternoon_info, 1);
			String[][] afternoon_buttons = {afternoon_name.toArray(new String[0]),afternoon_data.toArray(new String[0])};
			inlineKyes.put(afternoon_buttons, 3);
		}
		else
			return null;
		String[][] pre_page = {{},{}};
		inlineKyes.put(pre_page, 1);
		return inlineKyes;
	}
	
	public static Map<String[][],Integer> getAdminReserveTimeBottun(int dateID, int unitID)
	{
		return getReserveTimeBottunInfo(getAdminUnitReserveTimeInADay(dateID, unitID));
	}
	
	public static Map<String[][],Integer> getClientReserveTimeBottun(int dateID, int unitID)
	{
		return getReserveTimeBottunInfo(getClientUnitReserveTimeInADay(dateID, unitID));
	}
	

	public static boolean setClientNewReserveTime(ReserveTime reservetime)
	{
		List<Integer> serviceIDs = reservetime.getServIDs();
		int middayID = reservetime.getMiddayID();
		int ID = reservetime.getID(); 
		int dateID = reservetime.getDateID();
		int unitID = reservetime.getUnitID();
		int companyID = TableUnit.getCompanyID(unitID);
		int clientID = reservetime.getClientID();
		
		
		String resCodeID = Integer.toString(reservetime.getID());
		RestimeServiceUtils resTimeServ = new RestimeServiceUtils();
		
		long resTimeDuration = CalendarUtils.sqlTimeToLong(reservetime.getDuration()) ;
		long serviceDur = (long) 0;
		for(Integer serviceID : serviceIDs)
		{
			serviceDur += CalendarUtils.stringToTime(TableService.getTime(serviceID));
			resTimeServ.setRestimeID(ID).insertRestimeServ(dateID, serviceID, unitID, companyID, clientID);
		}
		reservetime.setStatus(2);
		reservetime.setRescodeID(resCodeID);
		setClientReserveData(ID, reservetime);
		
		if(serviceDur != resTimeDuration)
		{		
			List<Integer> oldreservetimes = getReserveTimeIDsFromMidday(dateID, unitID, middayID);
			ReserveTime holdrestime = new ReserveTime();
			
			holdrestime.setStatus(4);
			holdrestime.setClientID(clientID);
			holdrestime.setDateID(dateID);
			holdrestime.setUnitID(unitID);
			holdrestime.setMiddayID(middayID);
			long reservedtimeduration = CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(serviceDur));
			setDuration(ID, new Time(reservedtimeduration));
			
			//If the selected time is the last time of the midday
			if(reservetime.getID() == oldreservetimes.get(oldreservetimes.size()-1))
			{								
				long holdduration = resTimeDuration - serviceDur;
				long holdsttime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStarttime()) + serviceDur;
				holdrestime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
				holdrestime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
				return insertReserveTime(holdrestime);
			}
			else
			{
				List<Integer> suboldreservetimes = oldreservetimes.subList(oldreservetimes.indexOf(ID)+1, oldreservetimes.size());
				int endtimeID = 0;
				long endTime = 0;
				
				for (int i = 0 ; i < suboldreservetimes.size() ; i++)
				{
					if(getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 2
							|| getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 3)
					{
						endtimeID = suboldreservetimes.get(i);
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStarttime());
						break;
					}else
					{
						endtimeID = suboldreservetimes.get(i);
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStarttime()) + 
								CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
					}
			
				}
				List<Integer> shoulddeleteIDs = suboldreservetimes.indexOf(endtimeID) == suboldreservetimes.size()-1 && 
						( getReserveTimeFromID(endtimeID).getStatus() == 1 || getReserveTimeFromID(endtimeID).getStatus() == 4 )
						? suboldreservetimes.subList(0, suboldreservetimes.indexOf(endtimeID)+1) : suboldreservetimes.subList(0, 
								suboldreservetimes.indexOf(endtimeID));	
						if(!shoulddeleteIDs.isEmpty())
							deleteReserveTimes(shoulddeleteIDs);
						
						long startTime = CalendarUtils.sqlTimeToLong(reservetime.getStarttime()) + serviceDur;
						List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
								, CalendarUtils.timeToString(endTime));
						if ((endTime - startTime) % resTimeDuration != 0)
						{
							long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStarttime())
									+ resTimeDuration : startTime;
							long holdduration = (endTime - startTime) % resTimeDuration;
							holdrestime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));				
							holdrestime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
							newreservetimes.add(holdrestime);
						}
						insertReserveTimes(newreservetimes);	
			}
		
		}		
		return true;
	}

	public static boolean cancelClientReserveTime(int restimeID)
	{
		//ClientReserveTableUtils cltutils = new ClientReserveTableUtils();
		//ClientReserveTime cltrestime = cltutils.getClientReserveTimeWithCode(rescodeID);
		ReserveTime reservetime = getReserveTimeFromID(restimeID); 
		
		int middayID = reservetime.getMiddayID();
		int ID = reservetime.getID(); 
		int dateID = reservetime.getDateID();
		int unitID = reservetime.getUnitID();
		int clientID = reservetime.getClientID();
		
		UnitUtils unit = new UnitUtils();
		RestimeServiceUtils restimeserv = new RestimeServiceUtils();
		
		long resTimeDuration = CalendarUtils.sqlTimeToLong(reservetime.getDuration()) ;
		long unitDur = CalendarUtils.stringToTime(unit.getStepTime(unitID));
		
		ReserveTime holdResTime = new ReserveTime();
		holdResTime.setStatus(4);
		holdResTime.setClientID(clientID);
		holdResTime.setDateID(dateID);
		holdResTime.setUnitID(unitID);
		holdResTime.setMiddayID(middayID);
		
		List<Integer> oldReserveTimes = getReserveTimeIDsFromMidday(dateID, unitID, middayID);
		
		setStatus(ID, 1);
		resetClientReserveData(ID);
		restimeserv.setRestimeID(ID).deleteRestimeServ();
		
		if(oldReserveTimes.indexOf(ID) > 0)
		{
			if(getReserveTimeFromID(oldReserveTimes.get(oldReserveTimes.indexOf(ID)-1)).getStatus() != 4)
			{				
				if(unitDur != resTimeDuration)
				{		
					long reservedTimeDuration = CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(unitDur));
					setDuration(ID, new Time(reservedTimeDuration));
					
					//If the selected time is the last time of the midday
					if(reservetime.getID() == oldReserveTimes.get(oldReserveTimes.size()-1))
					{				
						if(resTimeDuration > unitDur)
						{							
							long startTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStarttime()) + unitDur;
							long endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStarttime()) + resTimeDuration;
							
							List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
									, CalendarUtils.timeToString(endTime));
							if ((endTime - startTime) % unitDur != 0)
							{
								long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStarttime())
										+ unitDur : startTime;
								long holdduration = (endTime - startTime) % unitDur;
								holdResTime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
								holdResTime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
								newreservetimes.add(holdResTime);
							}							
							insertReserveTimes(newreservetimes);
						}else
						{
							return setStatus(ID, 4);
						}
					}
					else
					{
						List<Integer> suboldreservetimes = oldReserveTimes.subList(oldReserveTimes.indexOf(ID)+1, oldReserveTimes.size());
						int endtimeID = 0;
						long endTime = 0;
						
						for (int i = 0 ; i < suboldreservetimes.size() ; i++)
						{
							if(getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 2
									|| getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 3)
							{
								endtimeID = suboldreservetimes.get(i);
								endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStarttime());
								break;
							}else
							{
								endtimeID = suboldreservetimes.get(i);
								endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStarttime()) + 
										CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
							}
					
						}
						List<Integer> shoulddeleteIDs = suboldreservetimes.indexOf(endtimeID) == suboldreservetimes.size()-1 && 
								( getReserveTimeFromID(endtimeID).getStatus() == 1 || getReserveTimeFromID(endtimeID).getStatus() == 4 )
								? suboldreservetimes.subList(0, suboldreservetimes.indexOf(endtimeID)+1) : suboldreservetimes.subList(0, 
										suboldreservetimes.indexOf(endtimeID));
								
								if(!shoulddeleteIDs.isEmpty())
									deleteReserveTimes(shoulddeleteIDs);
								
								long startTime = CalendarUtils.sqlTimeToLong(reservetime.getStarttime()) + unitDur;
								List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
										, CalendarUtils.timeToString(endTime));
								if ((endTime - startTime) % unitDur != 0)
								{
									long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStarttime())
											+ unitDur : startTime;
									long holdduration = (endTime - startTime) % unitDur;
									holdResTime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
									holdResTime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
									newreservetimes.add(holdResTime);
								}
								
								insertReserveTimes(newreservetimes);	
					}				
				}
			}else
			{
				List<Integer> suboldreservetimes = oldReserveTimes.subList(oldReserveTimes.indexOf(ID)-1, oldReserveTimes.size());
				int endtimeID = 0;
				long endTime = 0;
				
				for (int i = 0 ; i < suboldreservetimes.size() ; i++)
				{
					if(getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 2
							|| getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 3)
					{
						endtimeID = suboldreservetimes.get(i);
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStarttime());
						break;
					}else
					{
						endtimeID = suboldreservetimes.get(i);
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStarttime()) + 
								CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
					}
			
				}
				long startTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(0)).getStarttime());
				
				List<Integer> shoulddeleteIDs = suboldreservetimes.indexOf(endtimeID) == suboldreservetimes.size()-1 && 
						( getReserveTimeFromID(endtimeID).getStatus() == 1 || getReserveTimeFromID(endtimeID).getStatus() == 4 )
						? suboldreservetimes.subList(0, suboldreservetimes.indexOf(endtimeID)+1) : suboldreservetimes.subList(0, 
								suboldreservetimes.indexOf(endtimeID));	
						
						if(!shoulddeleteIDs.isEmpty())
							deleteReserveTimes(shoulddeleteIDs);
						
						List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
								, CalendarUtils.timeToString(endTime));
						if ((endTime - startTime) % unitDur != 0)
						{
							long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStarttime())
									+ unitDur : startTime;
							long holdduration = (endTime - startTime) % unitDur;
							holdResTime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
							holdResTime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
							newreservetimes.add(holdResTime);
						}
						
						insertReserveTimes(newreservetimes);	
			}
		}else
		{
			if(unitDur != resTimeDuration)
			{		
				holdResTime.setStatus(4);
				holdResTime.setClientID(clientID);
				holdResTime.setDateID(dateID);
				holdResTime.setUnitID(unitID);
				holdResTime.setMiddayID(middayID);
				long reservedtimeduration = CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(unitDur));
				setDuration(ID, new Time(reservedtimeduration));
				
				//If the selected time is the last time of the midday
				if(reservetime.getID() == oldReserveTimes.get(oldReserveTimes.size()-1))
				{					
					if(resTimeDuration > unitDur)
					{
						long startTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStarttime()) + unitDur;
						long endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStarttime()) + resTimeDuration;
						
						List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
								, CalendarUtils.timeToString(endTime));
						if ((endTime - startTime) % unitDur != 0)
						{
							long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStarttime())
									+ unitDur : startTime;
							long holdduration = (endTime - startTime) % unitDur;
							holdResTime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
							holdResTime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
							newreservetimes.add(holdResTime);
						}							
						insertReserveTimes(newreservetimes);
					}else
					{
						return setStatus(ID, 4);
					}
				}
				else
				{
					List<Integer> suboldreservetimes = oldReserveTimes.subList(oldReserveTimes.indexOf(ID)+1, oldReserveTimes.size());
					int endtimeID = 0;
					long endTime = 0;
					
					for (int i = 0 ; i < suboldreservetimes.size() ; i++)
					{
						if(getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 2
								|| getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 3)
						{
							endtimeID = suboldreservetimes.get(i);
							endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStarttime());
							break;
						}else
						{
							endtimeID = suboldreservetimes.get(i);
							endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStarttime()) + 
									CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
						}
				
					}
					List<Integer> shoulddeleteIDs = suboldreservetimes.indexOf(endtimeID) == suboldreservetimes.size()-1 && 
							( getReserveTimeFromID(endtimeID).getStatus() == 1 || getReserveTimeFromID(endtimeID).getStatus() == 4 )
							? suboldreservetimes.subList(0, suboldreservetimes.indexOf(endtimeID)+1) : suboldreservetimes.subList(0, 
									suboldreservetimes.indexOf(endtimeID));	
							
							if(!shoulddeleteIDs.isEmpty())
								deleteReserveTimes(shoulddeleteIDs);
							
							long startTime = CalendarUtils.sqlTimeToLong(reservetime.getStarttime()) + unitDur;
							List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
									, CalendarUtils.timeToString(endTime));
							if ((endTime - startTime) % unitDur != 0)
							{
								long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStarttime())
										+ unitDur : startTime;
								long holdduration = (endTime - startTime) % unitDur;
								holdResTime.setStarttime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
								holdResTime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
								newreservetimes.add(holdResTime);
							}
							insertReserveTimes(newreservetimes);	
				}
			
			}
		}		
		return true;
	}
	
	public static boolean deleteReserveTimes(List<Integer> IDs)
	{
		for(int i = 0; i<IDs.size(); i++)
		{			
			if(deleteReserveTime(IDs.get(i)));
			else
				return false;
		}
		return true;
	}
	
	public static boolean isPossibleToGetForService(int ID, Time serviceDur)
	{
		ReserveTime restime = getReserveTimeFromID(ID);
		int dateID = restime.getDateID();
		int unitID = restime.getUnitID();
		int middayID = restime.getMiddayID();
		List<Integer> oldreservetimes = getReserveTimeIDsFromMidday(dateID, unitID, middayID);
		List<Integer> suboldreservetimes = oldreservetimes.subList(oldreservetimes.indexOf(ID), oldreservetimes.size());
		int endtimeID = 0;
		long endTime = 0;
		long stTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStarttime());
		long servicedur = CalendarUtils.sqlTimeToLong(serviceDur);
		
		for (int i = 0 ; i < suboldreservetimes.size() ; i++)
		{
			if(getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 2
					|| getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 3)
			{
				endtimeID = suboldreservetimes.get(i);
				endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStarttime());
				break;
			}else
			{
				endtimeID = suboldreservetimes.get(i);
				endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStarttime()) + 
						CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
			}
		}
		return endTime - stTime >= servicedur ? true : false;
	}
	
	
	public static boolean clientHasAReserveTime(int client_id)
	{
		return ! getClientNotExpiredTimes(client_id).isEmpty() ? true : false;
	}	
	
	public static List<ReserveTime> getClientNotExpiredTimes(int client_id)
	{
		List<ReserveTime> clienttimes = getClientReservedTimes(client_id);
		List<ReserveTime> newclienttimes = new ArrayList<ReserveTime>();
		CalendarUtils dateaction  =new CalendarUtils();				
		for(int i = 0; i < clienttimes.size(); i++)
		{
			if(clienttimes.get(i).getDateID() > dateaction.getCalDateID(CalendarUtils.getCurrDate()))
				newclienttimes.add(clienttimes.get(i));
			else if(clienttimes.get(i).getDateID() == dateaction.getCalDateID(CalendarUtils.getCurrDate()))
			{
				if(CalendarUtils.isExpiredHour(clienttimes.get(i).getStarttime()))
					newclienttimes.add(clienttimes.get(i));
			}
		}
		return newclienttimes;
	}
	
	//0:Expired Times 1:Not Expired Times
	public static Map<String,ReserveTime> getClientAllReserveTimes(int client_id)
	{
		Map<String,ReserveTime> clientAllTimes = new LinkedHashMap<String,ReserveTime>();
		List<ReserveTime> clientTimes = getClientReservedTimes(client_id);
		CalendarUtils dateaction  = new CalendarUtils();				
		for(int i = 0; i < clientTimes.size(); i++)
		{
			if(clientTimes.get(i).getDateID() > dateaction.getCalDateID(CalendarUtils.getCurrDate()))
				clientAllTimes.put("0"+i, clientTimes.get(i));
			else if(clientTimes.get(i).getDateID() == dateaction.getCalDateID(CalendarUtils.getCurrDate()))
			{
				if(CalendarUtils.isExpiredHour(clientTimes.get(i).getStarttime()))
					clientAllTimes.put("0"+i, clientTimes.get(i));
				else if(CommentUtils.getCommentByResTimeID(clientTimes.get(i).getID()) != null)
					clientAllTimes.put("1"+i, clientTimes.get(i));
				else
					clientAllTimes.put("2"+i, clientTimes.get(i));
			}else if(CommentUtils.getCommentByResTimeID(clientTimes.get(i).getID()) != null)
				clientAllTimes.put("1"+i, clientTimes.get(i));
			else
				clientAllTimes.put("2"+i, clientTimes.get(i));
				
		}
		return clientAllTimes;
	}
	
	public static boolean isExistedReserveCode(String resCodeID)
	{		
		CalendarUtils dateaction  =new CalendarUtils();
		ReserveTime restime = getReserveTimeFromCode(resCodeID);
		if(restime != null)
		{
			if(restime.getDateID() > dateaction.getCalDateID(CalendarUtils.getCurrDate()))
					return true;
			else if (restime.getDateID() == dateaction.getCalDateID(CalendarUtils.getCurrDate()))
			{
				if(CalendarUtils.isExpiredHour(restime.getStarttime()))
					return true;
			}
		}
		return false;
	}
	
	public static List<Integer> getRestimeIDsForChangeActions(List<ReserveTime> restimes)
	{
		List<Integer> restimeIDlist = new ArrayList<Integer>();
		for (int i = 0; i< restimes.size(); i++)
		{
			int restimeID = restimes.get(i).getID();
			restimeIDlist.add(restimeID);
		}
		return restimeIDlist;
	}
	
	public static List<ReserveTime> getClientResTimeForChangeActions(List<ReserveTime> restimes)
	{
		List<ReserveTime> clienrestimes = new ArrayList<ReserveTime>();
		for (int i = 0; i< restimes.size(); i++)
		{
			int ID = restimes.get(i).getClientID();
			if(ID != 0)
				clienrestimes.add(restimes.get(i));
		}
		return clienrestimes;
	}
	
	public static List<Integer> getReservedDatesForChangeActions(List<ReserveTime> restimes)
	{
		List<Integer> dateIDs = new ArrayList<Integer>();
		for (int i = 0; i < restimes.size(); i++)
		{
			int ID = restimes.get(i).getDateID();
			if(!dateIDs.contains(ID))
				dateIDs.add(restimes.get(i).getDateID());
		}
		return dateIDs;
	}
	
	public static List<Integer> getInterferedDatesBetweenMiddays(int unitID, int stDate, int endDate,  String stTime, String endTime, int middayID)
	{
		List<Integer> interfereresDates  = new ArrayList<Integer>(); 
		for (int date = stDate; date <= endDate; date++)
		{
			if(middayID == 1)
			{
				if(getEndReserveTimeOfMidday(unitID, date, middayID) != null)
				{
					ReserveTime restime = getEndReserveTimeOfMidday(unitID, date, middayID);
					long endtime = CalendarUtils.sqlTimeToLong(restime.getStarttime()) + CalendarUtils.sqlTimeToLong(restime.getDuration());
					if(CalendarUtils.stringToTime(stTime) < endtime)
						interfereresDates.add(date);
				}
				
			}else if(middayID == 2)
			{
				if(getStartReserveTimeOfMidday(unitID, date, middayID) != null)
				{
					ReserveTime restime = getStartReserveTimeOfMidday(unitID, date, middayID);
					long starttime = CalendarUtils.sqlTimeToLong(restime.getStarttime());
					if( starttime <  CalendarUtils.stringToTime(endTime))
						interfereresDates.add(date);
				}
			}
		}
		return interfereresDates;
	}
	
	public static List<ReserveTime> getClientResTimeInAUnit(int unitID)
	{
		CalendarUtils dateaction = new CalendarUtils();
		int currdayID = dateaction.getCalDateID(CalendarUtils.getCurrDate());
		List<ReserveTime> restimes = getUnitReservedTimes(unitID, currdayID);
		List<ReserveTime> clientrestimes = new ArrayList<ReserveTime>(); 
		for (int i = 0 ; i < restimes.size(); i++)
		{
			if(restimes.get(i).getClientID() != 1) // Ignoring admin reserved times
				clientrestimes.add(restimes.get(i));
		}
		return clientrestimes;
	}
			
	public static boolean isExisted(int ID)
	{
		return getStatus(ID) != 5 ? true : false;
	}
	
	public static boolean isNotReserved(int ID)
	{
		return getStatus(ID) == 1 ? true : false;
	}
	
	public static boolean isReserved(int ID)
	{
		return getStatus(ID) == 2 ? true : false;
	}
	
	public static boolean isCanceled(int ID)
	{
		return getStatus(ID) == 3 ? true : false;
	}
	
	public static boolean cancelReserveTime(Integer restimeID)
	{
			return setStatus(restimeID, 3);
	}
	
	public static boolean setFreeReserveTime(Integer restimeID)
	{
			return setStatus(restimeID, 1);
	}
}

