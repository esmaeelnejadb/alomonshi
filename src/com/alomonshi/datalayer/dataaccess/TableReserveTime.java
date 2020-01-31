package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.server.AlomonshiServer;

public class TableReserveTime {

	private static String insertCommand = "insert into RESERVETIMES(UNIT_ID, DAY_ID, MIDDAY_ID," +
			" ST_TIME, DURATION, STATUS) values(?, ?, ?, ?, ?, ?)";

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

	public static boolean insertReserveTimeList(List<ReserveTime> reserveTimes){
		Connection connection = DBConnection.getConnection();
		for(ReserveTime reserveTime : reserveTimes)
			if(!executeInsertUpdate(reserveTime, insertCommand, connection))
				return false;
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
        String updateCommand = "update RESERVETIMES set UNIT_ID = ?, DAY_ID = ?, MIDDAY_ID = ?, ST_TIME = ?, DURATION = ?" +
                ", STATUS = ? where id = " + reserveTime.getID();
        boolean response = executeInsertUpdate(reserveTime, updateCommand, connection);
		DBConnection.closeConnection(connection);
		return response;
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
			Logger.getLogger(AlomonshiServer.class.getName()).log(Level.SEVERE, "Exception : " + e);
			return false;
		}
	}

    public static boolean deleteBetweenDays(int startDate, int endDate, int unitID)
    {
        String command = "update RESERVETIMES set status = " + ReserveTimeStatus.DELETED.getValue() + " where " +
                "DAY_ID between ? and ? and UNIT_ID = ?";
        Connection conn = DBConnection.getConnection();
        try
        {
            PreparedStatement ps = conn.prepareStatement(command);
            ps.setInt(1, startDate);
            ps.setInt(2, endDate);
            ps.setInt(3, unitID);
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

	public static boolean deleteUnit(int unitID)
	{
		String command = "update RESERVETIMES set status = " + ReserveTimeStatus.DELETED.getValue() + " where unit_ID = ?";
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
		String command = "update RESERVETIMES set status = " + ReserveTimeStatus.DELETED.getValue() + " where unit_ID = ? AND ID = ?";
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
	
	public static boolean setStatus(ReserveTime reserveTime, ReserveTimeStatus status)
	{
	    reserveTime.setStatus(status);
	    return updateReserveTime(reserveTime);
	}
	
	public static boolean setDuration(ReserveTime reserveTime, int duration)
	{
	    reserveTime.setDuration(duration);
	    return updateReserveTime(reserveTime);
	}

	public static boolean resetClientReserveData(ReserveTime reserveTime)
	{
	    reserveTime.setStatus(ReserveTimeStatus.RESERVABLE).setClientID(0).setResCodeID(null);
	    return updateReserveTime(reserveTime);
	}
	
	public static ReserveTime getReserveTime(int ID)
	{
		Connection conn = DBConnection.getConnection();
		ReserveTime reserveTime = new ReserveTime();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select * from RESERVETIMES where ID = " + ID;
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
		try
		{
			List<ReserveTime> reserveTimes = new ArrayList<>();
			Statement stmt = conn.createStatement();
			String command = "select * from RESERVETIMES"
					+ " where DAY_ID = " + dateID + " and UNIT_ID = " + unitID + " and (status != 5 && status != 4) ORDER BY ST_TIME";
			ResultSet rs=stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
			
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
	}
	
	public static Map<Enum, List<ReserveTime>> getClientUnitReserveTimeInADay(int dateID, int unitID)
	{
		Connection conn = DBConnection.getConnection();
		Map<Enum, List<ReserveTime>> reserveTimes = new HashMap<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select * from RESERVETIMES"
					+ " where DAY_ID = " + dateID + " and UNIT_ID = " + unitID + " and STATUS = 1 ORDER BY ST_TIME";
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);

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
	
	public static List<ReserveTime> getUnitReservedTimes(int unitID, int stDate)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ReserveTime> reserveTimes = new ArrayList<>();
			Statement stmt =conn.createStatement();
			String command="select * FROM RESERVETIMES"
					+ " where UNIT_ID = " + unitID + " AND DAY_ID >= "+ stDate +" AND STATUS = 2 ";
			ResultSet rs=stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
			
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
	}

	public static List<ReserveTime> getClientReservedTimes(int clientID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ReserveTime> reserveTimes = new ArrayList<ReserveTime>();
			Statement stmt =conn.createStatement();
			String command="select * from RESERVETIMES where status = 2 and Client_ID = " + clientID;
			ResultSet rs=stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
			
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
	}
	
	public static ReserveTime getReserveTimeFromID(int ID)
	{
		Connection conn = DBConnection.getConnection();
		ReserveTime reserveTime = new ReserveTime();
		try
		{
			Statement stmt =conn.createStatement();
			String command="select * from RESERVETIMES where ID=" + ID;
			ResultSet rs=stmt.executeQuery(command);
			while (rs.next()){
				fillReserveTime(rs, reserveTime);
			}
			return reserveTime;
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
	}
	
	public static ReserveTime getReserveTimeFromCode(String Code)
	{
		Connection conn = DBConnection.getConnection();
		ReserveTime reserveTime = new ReserveTime();
		try
		{
			Statement stmt =conn.createStatement();
			String command="select * from RESERVETIMES where Res_code_ID = " + Code;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				fillReserveTime(rs, reserveTime);
				return reserveTime;
			}
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
	
	public static List<Integer> getReserveTimeIDsFromMidday(int dateID, int unitID, int middayID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<Integer> resTimes = new ArrayList<>();
			Statement stmt =conn.createStatement();
			String command="select ID from RESERVETIMES where STATUS != 5 AND DAY_ID = " + dateID +
					" AND UNIT_ID = "+ unitID + " AND MIDDAY_ID = " + middayID + " ORDER BY ST_TIME";
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				resTimes.add(rs.getInt(1));
			}
			return resTimes;
			
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
	}
	
	public static List<ReserveTime> getReserveTimesForMiddayActions(int unitID, int stDate, int endDate, int middayID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ReserveTime>  reserveTimes = new ArrayList<>();
			Statement stmt =conn.createStatement();
			String command="select * from RESERVETIMES where STATUS != 5 AND UNIT_ID = " + unitID + " AND MIDDAY_ID =" + middayID
					+ " AND DAY_ID BETWEEN " + stDate + " AND " + endDate + " ORDER BY DAY_ID";
			ResultSet rs=stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
			
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
	}	
	
	public static List<ReserveTime> getReserveTimesForDayActions(int unitID, int stDate, int endDate)
	{
		Connection conn = DBConnection.getConnection();
		List<ReserveTime>  reserveTimes = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select * from RESERVETIMES where STATUS !=5 AND UNIT_ID = " + unitID + " AND DAY_ID BETWEEN "
			+ stDate + " AND " + endDate;
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return reserveTimes;
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
	
	public static ReserveTime getStartReserveTimeOfMidday(int unitID, int dateID, int middayID)
	{
		Connection conn = DBConnection.getConnection();
		ReserveTime reserveTime = new ReserveTime();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT * FROM RESERVETIMES WHERE STATUS !=5 AND UNIT_ID = " + unitID + " AND MIDDAY_ID =" + middayID + " AND DAY_ID =" + dateID +
					" ORDER BY ST_TIME LIMIT 1";
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTime(rs, reserveTime);
			return reserveTime;
			
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
	}
	
	public static ReserveTime getEndReserveTimeOfMidday(int unitID, int dateID, int middayID)
	{
		Connection conn = DBConnection.getConnection();
		ReserveTime reserveTime = new ReserveTime();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT * FROM RESERVETIMES WHERE STATUS ! = 5 AND UNIT_ID = " + unitID + " AND MIDDAY_ID =" + middayID + " AND DAY_ID =" + dateID
							+ " ORDER BY ST_TIME DESC LIMIT 1";
			ResultSet rs = stmt.executeQuery(command);
			fillSingleReserveTime(rs, reserveTime);
			return reserveTime;
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
	}

    /**
     * Getting reserve times in a unit between two days
     * @param unitID intended unit id
     * @param stDate start date
     * @param endDate end date
     * @return list of reserved times
     */
	public static List<ReserveTime> getUnitReservedtimesBetweenDays(int unitID, int stDate, int endDate)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ReserveTime> reserveTimes = new ArrayList<>();
			Statement stmt = conn.createStatement();
			String command = "SELECT * FROM RESERVETIMES WHERE "
					+ "STATUS = " + ReserveTimeStatus.RESERVED + " AND UNIT_ID = " + unitID + " AND DAY_ID BETWEEN " + stDate + " AND " + endDate;
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
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
	}

	private static void prepare(PreparedStatement preparedStatement, ReserveTime reserveTime){
		try {
			preparedStatement.setInt(1, reserveTime.getUnitID());
			preparedStatement.setInt(2, reserveTime.getDateID());
			preparedStatement.setInt(3, reserveTime.getMiddayID());
			preparedStatement.setObject(4, reserveTime.getStartTime());
			preparedStatement.setInt(5, reserveTime.getDuration());
			preparedStatement.setInt(6, reserveTime.getStatus().getValue());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillReserveTime(ResultSet resultSet, ReserveTime reserveTime){
		try{
			reserveTime.setID(resultSet.getInt(1));
			reserveTime.setUnitID(resultSet.getInt(2));
			reserveTime.setDateID(resultSet.getInt(3));
			reserveTime.setMiddayID(resultSet.getInt(4));
			reserveTime.setStartTime(resultSet.getObject(5, LocalTime.class));
			reserveTime.setDuration(resultSet.getInt(6));
			reserveTime.setStatus(ReserveTimeStatus.getByValue(resultSet.getInt(7)));
			reserveTime.setClientID(resultSet.getInt(8));
			reserveTime.setResCodeID(resultSet.getString(9));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillSingleReserveTime(ResultSet resultSet, ReserveTime reserveTime){
	    try {
	        while (resultSet.next()){
	            fillReserveTime(resultSet, reserveTime);
            }
        }catch (SQLException e){
	        Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

	private static void fillReserveTimes(ResultSet resultSet, List<ReserveTime> reserveTimes){
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

	private static void fillReserveTimes(ResultSet resultSet, Map<Enum, List<ReserveTime>> reserveTimes){
		try{
			List<ReserveTime> morningReserveTimes = new ArrayList<>();
			List<ReserveTime> afternoonReserveTimes = new ArrayList<>();
			while (resultSet.next()){
				ReserveTime reserveTime = new ReserveTime();
				fillReserveTime(resultSet, reserveTime);
				if (reserveTime.getMiddayID() == MiddayID.MORNING.getValue())
					morningReserveTimes.add(reserveTime);
				else if (reserveTime.getMiddayID() == MiddayID.AFTERNOON.getValue())
					afternoonReserveTimes.add(reserveTime);
			}
			reserveTimes.put(MiddayID.MORNING, morningReserveTimes);
			reserveTimes.put(MiddayID.AFTERNOON, afternoonReserveTimes);
		}catch(SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
}