package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.CalendarDate;
import com.alomonshi.object.uiobjects.reservetimecalendar.ReserveTimeOfDate;
import com.alomonshi.object.uiobjects.reservetimecalendar.ReserveTimesOfMonth;
import com.alomonshi.utility.UtilityFunctions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableCalendar {

	/**
	 * Getting list of date between two dates with indicated day of weeks
	 * @param fromDateID from date
	 * @param toDateID to date
	 * @param dayNumbers determined day of week to be got
	 * @return list of calendar dates
	 */
	public static List<CalendarDate> getDates(int fromDateID, int toDateID, List<Integer> dayNumbers){
		List<CalendarDate> dates = new ArrayList<>();
		Connection conn = DBConnection.getConnection();
		try {
			String middleQuery = dayNumbers == null || dayNumbers.isEmpty()
					? " "
					: getDatesMiddleQuery(dayNumbers);
			String command = "SELECT" +
					" *" +
					" FROM" +
					" CALENDAR" +
					" WHERE" +
					" ID BETWEEN " + fromDateID + " AND " + toDateID
					+ middleQuery;
			Statement stmt =conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
            fillDates(rs, dates);
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}
		}
		return dates;
	}


	/**
	 * Getting day of a month with its empty reserve times
	 * @param unitID intended unit
	 * @return list empty reserve times in days of a month
	 */
	public static List<ReserveTimesOfMonth> getMonthDays(int unitID,
														 int fromDateID,
														 int toDateID,
														 int currentDateID) {
		List<ReserveTimesOfMonth> reserveTimesOfMonths = new ArrayList<>();
		Connection conn = DBConnection.getConnection();
		try {
			String command = "SELECT" +
					" cal.*," +
					" COUNT(rt.id) as reserveTimeNumbers," +
					" if(cal.id < " + currentDateID + ", false, true) as isActive" +
					" FROM" +
					" calendar cal" +
					" LEFT JOIN" +
					" reservetimes rt ON cal.id = rt.day_id AND unit_id = " + unitID +
					" AND rt.status = " + ReserveTimeStatus.RESERVABLE.getValue() +
					" WHERE" +
					" cal.id between " + fromDateID + " and " + toDateID +
					" GROUP BY cal.id" +
					" ORDER BY cal.id";
			Statement stmt =conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimeOfMonthList(rs, reserveTimesOfMonths);
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}
		}
		return reserveTimesOfMonths;
	}

	/**
	 * Getting days of a week wuery
	 * @param dayNumbers day numbers to be got
	 * @return intended query
	 */
	private static String getDatesMiddleQuery (List<Integer> dayNumbers) {
		StringBuilder columnName = new StringBuilder(" AND dayofweek IN ( ");
		return UtilityFunctions.getDayNumbersMiddleQuery(columnName, dayNumbers);
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
			date.setDayOfWeek(resultSet.getInt(8));
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

	/**
	 * Fill Reserve time of a date object
	 * @param resultSet returned from JDBC
	 * @param reserveTimeOfDate to be filled
	 */
	private static void fillReserveTimeOfDate (ResultSet resultSet,
											   ReserveTimeOfDate reserveTimeOfDate) {
		try {
			fillDate(resultSet, reserveTimeOfDate.getCalendarDate());
			reserveTimeOfDate.setEmptyReserveTimeNumbers(resultSet.getInt("reserveTimeNumbers"));
			reserveTimeOfDate.setActive(resultSet.getBoolean("isActive"));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Filling reserve times of month object
	 * @param resultSet returned from JDBC
	 * @param reserveTimesOfMonth object to be filled
	 */
	private static void fillReserveTimeOfMonth(ResultSet resultSet,
											   ReserveTimesOfMonth reserveTimesOfMonth) {
		ReserveTimeOfDate reserveTimeOfDate = new ReserveTimeOfDate();
		CalendarDate calendarDate = new CalendarDate();
		reserveTimeOfDate.setCalendarDate(calendarDate);
		fillReserveTimeOfDate(resultSet, reserveTimeOfDate);
		reserveTimesOfMonth.getReserveTimeOfDateList().add(reserveTimeOfDate);
		////Should be revised because it forces extra process to the program///
/*			reserveTimesOfMonth.setMonthName(calendarDate.getMonthName());
			reserveTimesOfMonth.setMonthNumber(calendarDate.getMonth());
			reserveTimesOfMonth.setYear(calendarDate.getYear());*/
	}

	/**
	 * Filling month reserve times information
	 * @param resultSet returned from JDBC
	 * @param reserveTimesOfMonth to be filled object
	 */
	private static void fillMonthInformation (ResultSet resultSet,
											  ReserveTimesOfMonth reserveTimesOfMonth) {
		try {
			reserveTimesOfMonth.setYear(resultSet.getInt(3));
			reserveTimesOfMonth.setMonthNumber(resultSet.getInt(4));
			reserveTimesOfMonth.setMonthName(resultSet.getString(6));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Filling reserve time dates
	 * @param resultSet returned from database
	 * @param reserveTimesOfMonthList returned list
	 */
	private static void fillReserveTimeOfMonthList(ResultSet resultSet,
												   List<ReserveTimesOfMonth> reserveTimesOfMonthList){
		try {
			ReserveTimesOfMonth reserveTimesOfMonth = new ReserveTimesOfMonth();
			List<ReserveTimeOfDate> reserveTimeOfDateList = new ArrayList<>();
			reserveTimesOfMonth.setReserveTimeOfDateList(reserveTimeOfDateList);
			boolean firstStep = true;
			while (resultSet.next()){
				if (!firstStep &&
						reserveTimesOfMonth.getMonthNumber() != resultSet.getInt(4)) {
					reserveTimesOfMonthList.add(reserveTimesOfMonth);
					reserveTimesOfMonth = new ReserveTimesOfMonth();
					reserveTimeOfDateList = new ArrayList<>();
					reserveTimesOfMonth.setReserveTimeOfDateList(reserveTimeOfDateList);
					fillMonthInformation(resultSet, reserveTimesOfMonth);
				}
				fillReserveTimeOfMonth(resultSet, reserveTimesOfMonth);
				if (firstStep) {
					firstStep = false;
					fillMonthInformation(resultSet, reserveTimesOfMonth);
				}
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

}