package com.alomonshi.bussinesslayer.reservetimes;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.bussinesslayer.ServiceResponse;
import com.alomonshi.datalayer.dataaccess.TableReserveTime;

import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.uiobjects.GenerateReserveTimeForm;
import com.alomonshi.restwebservices.message.ServerMessage;

public class ReserveTimeService{

    private GenerateReserveTimeForm generateReserveTimeForm;
    private ReserveTimeGenerator reserveTimeGenerator;
    private ReserveTimeDeletor reserveTimeDeletor;
    private ServiceResponse serviceRespone;

    public ReserveTimeService(GenerateReserveTimeForm generateReserveTimeForm, ServiceResponse serviceRespone){
        this.generateReserveTimeForm = generateReserveTimeForm;
        this.serviceRespone = serviceRespone;
        reserveTimeGenerator = new ReserveTimeGenerator(generateReserveTimeForm);
        reserveTimeDeletor = new ReserveTimeDeletor(serviceRespone);
    }

    /**
     * Handling reserve times to be inserted in data base, for inserting a list of reserve times
     * firstly all old times will be deleted if no time has been reserved in that duration, if there was a time
     * service response is returned with setting response to false and list of reserved time for showing to user
     * if there was not, inserting process will be started and if all times insert correctly, server response
     * will be returned with true response, otherwise response will set to false with empty response data
     * @return service response
     */

	public ServiceResponse handleGeneratingReserveTime(){
	    try {
            //delete previous reserve times if exited
            serviceRespone = reserveTimeDeletor.deleteUnitReserveTimeBetweenDays(
                    generateReserveTimeForm.getUnitID(),
                    generateReserveTimeForm.getStartDate(),
                    generateReserveTimeForm.getEndDate());
            //If there are no reserved times in requested days
            if (serviceRespone.getResponse()){
                List<ReserveTime> reserveTimes = reserveTimeGenerator.generateAllDayReserveTimes();
                boolean response = reserveTimes != null && insertReserveTimes(reserveTimes);
                serviceRespone.setResponse(response);
                if (response)
                    serviceRespone.setMessage(ServerMessage.SUCCESSMESSAGE);
                else
                    serviceRespone.setMessage(ServerMessage.FAULTMESSAGE);
            }else
                serviceRespone.setMessage(ServerMessage.RESERVETIMEERROR_01);
        }catch (Exception e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception in generating reserve times " + e);
            serviceRespone.setMessage(ServerMessage.FAULTMESSAGE);
        }
	    return serviceRespone;
	}

    /**
     * Inserting reserve times in database
     * @param reserveTimes to be inserted in database
     * @return true if all reserve times inserted truly in database
     */

