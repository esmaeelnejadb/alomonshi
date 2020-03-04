package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.object.uiobjects.ClientReservedTime;

public class TableReserveTime {

	private static String insertCommand = "INSERT INTO RESERVETIMES(" +
			"UNIT_ID" +
			", DAY_ID" +
			", MIDDAY_ID" +
			", ST_TIME" +
			", DURATION" +
			", STATUS" +
			", CLIENT_ID" +
			", RES_CODE_ID" +
			", RESERVE_GR_TIME" +
			")" +
			" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static String updateCommand = "UPDATE RESERVETIMES SET" +
			" UNIT_ID = ?" +
			", DAY_ID = ?" +
			", MIDDAY_ID = ?" +
			", ST_TIME = ?" +
			", DURATION = ?" +
			", STATUS = ?" +
			", CLIENT_ID = ?" +
			", RES_CODE_ID = ? " +
			", RESERVE_GR_TIME = ? " +
			"WHERE id = ";

    /**
	 * inserting a reserve time in database
	 * @param reserveTime a reserve time to be inserted in database
	 * @return true is inserted truly
	 */
	public static boolean insertReserveTime(ReserveTime reserveTime){
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(reserveTime, insertCommand, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Inserting a list of time
	 * @param reserveTimes list to be inserted in database
	 * @return true if all data inserted truly
	 */

	public static synchronized boolean insertReserveTimeList(List<ReserveTime> reserveTimes){
		Connection connection = DBConnection.getConnection();
		for(ReserveTime reserveTime : reserveTimes) {
			if(!executeInsertUpdate(reserveTime, insertCommand, connection)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * Updating a reserve time in database
	 * @param reserveTime a reserve time to be updated in database
	 * @return true if updated truly
	 */

	public static boolean updateReserveTime(ReserveTime reserveTime){
		Connection connection = DBConnection.getConnection();
        boolean response = executeInsertUpdate(reserveTime, updateCommand + reserveTime.getID(), connection);
		DBConnection.closeConnection(connection);
		return response;
	}


	/**
	 * updating a list of time
	 * @param reserveTimes list to be updated in database
	 * @return true if all data updated truly
	 */

	public static boolean updateReserveTimeList(List<ReserveTime> reserveTimes){
		Connection connection = DBConnection.getConnection();
		for(ReserveTime reserveTime : reserveTimes) {
			if(!executeInsertUpdate(reserveTime, updateCommand + reserveTime.getID(), connection)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * deleting a time from database
	 * @param reserveTime a reserve time to be deleted from database
	 * @return true if deleted from database truly
	 */
	public static boolean deleteReserveTime(ReserveTime reserveTime){
	    reserveTime.setStatus(ReserveTimeStatus.DELETED);
	    return updateReserveTime(reserveTime);
    }

	/**
	 * updating a list of time
	 * @param reserveTimes list to be updated in database
	 * @return true if all data updated truly
	 */

	public static boolean deleteReserveTimeList(List<ReserveTime> reserveTimes){
		Connection connection = DBConnection.getConnection();
		for(ReserveTime reserveTime : reserveTimes) {
			reserveTime.setStatus(ReserveTimeStatus.DELETED);
			if(!executeInsertUpdate(reserveTime, updateCommand + reserveTime.getID(), connection)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * Cancel Reserve Time
	 * @param reserveTimes to be canceled
	 * @return true all reserve times be canceled truly
	 */

	public static boolean cancelReservableTimeList(List<ReserveTime> reserveTimes) {
		Connection connection = DBConnection.getConnection();
		for(ReserveTime reserveTime : reserveTimes) {
			reserveTime.setStatus(ReserveTimeStatus.CANCELED);
			if(!executeInsertUpdate(reserveTime, updateCommand + reserveTime.getID(), connection)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * Retrive Canceled Reserve Time
	 * @param reserveTimes to be canceled
	 * @return true all reserve times be canceled truly
	 */

	public static boolean retriveCanceledTimeList(List<ReserveTime> reserveTimes) {
		Connection connection = DBConnection.getConnection();
		for(ReserveTime reserveTime : reserveTimes) {
			reserveTime.setStatus(ReserveTimeStatus.RESERVABLE);
			if(!executeInsertUpdate(reserveTime, updateCommand + reserveTime.getID(), connection)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	private static boolean executeInsertUpdate(ReserveTime reserveTime, String command, Connection connection)
	{
		try
		{
			PreparedStatement ps = connection.prepareStatement(command);
			prepare(ps, reserveTime);
			int i = ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}
	}

	/**
	 * Delete reserve times between days
	 * @param startDate start day for deleting times
	 * @param endDate end day for de;leting time
	 * @param unitID intended unit to be changed
	 * @return true if all statuses change successfully
	 */

    public static boolean deleteAllDayReserveTimesBetweenDays(int startDate, int endDate, int unitID)
    {
        String command = "UPDATE RESERVETIMES " +
				"SET " +
				"    STATUS = " + ReserveTimeStatus.DELETED.getValue() +
				" WHERE" +
				"    DAY_ID BETWEEN " + startDate + " AND " + endDate +
				"        AND UNIT_ID = " + unitID +
				" AND STATUS != " + ReserveTimeStatus.RESERVED.getValue();
        Connection conn = DBConnection.getConnection();
        try
        {
            PreparedStatement ps = conn.prepareStatement(command);
            int i = ps.executeUpdate();
            return i >= 0 ;
        }catch(SQLException e)
        {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            return false;
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
    }

	/**
	 * Deleting midday reserve times between days
	 * @param startDate start day for deleting
	 * @param endDate end day for deleting
	 * @param unitID intended unit to be changed
	 * @param middayID intended midday to be deleted
	 * @return true if all statuses change to deleted
	 */

	public static boolean deleteMiddayReserveTimesBetweenDays(int startDate
			, int endDate
			, int unitID
			, MiddayID middayID)
	{
		String command = "UPDATE RESERVETIMES " +
				"SET " +
				"    STATUS = " + ReserveTimeStatus.DELETED.getValue() +
				" WHERE" +
				"    DAY_ID BETWEEN " + startDate + " AND " + endDate +
				"        AND UNIT_ID = " + unitID +
				" AND MIDDAY_ID = " + middayID.getValue() +
				" AND STATUS != " + ReserveTimeStatus.RESERVED.getValue();
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			int i = ps.executeUpdate();
			return i >= 0 ;
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
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
	}

	public static boolean deleteUnit(int unitID)
	{
		String command = "update RESERVETIMES set status = "
				+ ReserveTimeStatus.DELETED.getValue()
				+ " where unit_ID = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, unitID);
			int i = ps.executeUpdate();
			return i == 1;

		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
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
	}

	public static boolean deleteService(int unitID, int ID)
	{
		String command = "update RESERVETIMES set status = "
				+ ReserveTimeStatus.DELETED.getValue()
				+ " where unit_ID = ? AND ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, unitID);
			ps.setInt(2, ID);
			int i=ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
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
	}

	/**
	 * Getting reserve time by id
	 * @param ID to be got
	 * @return got reserve time
	 */
	public static ReserveTime getReserveTime(int ID)
	{
		Connection conn = DBConnection.getConnection();
		ReserveTime reserveTime = new ReserveTime();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" RESERVETIMES" +
					" WHERE" +
					" ID = " + ID;
			ResultSet rs = stmt.executeQuery(command);
			fillSingleReserveTime(rs, reserveTime);
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
		return reserveTime;
	}
	
	public static List<ReserveTime> getAdminUnitReserveTimeInADay(int dateID, int unitID)
	{
		Connection conn = DBConnection.getConnection();
		List<ReserveTime> reserveTimes = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" RESERVETIMES" +
					" WHERE" +
					" DAY_ID = " + dateID + " AND UNIT_ID = " + unitID +
					" AND (status != " + ReserveTimeStatus.DELETED.getValue() +
					" && status != " + ReserveTimeStatus.HOLD.getValue() + ")" +
					" ORDER BY ST_TIME";
			ResultSet rs=stmt.executeQuery(command);
			fillReserveTimeList(rs, reserveTimes);
			
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
		return reserveTimes;
	}

	/**
	 * Getting reservable time for client in a day in a unit
	 * @param dateID intended day
	 * @param unitID intended unit
	 * @return list of reserved times based on morning and afternoon time
	 */
	public static Map<MiddayID, List<ReserveTime>> getClientUnitReserveTimeInADay(int dateID, int unitID)
	{
		Connection conn = DBConnection.getConnection();
		Map<MiddayID, List<ReserveTime>> reserveTimes = new HashMap<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select * from RESERVETIMES" +
					" where DAY_ID = " +
					dateID +
					" and UNIT_ID = " +
					unitID +
					" and STATUS = " + ReserveTimeStatus.RESERVABLE.getValue() +
					" ORDER BY ST_TIME";
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimeList(rs, reserveTimes);

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
		return reserveTimes;
	}

	/**
	 * Getting a client reserved times
	 * @param clientID intended client
	 * @return list of client reserved times
	 */
	public static List<ClientReservedTime> getClientReservedTimes(int clientID) {
		Connection conn = DBConnection.getConnection();
		List<ClientReservedTime> clientReservedTimes = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			String command = "SELECT " +
					"    r.id AS id," +
					"    r.DAY_ID AS dayID," +
					"    r.ST_TIME AS startTime," +
					"    r.DURATION AS duration," +
					"    r.RES_CODE_ID AS reserveCode," +
					"    r.RESERVE_GR_TIME AS gregorianDate," +
					"    c.id AS companyID," +
					"    c.COMP_NAME AS companyName," +
					"    u.ID AS unitID," +
					"    u.UNIT_NAME AS unitName," +
					"    IFNULL(com.comment, ' ') AS comment," +
					"    IFNULL(com.SERVICE_RATE, 0) AS commentRate," +
					"    IFNULL(com.ID, 0) AS commentID," +
					"    IFNULL(com.SERVICE_RATE, 0) AS serviceRate" +
					" FROM" +
					"    alomonshi.reservetimes r" +
					"        LEFT JOIN" +
					"    alomonshi.comments com ON com.RES_TIME_ID = r.ID," +
					"    alomonshi.units u," +
					"    alomonshi.companies c" +
					" WHERE" +
					"    r.unit_id = u.id AND u.comp_id = c.id" +
					"        AND r.status = " + ReserveTimeStatus.RESERVED.getValue() +
					"        AND r.client_id = " + clientID +
					" ORDER BY r.day_id DESC;";
			ResultSet rs = stmt.executeQuery(command);
			fillClientReserveTimeList(rs, clientReservedTimes);
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return null;
		}finally {
			if(conn != null)
			{
				try {
						conn.close();		
				} catch (SQLException e) {
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}
		}
		return clientReservedTimes;
	}

	/**
	 * Getting a client reserved time from reserve time id
	 * @param reserveTimeID intended reserve time
	 * @return client reserve time
	 */
	public static ClientReservedTime getClientReservedTime(int reserveTimeID) {
		Connection conn = DBConnection.getConnection();
		ClientReservedTime clientReservedTime = new ClientReservedTime();
		try {
			Statement stmt = conn.createStatement();
			String command = "SELECT " +
					"    r.id AS id," +
					"    r.DAY_ID AS dayID," +
					"    r.ST_TIME AS startTime," +
					"    r.DURATION AS duration," +
					"    r.RES_CODE_ID AS reserveCode," +
					"    r.RESERVE_GR_TIME AS gregorianDate," +
					"    c.id AS companyID," +
					"    c.COMP_NAME AS companyName," +
					"    u.ID AS unitID," +
					"    u.UNIT_NAME AS unitName" +
					" FROM" +
					"    alomonshi.reservetimes r," +
					"    alomonshi.units u," +
					"    alomonshi.companies c" +
					" WHERE" +
					"    r.unit_id = u.id AND u.comp_id = c.id" +
					"        AND r.ID = " + reserveTimeID;
			ResultSet rs = stmt.executeQuery(command);
			fillSingleClientReservedTime(rs, clientReservedTime);
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally {
			if(conn != null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}
		}
		return clientReservedTime;
	}

	/**
	 * Getting a reserve time from its reserve code
	 * @param Code to be reserve time derived from
	 * @return intended reserve time
	 */
	public static ReserveTime getReserveTimeByCode(String Code)
	{
		Connection conn = DBConnection.getConnection();
		ReserveTime reserveTime = new ReserveTime();
		try
		{
			Statement stmt =conn.createStatement();
			String command = "select * " +
					"from RESERVETIMES " +
					"where" +
					" Res_code_ID = " +
					Code;
			ResultSet rs=stmt.executeQuery(command);
			fillSingleReserveTime(rs, reserveTime);
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return reserveTime;
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
		return reserveTime;
	}

	/**
	 * Getting reserve times in a unit in a day in a midday
	 * @param dateID intended day
	 * @param unitID intended uni
	 * @param middayID intended midday
	 * @return reserve time list
	 */
	public static List<ReserveTime> getReserveTimeIDsFromMidday(int dateID, int unitID, MiddayID middayID) {
		Connection conn = DBConnection.getConnection();
		List<ReserveTime> reserveTimes = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select" +
					" * from" +
					" RESERVETIMES" +
					" where" +
					" STATUS != "
					+ ReserveTimeStatus.DELETED.getValue() +
					" AND DAY_ID = " +
					dateID +
					" AND UNIT_ID = " +
					unitID +
					" AND MIDDAY_ID = " +
					middayID.getValue() +
					" ORDER BY ST_TIME";
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimeList(rs, reserveTimes);
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
		return reserveTimes;
	}


    /**
     * Getting reserve times in a unit between two days
     * @param unitID intended unit id
     * @param stDate start date
     * @param endDate end date
     * @return list of reserved times
     */
	public static List<ReserveTime> getUnitReservedTimesBetweenDays(int unitID, int stDate, int endDate)
	{
		Connection conn = DBConnection.getConnection();
		List<ReserveTime> reserveTimes = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" * FROM" +
					" RESERVETIMES" +
					" WHERE" +
					" STATUS = " +
					ReserveTimeStatus.RESERVED.getValue() +
					" AND UNIT_ID = " +
					unitID +
					" AND DAY_ID BETWEEN " +
					stDate +
					" AND " +
					endDate;
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimeList(rs, reserveTimes);
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return null;
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
		return reserveTimes;
	}

	/**
	 * Getting unit midday reserved times
	 * @param unitID input
	 * @param stDate input
	 * @param endDate input
	 * @param midday input
	 * @return reserve time list
	 */
	public static List<ReserveTime> getUnitMiddayReservedTimesBetweenDays(int unitID
			, int stDate
			, int endDate
			, MiddayID midday) {
		Connection conn = DBConnection.getConnection();
		List<ReserveTime> reserveTimes = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" * FROM" +
					" RESERVETIMES" +
					" WHERE" +
					" STATUS = " +
					ReserveTimeStatus.RESERVED.getValue() +
					" AND UNIT_ID = " +
					unitID +
					" AND DAY_ID BETWEEN " +
					stDate +
					" AND " +
					endDate +
					" AND MIDDAY_ID = " + midday.getValue();
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimeList(rs, reserveTimes);
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return null;
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
		return reserveTimes;
	}

	/**
	 * Getting first time of a midday between days
	 * @param unitID input
	 * @param stDate input
	 * @param endDate input
	 * @param midday input
	 * @return first time in a midday
	 */
	public static LocalTime getUnitMiddayFirstTimeBetweenDays(int unitID
			, int stDate
			, int endDate
			, MiddayID midday) {
		Connection conn = DBConnection.getConnection();
		LocalTime minimumStartTime = null;
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" MIN(ST_TIME) as minimumStartTime" +
					" FROM" +
					" reservetimes" +
					" WHERE" +
					" day_id BETWEEN " + stDate + " AND " + endDate +
					" AND unit_id = " + unitID +
					" AND midday_id = " + midday.getValue() +
					" AND status != " + ReserveTimeStatus.DELETED.getValue();
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()){
				minimumStartTime = rs.getObject("minimumStartTime", LocalTime.class);
			}
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
		return minimumStartTime;
	}

	/**
	 * Getting last time of a midday between days
	 * @param unitID input
	 * @param stDate input
	 * @param endDate input
	 * @param midday input
	 * @return last time in a midday
	 */
	public static LocalTime getUnitMiddayLastTimeBetweenDays(int unitID
			, int stDate
			, int endDate
			, MiddayID midday) {
		Connection conn = DBConnection.getConnection();
		LocalTime maximumLastTime = null;
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT " +
					" MAX(ADDTIME(ST_TIME, SEC_TO_TIME(duration * 60))) as maximumLastTime" +
					" FROM" +
					" alomonshi.reservetimes" +
					" WHERE" +
					" day_id BETWEEN " + stDate + " AND " + endDate +
					" AND unit_id = " + unitID +
					" AND midday_id = " + midday.getValue() +
					" AND status != " + ReserveTimeStatus.DELETED.getValue();
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()){
				maximumLastTime = rs.getObject("maximumLastTime", LocalTime.class);
			}
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
		return maximumLastTime;
	}

	/**
	 * Getting unit reserve times after current time
	 * @param unitID intended unit id
	 * @return list reserved times after now
	 */
	public static List<ReserveTime> getUnitFutureReserveTime(int unitID) {
		Connection conn = DBConnection.getConnection();
		List<ReserveTime> reserveTimes = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" * FROM" +
					" RESERVETIMES" +
					" WHERE" +
					" STATUS = " +
					ReserveTimeStatus.RESERVED.getValue() +
					" AND UNIT_ID = " +
					unitID +
					" AND RESERVE_GR_TIME >= '" +
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())+
					"'";
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimeList(rs, reserveTimes);
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
		return reserveTimes;
	}

	/**
	 * fill prepare statement object to execute query
	 * @param preparedStatement to be filled for execution
	 * @param reserveTime where prepared statement will be filled from
	 */
	private static void prepare(PreparedStatement preparedStatement, ReserveTime reserveTime){
		try {
			preparedStatement.setInt(1, reserveTime.getUnitID());
			preparedStatement.setInt(2, reserveTime.getDateID());
			preparedStatement.setInt(3, reserveTime.getMiddayID().getValue());
			preparedStatement.setObject(4, reserveTime.getStartTime());
			preparedStatement.setInt(5, reserveTime.getDuration());
			preparedStatement.setInt(6, reserveTime.getStatus().getValue());
			preparedStatement.setInt(7, reserveTime.getClientID());
			preparedStatement.setString(8, reserveTime.getResCodeID());
			preparedStatement.setObject(9, reserveTime.getReserveTimeGRDateTime());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill reserve time got from data base
	 * @param resultSet returned from JDBC
	 * @param reserveTime to be filled
	 */
	private static void fillReserveTime(ResultSet resultSet, ReserveTime reserveTime){
		try{
			reserveTime.setID(resultSet.getInt(1));
			reserveTime.setUnitID(resultSet.getInt(2));
			reserveTime.setDateID(resultSet.getInt(3));
			reserveTime.setMiddayID(MiddayID.getByValue(resultSet.getInt(4)));
			reserveTime.setStartTime(resultSet.getObject(5, LocalTime.class));
			reserveTime.setDuration(resultSet.getInt(6));
			reserveTime.setStatus(ReserveTimeStatus.getByValue(resultSet.getInt(7)));
			reserveTime.setClientID(resultSet.getInt(8));
			reserveTime.setResCodeID(resultSet.getString(9));
			reserveTime.setReserveTimeGRDateTime(resultSet.getObject(10, LocalDateTime.class));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * if a result be unique Object will be filled with this  method
	 * @param resultSet returned from JDBC
	 * @param reserveTime to be filled
	 */

	private static void fillSingleReserveTime(ResultSet resultSet, ReserveTime reserveTime){
	    try {
	        while (resultSet.next()){
	            fillReserveTime(resultSet, reserveTime);
            }
        }catch (SQLException e){
	        Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

	/**
	 * if a result is more than one object, result list will be filled with this method
	 * @param resultSet returned from JDBC
	 * @param reserveTimes to be filled
	 */
	private static void fillReserveTimeList(ResultSet resultSet, List<ReserveTime> reserveTimes){
		try{
			while (resultSet.next()){
				ReserveTime reserveTime = new ReserveTime();
				fillReserveTime(resultSet, reserveTime);
				reserveTimes.add(reserveTime);
			}
		}catch(SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * separate reserve times by morning and afternoon times
	 * @param resultSet returned from JDBC
	 * @param reserveTimes to be filled
	 */
	private static void fillReserveTimeList(ResultSet resultSet
			, Map<MiddayID
			, List<ReserveTime>> reserveTimes){
		try{
			List<ReserveTime> morningReserveTimes = new ArrayList<>();
			List<ReserveTime> afternoonReserveTimes = new ArrayList<>();
			while (resultSet.next()){
				ReserveTime reserveTime = new ReserveTime();
				fillReserveTime(resultSet, reserveTime);
				if (reserveTime.getMiddayID() == MiddayID.MORNING)
					morningReserveTimes.add(reserveTime);
				else if (reserveTime.getMiddayID() == MiddayID.AFTERNOON)
					afternoonReserveTimes.add(reserveTime);
			}
			reserveTimes.put(MiddayID.MORNING, morningReserveTimes);
			reserveTimes.put(MiddayID.AFTERNOON, afternoonReserveTimes);
		}catch(SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}


	/**
	 * Filling client reserve time information into ClientReserveTime object
	 * @param resultSet returned from JDBC
	 * @param clientReservedTime filled object
	 */
	private static void fillClientReserveTime(ResultSet resultSet
			, ClientReservedTime clientReservedTime) {
		try {
			clientReservedTime.setReserveTimeID(resultSet.getInt("id"));
			clientReservedTime.setDayID(resultSet.getInt("dayID"));
			clientReservedTime.setStartTime(resultSet.getObject("startTime", LocalTime.class));
			clientReservedTime.setDuration(resultSet.getInt("duration"));
			clientReservedTime.setReserveCodeID(resultSet.getString("reserveCode"));
			clientReservedTime.setGregorianDateTime(resultSet.getObject("gregorianDate", LocalDateTime.class));
			clientReservedTime.setCompanyID(resultSet.getInt("companyID"));
			clientReservedTime.setCompanyName(resultSet.getString("companyName"));
			clientReservedTime.setUnitID(resultSet.getInt("unitID"));
			clientReservedTime.setUnitName(resultSet.getString("unitName"));
			clientReservedTime.setServices(TableReserveTimeServices
					.getServices(clientReservedTime.getReserveTimeID()));
			clientReservedTime.setCost(getServicesTotalCost(clientReservedTime.getServices()));
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Filling client reserve time information into ClientReserveTime object
	 * @param resultSet returned from JDBC
	 * @param clientReservedTime filled object
	 */
	private static void fillClientReserveTimeCommentSection(ResultSet resultSet
			, ClientReservedTime clientReservedTime) {
		try {
			clientReservedTime.setCommentID(resultSet.getInt("commentID"));
			clientReservedTime.setComment(resultSet.getString("comment"));
			clientReservedTime.setCommentRate(resultSet.getFloat("commentRate"));
			clientReservedTime.setCancelable(isTimeCancelable(clientReservedTime));
			clientReservedTime.setCommentable(clientReservedTime.getCommentID() == 0
					&& !clientReservedTime.isCancelable());

		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill client reserve time list
	 * @param resultSet returned from JDBC
	 * @param clientReservedTimes to be filled
	 */
	private static void fillClientReserveTimeList(ResultSet resultSet
			, List<ClientReservedTime> clientReservedTimes) {
		try {
			while (resultSet.next()) {
				ClientReservedTime clientReservedTime = new ClientReservedTime();
				fillClientReserveTime(resultSet, clientReservedTime);
				//Fill comment section
				fillClientReserveTimeCommentSection(resultSet, clientReservedTime);
				clientReservedTimes.add(clientReservedTime);
			}
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill single client reserved time without comment sections
	 * @param resultSet returned from JDBC
	 * @param clientReservedTime to be filled object
	 */
	private static void fillSingleClientReservedTime(ResultSet resultSet,
													 ClientReservedTime clientReservedTime) {
		try {
			while (resultSet.next()) {
				fillClientReserveTime(resultSet, clientReservedTime);
			}
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Getting total cost of service list
	 * @param services to be calculated their cost
	 * @return total cost
	 */
	private static int getServicesTotalCost(List<Services> services){
		return services.stream().mapToInt(Services::getServicePrice).sum();
	}

	/**
	 * Check if time is can be canceled or not
	 * @param clientReservedTime to be checked
	 * @return true if it can be canceled
	 */
	private static boolean isTimeCancelable(ClientReservedTime clientReservedTime) {
		//Reserved time can be canceled until 1 hour before time is reached
		return clientReservedTime.getGregorianDateTime().minusHours(1).isAfter(LocalDateTime.now());
	}
}