package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.Services;

public class TableService {

	public static boolean insertService(Services service){
		String command="insert into SERVICES(UNIT_ID, SERVICE_NAME, SERVICE_TIME, SERVICE_PRICE, IS_ACTIVE, REMARK ) values(?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(service, command);
	}

	public static boolean updateService(Services service){
		String command="update SERVICES set UNIT_ID = ?, SERVICE_NAME = ?, SERVICE_TIME = ?, SERVICE_PRICE = ?, IS_ACTIVE = ?, REMARK = ? " +
				", IS_ACTIVE = ?";
		return executeInsertUpdate(service, command);
	}


	public static boolean delete(Services service){
		service.setIsActive(false);
		return updateService(service);
	}

	private static boolean executeInsertUpdate(Services service, String command) {
		Connection conn = DBConnection.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, service);
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
	
	public static boolean deleteUnitServices(int unitID)
	{
		String command = "UPDATE SERVICES SET IS_ACTIVE = false where unit_id = ? ";
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

	public static boolean setTime(String time, int serviceID)
	{
		String command="update SERVICES set service_time = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, time);
			ps.setInt(2, serviceID);			
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
	
	public static boolean setPrice(int price, int serviceID)
	{
		String command="update SERVICES set service_price = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, price);
			ps.setInt(2, serviceID);			
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

	public static boolean setStatus(boolean status, int serviceID)
	{
		String command="update SERVICES set IS_ACTIVE = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setBoolean(1, status);
			ps.setInt(2, serviceID);			
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

	
	public static int getID(String service_name, int unitID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select ID from SERVICES where IS_ACTIVE is true AND unit_id = " + unitID + " and service_name = '" + service_name + "'";
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
	
	public static int getUnitID(int serviceID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select UNIT_ID from SERVICES where ID = " + serviceID;
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
	
	public static int getStatus(int serviceID) {
		Connection conn = DBConnection.getConnection(); 
		try {
			Statement stmt =conn.createStatement();
			String command="select IS_ACTIVE from SERVICES where ID = " + serviceID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next()) {
				return rs.getInt(1);
			}
			
		}catch(SQLException e) {
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
	
	public static String getName(int serviceID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select service_name from SERVICES where ID = " + serviceID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getString(1);
			}
			
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
		return null;
	}
	
	public static String getTime(int serviceID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command = "select service_time from SERVICES where ID = "+serviceID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getString(1);
			}
			
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
		return null;
	}
	
	public static int getPrice(int serviceID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command = "select service_price from SERVICES where ID = " + serviceID;
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

	public static Services getService(int serviceID) {
		Services service = new Services();
		Connection conn = DBConnection.getConnection();
		try {
			Statement stmt =conn.createStatement();
			String command="select * from SERVICES where IS_ACTIVE is true AND ID = " + serviceID;
			ResultSet rs=stmt.executeQuery(command);
			fillService(rs, service);
			return service;
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
	
	public static List<Services> getUnitServices(int unitID)
	{
		List<Services> services = new ArrayList<>();
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select * from SERVICES where IS_ACTIVE is true AND unit_id = " + unitID;
			ResultSet rs=stmt.executeQuery(command);
			fillServices(rs, services);
			return services;
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
	
	public static Set<Integer> getSearchedServices(String serviceName)
	{
		Set<Integer> unitIDs = new LinkedHashSet<>();
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select UNIT_ID from SERVICES where IS_ACTIVE is true AND SERVICE_NAME like '%" + serviceName + "%'";
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				int unitID = rs.getInt(1);
				unitIDs.add(unitID);
			}
			return unitIDs;
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
	
	public static List<Integer> getUnitServicesIDs(int unitID) {
		List<Integer> services = new ArrayList<>();
		Connection conn = DBConnection.getConnection(); 
		try {
			Statement stmt =conn.createStatement();
			String command="select ID from SERVICES where IS_ACTIVE is true AND unit_id = " + unitID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				services.add(rs.getInt(1));
			}
			return services;
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

	public static boolean isServiceExisted(int serviceID, int unitID) {
		Connection conn = DBConnection.getConnection(); 
		try {
			Statement stmt =conn.createStatement();
			String command="select ID from SERVICES where IS_ACTIVE is true AND unit_id = " + unitID + " and ID = " + serviceID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1) != 0;
			}
			
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
		return false;
	}

	private static void prepare(PreparedStatement preparedStatement, Services service){
		try {
			preparedStatement.setInt(1, service.getUnitID());
			preparedStatement.setString(2, service.getServiceName());
			preparedStatement.setInt(3, service.getServiceTime());
			preparedStatement.setInt(4, service.getServicePrice());
			preparedStatement.setBoolean(5, service.isActive());
			preparedStatement.setString(6, service.getRemark());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
	private static void fillService(ResultSet resultSet, Services service){
		try {
			service.setID(resultSet.getInt(1));
			service.setUnitID(resultSet.getInt(2));
			service.setServiceName(resultSet.getString(3));
			service.setServiceTime(resultSet.getInt(4));
			service.setServicePrice(resultSet.getInt(5));
			service.setIsActive(resultSet.getBoolean(6));
			service.setRemark(resultSet.getString(7));
			service.setPictureURLs(TableServicePicture.getServicePictures(service.getID()));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
	private static void fillServices(ResultSet resultSet, List<Services> services){
		try {
			while (resultSet.next()){
				Services service = new Services();
				fillService(resultSet,service);
				services.add(service);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

}