	private static boolean insertReserveTimes(List<ReserveTime> reserveTimes)
	{
        return TableReserveTime.insertReserveTimeList(reserveTimes);
	}
/*
	public static boolean setClientNewReserveTime(ReserveTime reservetime)
	{
		List<Integer> serviceIDs = reservetime.getServiceIDs();
		int middayID = reservetime.getMiddayID();
		int ID = reservetime.getID(); 
		int dateID = reservetime.getDateID();
		int unitID = reservetime.getUnitID();
		int companyID = TableUnit.getCompanyID(unitID);
		int clientID = reservetime.getClientID();
		
		
		String resCodeID = Integer.toString(reservetime.getID());
		RestimeServiceUtils reserveTimeService = new RestimeServiceUtils();
		
		long resTimeDuration = CalendarUtils.sqlTimeToLong(reservetime.getDuration()) ;
		long serviceDur = (long) 0;
		for(Integer serviceID : serviceIDs)
		{
			serviceDur += CalendarUtils.stringToTime(TableService.getTime(serviceID));
			reserveTimeService.setRestimeID(ID).insertRestimeServ(dateID, serviceID, unitID, companyID, clientID);
		}
		reservetime.setStatus(2);
		reservetime.setResCodeID(resCodeID);
		setClientReserveData(ID, reservetime);
		
		if(serviceDur != resTimeDuration)
		{		
			List<Integer> oldReserveTimes = getReserveTimeIDsFromMidday(dateID, unitID, middayID);
			ReserveTime holdReserveTime = new ReserveTime();
			
			holdReserveTime.setStatus(4);
			holdReserveTime.setClientID(clientID);
			holdReserveTime.setDateID(dateID);
			holdReserveTime.setUnitID(unitID);
			holdReserveTime.setMiddayID(middayID);
			long reservedTimeDuration = CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(serviceDur));
			setDuration(ID, new Time(reservedTimeDuration));
			
			//If the selected time is the last time of the midday
			if(reservetime.getID() == oldReserveTimes.get(oldReserveTimes.size()-1))
			{								
				long holdDuration = resTimeDuration - serviceDur;
				long holdStartTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStartTime()) + serviceDur;
				holdReserveTime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdDuration))));
				holdReserveTime.setStartTime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdStartTime))));
				return insertReserveTime(holdReserveTime);
			}
			else
			{
				List<Integer> suboldreservetimes = oldReserveTimes.subList(oldReserveTimes.indexOf(ID)+1, oldReserveTimes.size());
				int endTimeID = 0;
				long endTime = 0;
				
				for (Integer id : suboldreservetimes)
				{
					if(getReserveTimeFromID(id).getStatus() == 2
							|| getReserveTimeFromID(id).getStatus() == 3)
					{
						endTimeID = id;
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(id).getStartTime());
						break;
					}else
					{
						endTimeID = id;
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endTimeID).getStartTime()) +
								CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endTimeID).getDuration());
					}
			
				}
				List<Integer> shoulddeleteIDs = suboldreservetimes.indexOf(endTimeID) == suboldreservetimes.size()-1 &&
						( getReserveTimeFromID(endTimeID).getStatus() == 1 || getReserveTimeFromID(endTimeID).getStatus() == 4 )
						? suboldreservetimes.subList(0, suboldreservetimes.indexOf(endTimeID)+1) : suboldreservetimes.subList(0,
								suboldreservetimes.indexOf(endTimeID));
						if(!shoulddeleteIDs.isEmpty())
							deleteReserveTimes(shoulddeleteIDs);
						
						long startTime = CalendarUtils.sqlTimeToLong(reservetime.getStartTime()) + serviceDur;
						List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
								, CalendarUtils.timeToString(endTime));
						if ((endTime - startTime) % resTimeDuration != 0)
						{
							long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStartTime())
									+ resTimeDuration : startTime;
							long holdduration = (endTime - startTime) % resTimeDuration;
							holdReserveTime.setStartTime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
							holdReserveTime.setDuration(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdduration))));
							newreservetimes.add(holdReserveTime);
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
							long startTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStartTime()) + unitDur;
							long endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStartTime()) + resTimeDuration;
							
							List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
									, CalendarUtils.timeToString(endTime));
							if ((endTime - startTime) % unitDur != 0)
							{
								long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStartTime())
										+ unitDur : startTime;
								long holdduration = (endTime - startTime) % unitDur;
								holdResTime.setStartTime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
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
								endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStartTime());
								break;
							}else
							{
								endtimeID = suboldreservetimes.get(i);
								endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStartTime()) +
										CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
							}
					
						}
						List<Integer> shoulddeleteIDs = suboldreservetimes.indexOf(endtimeID) == suboldreservetimes.size()-1 && 
								( getReserveTimeFromID(endtimeID).getStatus() == 1 || getReserveTimeFromID(endtimeID).getStatus() == 4 )
								? suboldreservetimes.subList(0, suboldreservetimes.indexOf(endtimeID)+1) : suboldreservetimes.subList(0, 
										suboldreservetimes.indexOf(endtimeID));
								
								if(!shoulddeleteIDs.isEmpty())
									deleteReserveTimes(shoulddeleteIDs);
								
								long startTime = CalendarUtils.sqlTimeToLong(reservetime.getStartTime()) + unitDur;
								List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
										, CalendarUtils.timeToString(endTime));
								if ((endTime - startTime) % unitDur != 0)
								{
									long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStartTime())
											+ unitDur : startTime;
									long holdduration = (endTime - startTime) % unitDur;
									holdResTime.setStartTime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
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
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStartTime());
						break;
					}else
					{
						endtimeID = suboldreservetimes.get(i);
						endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStartTime()) +
								CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
					}
			
				}
				long startTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(0)).getStartTime());
				
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
							long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStartTime())
									+ unitDur : startTime;
							long holdduration = (endTime - startTime) % unitDur;
							holdResTime.setStartTime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
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
						long startTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStartTime()) + unitDur;
						long endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStartTime()) + resTimeDuration;
						
						List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
								, CalendarUtils.timeToString(endTime));
						if ((endTime - startTime) % unitDur != 0)
						{
							long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStartTime())
									+ unitDur : startTime;
							long holdduration = (endTime - startTime) % unitDur;
							holdResTime.setStartTime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
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
							endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStartTime());
							break;
						}else
						{
							endtimeID = suboldreservetimes.get(i);
							endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStartTime()) +
									CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
						}
				
					}
					List<Integer> shoulddeleteIDs = suboldreservetimes.indexOf(endtimeID) == suboldreservetimes.size()-1 && 
							( getReserveTimeFromID(endtimeID).getStatus() == 1 || getReserveTimeFromID(endtimeID).getStatus() == 4 )
							? suboldreservetimes.subList(0, suboldreservetimes.indexOf(endtimeID)+1) : suboldreservetimes.subList(0, 
									suboldreservetimes.indexOf(endtimeID));	
							
							if(!shoulddeleteIDs.isEmpty())
								deleteReserveTimes(shoulddeleteIDs);
							
							long startTime = CalendarUtils.sqlTimeToLong(reservetime.getStartTime()) + unitDur;
							List<ReserveTime> newreservetimes = generateReserveTime(unitID, dateID, dateID, middayID, CalendarUtils.timeToString(startTime)
									, CalendarUtils.timeToString(endTime));
							if ((endTime - startTime) % unitDur != 0)
							{
								long holdsttime = !newreservetimes.isEmpty() ? CalendarUtils.sqlTimeToLong(newreservetimes.get(newreservetimes.size()-1).getStartTime())
										+ unitDur : startTime;
								long holdduration = (endTime - startTime) % unitDur;
								holdResTime.setStartTime(new Time(CalendarUtils.stringToTimeForDB(CalendarUtils.timeToString(holdsttime))));
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
		long stTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(ID).getStartTime());
		long servicedur = CalendarUtils.sqlTimeToLong(serviceDur);
		
		for (int i = 0 ; i < suboldreservetimes.size() ; i++)
		{
			if(getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 2
					|| getReserveTimeFromID(suboldreservetimes.get(i)).getStatus() == 3)
			{
				endtimeID = suboldreservetimes.get(i);
				endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(suboldreservetimes.get(i)).getStartTime());
				break;
			}else
			{
				endtimeID = suboldreservetimes.get(i);
				endTime = CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getStartTime()) +
						CalendarUtils.sqlTimeToLong(getReserveTimeFromID(endtimeID).getDuration());
			}
		}
		return endTime - stTime >= servicedur;
	}
	
	
	public static boolean clientHasAReserveTime(int client_id)
	{
		return !getClientNotExpiredTimes(client_id).isEmpty();
	}	
	
	public static List<ReserveTime> getClientNotExpiredTimes(int client_id)
	{
		List<ReserveTime> clienttimes = getClientReservedTimes(client_id);
		List<ReserveTime> newclienttimes = new ArrayList<>();
		CalendarUtils dateaction  =new CalendarUtils();
		for(int i = 0; i < clienttimes.size(); i++)
		{
			if(clienttimes.get(i).getDateID() > dateaction.getCalDateID(CalendarUtils.getCurrDate()))
				newclienttimes.add(clienttimes.get(i));
			else if(clienttimes.get(i).getDateID() == dateaction.getCalDateID(CalendarUtils.getCurrDate()))
			{
				if(CalendarUtils.isExpiredHour(clienttimes.get(i).getStartTime()))
					newclienttimes.add(clienttimes.get(i));
			}
		}
		return newclienttimes;
	}
	
	//0:Expired Times 1:Not Expired Times
	public static Map<String,ReserveTime> getClientAllReserveTimes(int client_id)
	{
		Map<String,ReserveTime> clientAllTimes = new LinkedHashMap<>();
		List<ReserveTime> clientTimes = getClientReservedTimes(client_id);
		CalendarUtils dateaction  = new CalendarUtils();				
		for(int i = 0; i < clientTimes.size(); i++)
		{
			if(clientTimes.get(i).getDateID() > dateaction.getCalDateID(CalendarUtils.getCurrDate()))
				clientAllTimes.put("0"+i, clientTimes.get(i));
			else if(clientTimes.get(i).getDateID() == dateaction.getCalDateID(CalendarUtils.getCurrDate()))
			{
				if(CalendarUtils.isExpiredHour(clientTimes.get(i).getStartTime()))
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

	public static List<Integer> getRestimeIDsForChangeActions(List<ReserveTime> restimes)
	{
		List<Integer> restimeIDlist = new ArrayList<>();
		for (int i = 0; i< restimes.size(); i++)
		{
			int restimeID = restimes.get(i).getID();
			restimeIDlist.add(restimeID);
		}
		return restimeIDlist;
	}
	
	public static List<ReserveTime> getClientResTimeForChangeActions(List<ReserveTime> resTimes)
	{
		List<ReserveTime> reserveTimes = new ArrayList<>();
		for (int i = 0; i< resTimes.size(); i++)
		{
			int ID = resTimes.get(i).getClientID();
			if(ID != 0)
				reserveTimes.add(resTimes.get(i));
		}
		return reserveTimes;
	}

	public static boolean isExisted(int ID)
	{
		return getStatus(ID) != 5;
	}
	
	public static boolean isNotReserved(int ID)
	{
		return getStatus(ID) == 1;
	}
	
	public static boolean isReserved(int ID)
	{
		return getStatus(ID) == 2;
	}
	
	public static boolean isCanceled(int ID)
	{
		return getStatus(ID) == 3;
	}
	
	public static boolean cancelReserveTime(Integer restimeID)
	{
			return setStatus(restimeID, 3);
	}
	
	public static boolean setFreeReserveTime(Integer restimeID)
	{
			return setStatus(restimeID, 1);
	}*/
}

