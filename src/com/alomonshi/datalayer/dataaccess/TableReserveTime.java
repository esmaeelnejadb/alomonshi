package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.enums.MiddayID;
import com.alomonshi.server.AlomonshiServer;

public class TableReserveTime {

	public static boolean insertReserveTime(ReserveTime reserveTime){
		String command = "insert into RESERVETIMES(UNIT_ID, DAY_ID, MIDDAY_ID, ST_TIME, DURATION, STATUS) values(?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(reserveTime, command);
	}

	public static boolean updateReserveTime(ReserveTime reserveTime){
		String command = "update RESERVETIMES set UNIT_ID = ?, DAY_ID = ?, MIDDAY_ID = ?, ST_TIME = ?, DURATION = ?" +
				", STATUS = ? ";
		return executeInsertUpdate(reserveTime, command);
	}
	
	private static boolean executeInsertUpdate(ReserveTime reserveTime, String command)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, reserveTime);
			int i = ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
			Logger.getLogger(AlomonshiServer.class.getName()).log(Level.SEVERE, "Exception : " + e);
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
					e.printStackTrace();
					Logger.getLogger(AlomonshiServer.class.getName()).log(Level.SEVERE, "Exception" + e);
				}
			}	
		}
	}


	public static boolean deleteUnit(int unitID)
	{
		String command = "update RESERVETIMES set status = 5 where unit_ID = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, unitID);
			int i = ps.executeUpdate();
			return i == 1;

		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean deleteService(int unitID, int ID)
	{
		String command = "update RESERVETIMES set status = 5 where unit_ID = ? AND ID = ?";
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
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}
	
	public static boolean deleteReserveTime(int ID)
	{
		String command="update RESERVETIMES set status = 5 where id= ?" ;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, ID);
			int i=ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}
	
	public static boolean setStatus(int ID, int status)
	{
		String command="update RESERVETIMES set status=? where ID=?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, status);
			ps.setInt(2, ID);			
			int i = ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}
	
	public static boolean setDuration(int ID, Time duration)
	{
		String command="update RESERVETIMES set DURATION = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setTime(1, duration);
			ps.setInt(2, ID);
			int i = ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}
	
	public static boolean setClientReserveData(int ID, ReserveTime clientdata)
	{
		String command="update RESERVETIMES set status = ?, Client_ID = ?, Res_code_ID = ? where ID = " + ID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, clientdata.getStatus());
			ps.setInt(2, clientdata.getClientID());
			ps.setString(3, clientdata.getResCodeID());
			int i=ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}
	public static boolean resetClientReserveData(int ID)
	{
		String command="update RESERVETIMES set Client_ID = NULL, Res_code_ID = NULL where ID = " + ID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			int i = ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}
	
	public static int getStatus(int ID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select status from RESERVETIMES where ID = " + ID;
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
		return reserveTimes;
	}
	
	public static List<ReserveTime> getUnitReservedTimes(int unitID, int stDate)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ReserveTime> reserveTimes = new ArrayList<ReserveTime>();
			Statement stmt =conn.createStatement();
			String command="select * FROM RESERVETIMES"
					+ " where UNIT_ID = " + unitID + " AND DAY_ID >= "+ stDate +" AND STATUS = 2 ";
			ResultSet rs=stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
			List<Integer> resTimes = new ArrayList<Integer>();
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
			e.printStackTrace();
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
					e.printStackTrace();	
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
			String command = "SELECT * FROM RESERVETIMES WHERE STATUS !=5 AND UNIT_ID = " + unitID + " AND MIDDAY_ID =" + middayID + " AND DAY_ID =" + dateID
							+ " ORDER BY ST_TIME DESC LIMIT 1";
			ResultSet rs = stmt.executeQuery(command);
			fillReserveTime(rs, reserveTime);
			return reserveTime;
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}

	public static List<ReserveTime> getClientReservedListInAUnit(int unitID, int stDate, int endDate)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ReserveTime> reserveTimes = new ArrayList<ReserveTime>();
			Statement stmt =conn.createStatement();
			String command="SELECT * FROM RESERVETIMES WHERE "
					+ "STATUS = 2 AND UNIT_ID = " + unitID + " AND DAY_ID BETWEEN " + stDate + " AND " + endDate;
			ResultSet rs=stmt.executeQuery(command);
			fillReserveTimes(rs, reserveTimes);
			return reserveTimes;
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}

	private static void prepare(PreparedStatement preparedStatement, ReserveTime reserveTime){
		try {
			preparedStatement.setInt(1, reserveTime.getUnitID());
			preparedStatement.setInt(2, reserveTime.getDateID());
			preparedStatement.setInt(3, reserveTime.getMiddayID());
			preparedStatement.setTime(4, reserveTime.getStartTime());
			preparedStatement.setTime(5, reserveTime.getDuration());
			preparedStatement.setInt(6, reserveTime.getStatus());
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private static void fillReserveTime(ResultSet resultSet, ReserveTime reserveTime){
		try{
			reserveTime.setID(resultSet.getInt(1));
			reserveTime.setUnitID(resultSet.getInt(2));
			reserveTime.setDateID(resultSet.getInt(3));
			reserveTime.setMiddayID(resultSet.getInt(4));
			reserveTime.setStartTime(resultSet.getTime(5));
			reserveTime.setDuration(resultSet.getTime(6));
			reserveTime.setStatus(resultSet.getInt(7));
			reserveTime.setClientID(resultSet.getInt(8));
			reserveTime.setResCodeID(resultSet.getString(9));
		}catch (SQLException e){
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
}
