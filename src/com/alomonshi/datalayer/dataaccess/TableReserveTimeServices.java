package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;


public class TableReserveTimeServices {
	
	private int resTimeID;
	public TableReserveTimeServices()
	{}
	
	public TableReserveTimeServices setRestimeID(int resCodeID)
	{
		this.resTimeID = resCodeID;
		return this;
	}
	
	public boolean insertRestimeServ(int dateID, int serviceID, int unitID, int companyID, int clientID)
	{
		String command="insert into RESERVETIMESERVICES(RES_TIME_ID, DAY_ID, SERVICE_ID, UNIT_ID, COMP_ID, CLIENT_ID, STATUS) values(?, ?, ?, ?, ?, ?, ?)";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, resTimeID);
			ps.setInt(2, dateID);
			ps.setInt(3, serviceID);
			ps.setInt(4, unitID);
			ps.setInt(5, companyID);			
			ps.setInt(6, clientID);			
			ps.setInt(7	, 1);
			int i=ps.executeUpdate();
			return i==1?true:false;
			
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
	
	public boolean deleteRestimeServ()
	{
		String command="update RESERVETIMESERVICES set status = 5 where RES_TIME_ID=?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, resTimeID);
			int i=ps.executeUpdate();
			return i==1?true:false;
			
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

	public List<Integer> getService()
	{
		String command="select SERVICE_ID from RESERVETIMESERVICES where STATUS = 1 AND RES_TIME_ID = " + resTimeID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			List<Integer> serviceIDs = new ArrayList<Integer>();			
			while(rs.next())
			{
				serviceIDs.add(rs.getInt(1));				
			}
			return serviceIDs;
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
					return null;
				}
			}	
		}
	}
	
	public Integer getCompany()
	{
		String command="select COMP_ID from RESERVETIMESERVICES where STATUS = 1 AND RES_TIME_ID = " + resTimeID;
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
					return 0;
				}
			}	
		}
		return 0;
	}

	
	public LinkedHashSet<Integer> getClientInAService(int serviceID)
	{
		String command="select CLIENT_ID from RESERVETIMESERVICES where STATUS = 1 AND SERVICE_ID = " + serviceID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			LinkedHashSet<Integer> clientIDs = new LinkedHashSet<Integer>();			
			while(rs.next())
			{
				clientIDs.add(rs.getInt(1));				
			}
			return clientIDs;
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
					return null;
				}
			}	
		}
	}
	
	public LinkedHashSet<Integer> getClientInAUnit(int unitID)
	{
		String command="select CLIENT_ID from RESERVETIMESERVICES where STATUS = 1 AND UNIT_ID = " + unitID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			LinkedHashSet<Integer> clientIDs = new LinkedHashSet<Integer>();			
			while(rs.next())
			{
				clientIDs.add(rs.getInt(1));				
			}
			return clientIDs;
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
					return null;
				}
			}	
		}
	}
	
	public LinkedHashSet<Integer> getClientInACompany(int companyID)
	{
		String command="select CLIENT_ID from RESERVETIMESERVICES where STATUS = 1 AND COMP_ID = " + companyID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			LinkedHashSet<Integer> clientIDs = new LinkedHashSet<Integer>();	
			while(rs.next())
			{
				clientIDs.add(rs.getInt(1));				
			}
			return clientIDs;
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
					return null;
				}
			}	
		}
	}	
	
	public LinkedHashSet<Integer> getClientReservedTimesInAUnit(int unitID, int clientID)
	{
		String command="select RES_TIME_ID from RESERVETIMESERVICES where STATUS = 1 AND UNIT_ID = " + unitID + " AND CLIENT_ID = " + clientID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			LinkedHashSet<Integer> rescodeIDs = new LinkedHashSet<Integer>();	
			while(rs.next())
			{
				rescodeIDs.add(rs.getInt(1));				
			}
			return rescodeIDs;
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
					return null;
				}
			}	
		}
	}
	
	public LinkedHashSet<Integer> getClientReservedTimesInAService(int serviceID, int clientID)
	{
		String command="select RES_TIME_ID from RESERVETIMESERVICES where STATUS = 1 AND SERVICE_ID = " + serviceID + " AND CLIENT_ID = " + clientID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			LinkedHashSet<Integer> rescodeIDs = new LinkedHashSet<Integer>();	
			while(rs.next())
			{
				rescodeIDs.add(rs.getInt(1));				
			}
			return rescodeIDs;
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
					return null;
				}
			}	
		}
	}	

}
