package com.alomonshi.datalayer.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.Units;

public class TableUnit {

	public static boolean insertUnit(Units unit){
		String command = "insert into UNITS(Comp_ID, unit_name, unit_step_time, IS_ACTIVE, PICTURE_URL, REMARK) values(?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(unit, command);
	}

	public static boolean updateUnit(Units unit){
		String command = "update UNITS set Comp_ID = ?, unit_name = ?, unit_step_time = ?, IS_ACTIVE = ?, PICTURE_URL = ?, REMARK = ?";
		return executeInsertUpdate(unit, command);
	}

	public static boolean delete(Units unit){
		unit.setActive(false);
		return updateUnit(unit);
	}

	private static boolean executeInsertUpdate(Units unit, String command){
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, unit);
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
		
	public static boolean setName(String name, int unitID)
	{
		String command="update UNITS set unit_name = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, name);
			ps.setInt(2, unitID);			
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

	public static boolean setSteptime(String stepTime, int unitID)
	{
		String command="update UNITS set unit_step_time=? where ID=?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, stepTime);
			ps.setInt(2, unitID);
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
	

	public static List<Units> getUnits(int companyID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<Units> units=new ArrayList<>();
			Statement stmt =conn.createStatement();
			String command="select * from UNITS where  IS_ACTIVE is true and Comp_ID = " + companyID;
			ResultSet rs=stmt.executeQuery(command);
			fillUnits(rs, units);
			return units;
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
	
	public static List<String> getUnitNames(int companyID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<String> unitList = new ArrayList<String>();
			Statement stmt =conn.createStatement();
			String command="select unit_name from UNITS where IS_ACTIVE IS TRUE and Comp_ID = " + companyID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				unitList.add(rs.getString(1));
			}
			return unitList;
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
	
	public static List<Integer> getUnitIDs(int companyID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			List<Integer> unitlist=new ArrayList<Integer>();
			Statement stmt =conn.createStatement();
			String command="select id from UNITS where IS_ACTIVE IS TRUE and Comp_ID="+companyID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				unitlist.add(rs.getInt(1));
			}
			return unitlist;
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
	public static Units getUnit(int unitID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Units unit = new Units();			
			Statement stmt =conn.createStatement();
			String command="select * from UNITS where IS_ACTIVE IS TRUE and ID = " + unitID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				fillUnit(rs, unit);
				return unit;
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
	
	public static int getID(int companyID,String unitName)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select ID from UNITS where IS_ACTIVE IS TRUE and Comp_ID=" + companyID+" and unit_name = '" + unitName + "'";
			ResultSet rs = stmt.executeQuery(command);
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
	
	public static int getCompanyID(int unitID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select Comp_ID from UNITS where IS_ACTIVE IS TRUE and ID = " + unitID;
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
		
	public static String getName(int unitID)
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select unit_name from UNITS where ID = " + unitID;
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
	
	public static String getStepTime(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		String stepTime = "";
		try
		{
			Statement stmt = conn.createStatement();
			String command = "select unit_step_time from UNITS where ID = " + unitID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				stepTime = rs.getString(1);
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
		return stepTime;
	}

	private static void prepare(PreparedStatement preparedStatement, Units unit){
		try {
			preparedStatement.setInt(1, unit.getCompanyID());
			preparedStatement.setString(2, unit.getUnitName());
			preparedStatement.setInt(3, unit.getUnitDuration());
			preparedStatement.setBoolean(4, unit.getActive());
			preparedStatement.setString(5, unit.getPictureURL());
			preparedStatement.setString(6, unit.getRemark());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillUnit(ResultSet resultSet, Units unit){
		try {
			unit.setID(resultSet.getInt(1));
			unit.setCompanyID(resultSet.getInt(2));
			unit.setUnitName(resultSet.getString(3));
			unit.setUnitDuration(resultSet.getInt(4));
			unit.setActive(resultSet.getBoolean(5));
			unit.setPictureURL(resultSet.getString(6));
			unit.setRemark(resultSet.getString(7));
			unit.setServices(TableService.getUnitServices(unit.getID()));
			unit.setUnitComments(TableComment.getUnitComments(unit.getID()));
		}catch(SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillUnits(ResultSet resultSet, List<Units> units){
		try{
			while (resultSet.next()){
				Units unit = new Units();
				fillUnit(resultSet, unit);
				units.add(unit);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
}