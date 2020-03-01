package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.CalendarDate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableCalendar {

	public static CalendarDate getDate(int dateID){
        String command = "select * from CALENDAR where ID = " + dateID;
        Connection conn = DBConnection.getConnection();
        CalendarDate date = new CalendarDate();
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillSingleDate(rs, date);
        }catch(SQLException e)
        {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }finally
        {
            if(conn != null)
            {
                try
                {
                    conn.close();
                } catch (SQLException e)
                {
                    Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                }
            }
        }
        return date;
	}

	public static List<CalendarDate> getDates(int formDateID, int toDateID){
		String command = "select * from CALENDAR where ID between " + formDateID + " and " + toDateID ;
		List<CalendarDate> dates = new ArrayList<>();
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
            fillDates(rs, dates);
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally
		{
			if(conn != null)
			{
				try
				{
					conn.close();
				} catch (SQLException e)
				{
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}
		}
		return dates;
	}

	private static void fillDate(ResultSet resultSet, CalendarDate date){
		try {
			date.setID(resultSet.getInt(1));
			date.setDate(resultSet.getString(2));
			date.setYear(resultSet.getInt(3));
			date.setMonth(resultSet.getInt(4));
			date.setDayOfMonth(resultSet.getInt(5));
			date.setMonthName(resultSet.getString(6));
			date.setDayName(resultSet.getString(7));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillSingleDate(ResultSet resultSet, CalendarDate date){
	    try {
	        while (resultSet.next()){
	            fillDate(resultSet, date);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    private static void fillDates(ResultSet resultSet, List<CalendarDate> dates){
	    try {
	        while (resultSet.next()){
	            CalendarDate date = new CalendarDate();
	            fillDate(resultSet, date);
	            dates.add(date);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }
}
