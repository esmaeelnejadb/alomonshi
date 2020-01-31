package com.alomonshi.datalayer.dataaccess;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.ClientReserveTime;

public class TableClientReserve {


	/**
	 * Author Behzad
	 * Inserting new reserve time
	 * @param reservetime intended reserve time to be inserted
	 * @return true if process has been done properly
	 */

	public boolean insertClientReserveTime(ClientReserveTime reservetime)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
			String command="insert into RESERVETABLE (client_ID, comp_ID, unit_ID, client_service_ID, date_ID, midday_ID,"
					+ " client_Restime_id, client_reserve_stat, client_reserve_code) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, reservetime.getClientID());
			ps.setInt(2, reservetime.getCompID());
			ps.setInt(3, reservetime.getUnitID());
			ps.setInt(4, reservetime.getServiceID());
			ps.setInt(5, reservetime.getDateID());
			ps.setInt(6, reservetime.getMiddayID());
			ps.setInt(7, reservetime.getReserveTimeID());
			ps.setInt(8, reservetime.getStatus());
			ps.setString(9, reservetime.getReserveCodeID());
			
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
	 * Author Behzad
	 * @param ID id of the reserved time
	 * @param status status of reserve time
	 * @return true if process has been done properly
	 */

	public boolean setReserveStatus(int ID , int status) {
		String command="update RESERVETABLE set client_reserve_stat=? where ID=?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, status);
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
	 * Author Behzad
	 * @param code intended reserve code
	 * @param ID id of the reserved time
	 * @return true if process has been done properly
	 */

	public boolean setReserveCode(String code, int ID) {
		String command="update RESERVETABLE set client_reserve_code=? where ID=?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, code);
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
	 * Author Behzad
	 * Getting client reserve time with reserve code
	 * @param code reserve code of the intended reserve time
	 * @return coient reserve time object
	 */

	public ClientReserveTime getClientReserveTimeWithCode(String code) {
		String command="select client_ID, comp_ID, unit_ID, client_service_ID, date_ID, midday_ID, client_Restime_id, "
				+ "client_reserve_stat, client_reserve_code"
				+ " from RESERVETABLE where client_reserve_stat = 1 AND  client_reserve_code="+code;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			ClientReserveTime cltrestime = new ClientReserveTime(); 
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				cltrestime.setClientID(rs.getInt(1));
				cltrestime.setCompID(rs.getInt(2));
				cltrestime.setUnitID(rs.getInt(3));
				cltrestime.setServiceID(rs.getInt(4));
				cltrestime.setDateID(rs.getInt(5));
				cltrestime.setMiddayID(rs.getInt(6));
				cltrestime.setReserveTimeID(rs.getInt(7));
				cltrestime.setStatus(rs.getInt(8));
				cltrestime.setReserveCodeID(rs.getString(9));
			}
			return cltrestime;
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

	/**
	 * Author Behzad
	 * @param clientID client id
	 * @return list of client reserved times
	 */

	public List<ClientReserveTime> getClientReserveTimes(int clientID) {
		String command="select client_ID, comp_ID, unit_ID, client_service_ID, date_ID, midday_ID, client_Restime_id,"
				+ " client_reserve_stat, client_reserve_code"
				+ " from RESERVETABLE where client_reserve_stat = 1 AND client_ID=" + clientID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ClientReserveTime> cltrestimes = new ArrayList<ClientReserveTime>(); 
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				ClientReserveTime cltrestime = new ClientReserveTime();
				cltrestime.setClientID(rs.getInt(1));
				cltrestime.setCompID(rs.getInt(2));
				cltrestime.setUnitID(rs.getInt(3));
				cltrestime.setServiceID(rs.getInt(4));
				cltrestime.setDateID(rs.getInt(5));
				cltrestime.setMiddayID(rs.getInt(6));
				cltrestime.setReserveTimeID(rs.getInt(7));
				cltrestime.setStatus(rs.getInt(8));
				cltrestime.setReserveCodeID(rs.getString(9));
				
			}
			return cltrestimes;
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

	/**
	 * Author Behzad
	 * @param unitID
	 * @param stdate
	 * @param enddate
	 * @param midday
	 * @return
	 */

	public List<ClientReserveTime> getReserveTimesMiddayBetweenDays(int unitID, int stdate, int enddate, int midday) {
		String command="select client_ID, comp_ID, unit_ID, client_service_ID, date_ID, midday_ID, client_Restime_id, client_reserve_stat"
				+ ", client_reserve_code" + " from RESERVETABLE where unit_ID = " + unitID + " AND midday_ID = " + midday 
				+ " date_ID between " + stdate + " AND " + enddate ;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<ClientReserveTime> cltrestimes = new ArrayList<ClientReserveTime>(); 
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				ClientReserveTime cltrestime = new ClientReserveTime();
				cltrestime.setClientID(rs.getInt(1));
				cltrestime.setCompID(rs.getInt(2));
				cltrestime.setUnitID(rs.getInt(3));
				cltrestime.setServiceID(rs.getInt(4));
				cltrestime.setDateID(rs.getInt(5));
				cltrestime.setMiddayID(rs.getInt(6));
				cltrestime.setReserveTimeID(rs.getInt(7));
				cltrestime.setStatus(rs.getInt(8));
				cltrestime.setReserveCodeID(rs.getString(9));
				
			}
			return cltrestimes;
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

	/**
	 * Author Behzad
	 * @param resCode intended reserved code
	 * @return id of intended reserved time
	 */

	public int getID(String resCode) {
		String command="select ID from RESERVETABLE where client_reserve_stat = 1 AND client_reserve_code="+resCode;
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
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
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
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}	
		}
		return 0;
	}
}
