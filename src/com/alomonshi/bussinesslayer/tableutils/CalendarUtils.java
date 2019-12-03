package com.alomonshi.bussinesslayer.tableutils;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import com.alomonshi.datalayer.dataaccess.TableCalendar;
import com.alomonshi.datalayer.databaseconnection.DBConnection;

public class CalendarUtils extends TableCalendar {
	
	//Converting string format to time format
	public static long stringToTime(String time)
	{
		DateFormat df = new SimpleDateFormat("HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			Date date = df.parse(time);
			return date.getTime();
		}
		catch(ParseException e){
			e.printStackTrace();		
		}
		return 0;
	}
	
	public String getDate(int dateID) {		
		String parts[] = super.getDate(dateID).split(Pattern.quote("-"));
		parts[1] = parts[1].length()==1 ? "0" + parts[1] : parts[1];
		parts[2] = parts[2].length()==1 ? "0" + parts[2] : parts[2];		
		return parts[0]+"/"+parts[1]+"/"+parts[2];
	}
	
	//Converting string format to time format
	public static long stringToTimeForDB(String time)
	{
		DateFormat df=new SimpleDateFormat("HH:mm");
        //df.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			Date date=df.parse(time);
			return date.getTime();
		}
		catch(ParseException e){
			e.printStackTrace();		
		}
		return 0;
	}
	
	public static long sqlTimeToLong(Time time)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(time);
		return calendar.get(Calendar.HOUR_OF_DAY)*3600*1000 + calendar.get(Calendar.MINUTE)*60*1000 + calendar.get(Calendar.SECOND)*1000;   
	}
	
	//converting time format to string format
	public static String timeToString(long time)
	{
		DateFormat df=new SimpleDateFormat("HH:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date(time));
	}
	
	//Getting the current hour
	public static Time getCurrHour()
	{
		String command="select curtime()";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getTime(1);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(conn != null)
			{
				try 
				{
						conn.close();		
				} catch (SQLException e)  
				{	
					e.printStackTrace();	
				}
			}	
		}
		return null;
	}	
	
	//Getting current date
	public static String getCurrDate()
	{
		String command="select pdate(now())";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getString(1).substring(0,rs.getString(1).indexOf(" "));//get date;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(conn != null)
			{
				try 
				{
						conn.close();		
				} catch (SQLException e)  
				{	
					e.printStackTrace();	
				}
			}	
		}
		return null;
	}
	
	//Getting current year
	public static int getCurrYear()
	{
		String command="select pyear(now())";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				  return rs.getInt(1);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			return 0;
		}finally
		{
			if(conn != null)
			{
				try 
				{
						conn.close();		
				} catch (SQLException e)  
				{	
					e.printStackTrace();	
				}
			}	
		}
		return 0;
	}
	
	//getting current month
	public static int getCurrMonth()
	{
		String command="select pmonth(now())";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				  return rs.getInt(1);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			return 0;
		}finally
		{
			if(conn != null)
			{
				try 
				{
						conn.close();		
				} catch (SQLException e)  
				{	
					e.printStackTrace();	
				}
			}	
		}
		return 0;
	}
	//getting current month name
	public static String getCurrMonthName()
	{
		String command="select pmonthname(now())";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				  return rs.getString(1);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(conn != null)
			{
				try 
				{
						conn.close();		
				} catch (SQLException e)  
				{	
					e.printStackTrace();
				}
			}	
		}
		return null;
	}
	
	//getting current day name
	public static String getCurrDayName(String date)
	{
		String command="select dayname('"+date+"') as dayname";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				switch(rs.getString("dayname"))
				{
					case "Saturday":
						return "شنبه";
					case "Sunday":
						return "یکشنبه";
					case "Monday":
						return "دوشنبه";						
					case "Tuesday":
						return "سه شنبه";				
					case "Wednesday":
						return "چهارشنبه";						
					case "Thursday":
						return "پنجشنبه";						
					case "Friday":
						return "جمعه";						
					default: return "";
				}
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(conn != null)
			{
				try 
				{
						conn.close();		
				} catch (SQLException e)  
				{	
					e.printStackTrace();	
				}
			}	
		}
		return null;
	}
		
	static boolean isExpiredHour(Time hour)
	{
		return !hour.before(Objects.requireNonNull(getCurrHour()));
	}
	
	public static boolean isExpiredDate(int dateID) {
		return dateID <= new CalendarUtils().getCalDateID(getCurrDate());
	}


}
