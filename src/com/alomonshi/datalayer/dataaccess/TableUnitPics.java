package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.entity.UnitPicture;

public class TableUnitPics {


	public static boolean insertUnitPic(UnitPicture unitPicture){
		String command = "insert into UNITPICS (UNIT_ID, SERVICE_ID, URL, CAPTION, DATE, IS_ACTIVE) values(?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(unitPicture, command);
	}

	public static boolean updateUnitPic(UnitPicture unitPicture){
		String command = "update UNITPICS set UNIT_ID = ?, SERVICE_ID = ?, URL = ?, CAPTION = ?" +
				", DATE = ?, IS_ACTIVE = ?";
		return executeInsertUpdate(unitPicture, command);
	}

	public static boolean delete(UnitPicture unitPicture){
		unitPicture.setIsActive(false);
		return updateUnitPic(unitPicture);
	}

	private static boolean executeInsertUpdate(UnitPicture unitPic, String command)
	{		
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, unitPic);
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

	
	public static List<UnitPicture> getUnitPictures(int unitID) {
		String command="select * from UNITPICS where UNIT_ID = " + unitID + " AND IS_ACTIVE IS TRUE";
		Connection conn = DBConnection.getConnection();
		List<UnitPicture> unitPictures = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillUnitPictures(rs, unitPictures);
			return unitPictures;
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
	
	public static String getURL(int unitID) {
		String command = "select URL from UNITPICS where UNIT_ID = " + unitID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getString(1);			
			}
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
		return null;
	}
	
	public static boolean getStatus(int unitID) {
		String command="select IS_ACTIVE from UNITPICS where UNIT_ID =" + unitID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getBoolean(1);
			}
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
		return false;
	}
	
	public static boolean setURL(String url, int unitID)
	{
		String command="update UNITPICS set URL = ? where UNIT_ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, url);
			ps.setInt(2, unitID);
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
	
	public static boolean setStatus(boolean status, int unitID)
	{
		String command="update UNITPICS set IS_ACTIVE = ? where UNIT_ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setBoolean(1, status);
			ps.setInt(2, unitID);
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

	private static void prepare(PreparedStatement preparedStatement, UnitPicture unitPicture){
		try{
			preparedStatement.setInt(1, unitPicture.getUnitID());
			preparedStatement.setInt(2, unitPicture.getServiceID());
			preparedStatement.setString(3, unitPicture.getPicURL());
			preparedStatement.setString(4, unitPicture.getCaption());
			preparedStatement.setDate(5, unitPicture.getDate());
			preparedStatement.setBoolean(6, unitPicture.getIsActive());
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private static void fillUnitPicture(ResultSet resultSet, UnitPicture unitPicture){
		try {
			unitPicture.setID(resultSet.getInt(1));
			unitPicture.setUnitID(resultSet.getInt(2));
			unitPicture.setServiceID(resultSet.getInt(3));
			unitPicture.setPicURL(resultSet.getString(4));
			unitPicture.setCaption(resultSet.getString(5));
			unitPicture.setDate(resultSet.getDate(6));
			unitPicture.setIsActive(resultSet.getBoolean(7));
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private static void fillUnitPictures(ResultSet resultSet, List<UnitPicture> unitPictures){
		try {
			while(resultSet.next())
			{
				UnitPicture unitPicture = new UnitPicture();
				fillUnitPicture(resultSet, unitPicture);
				unitPictures.add(unitPicture);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
}