package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class TableCalendar {

	/**
	 * Author Behzad
	 * Getting id of the intended date
	 * @param date id of date
	 * @return id of date in calendar table
	 */
	public int getCalDateID(String date)
	{
		String command = "select ID from CALENDAR where date = '" + date + "'";
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			return rs.getInt("ID");
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
		return 0;
	}

	//Getting date

	/**
	 * Author Behzad
	 * Getting date by date id
	 * @param dateID date of calendar table
	 * @return date
	 */
	public String getDate(int dateID)
	{
		String command="select date from CALENDAR where id =" + dateID;
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

	/**
	 * Author Behzad
	 * @param dateID id of intended date
	 * @return day name
	 */

	public String getDayName(int dateID)
	{
		String command="select dayname from CALENDAR where ID = " + dateID;
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
						return "????";
					case "Sunday":
						return "??????";
					case "Monday":
						return "??????";
					case "Tuesday":
						return "?? ????";
					case "Wednesday":
						return "????????";
					case "Thursday":
						return "???????";
					case "Friday":
						return "????";
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

	public static List<Integer> getDates(int formDateID, int toDateID){
		String command = "select ID from CALENDAR where ID between " + formDateID + " and " + toDateID ;
		List<Integer> dates = new ArrayList<>();
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				dates.add(rs.getInt(1)) ;
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
		return dates;
	}
}
