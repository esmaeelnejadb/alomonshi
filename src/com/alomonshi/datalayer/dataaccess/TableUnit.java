package com.alomonshi.datalayer.dataaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.Units;

public class TableUnit {

	public static int insertUnit(Units unit){
		String command = "insert into UNITS(Comp_ID" +
				", unit_name" +
				", unit_step_time" +
				", IS_ACTIVE" +
				", PICTURE_URL" +
				", REMARK " +
				", CREATE_DATE" +
				", UPDATE_DATE" +
				", REMOVE_DATE" +
				") values(?, ?, ?, ?, ?, ?, ?, ?, ? )";
		return executeInsert(unit, command);
	}

	public static boolean updateUnit(Units unit){
		String command = "update UNITS set" +
				" Comp_ID = ?" +
				", unit_name = ?" +
				", unit_step_time = ?" +
				", IS_ACTIVE = ?" +
				", PICTURE_URL = ?" +
				", REMARK = ?" +
				", CREATE_DATE = ?" +
				", UPDATE_DATE = ?" +
				", REMOVE_DATE = ?" +
				" WHERE ID = " + unit.getID();

		return executeUpdate(unit, command);
	}

	public static boolean delete(Units unit){
		unit.setActive(false);
		return updateUnit(unit);
	}

	/**
	 * Executing insert update function
	 * @param unit to be processed
	 * @param command to be executed
	 * @return inserted ID
	 */

	private static int executeInsert(Units unit, String command){
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command, Statement.RETURN_GENERATED_KEYS);
			prepare(ps, unit);
			int i = ps.executeUpdate();

			if (i == 0) {
				return 0;
			}else {
				try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						return generatedKeys.getInt(1);
					}
					else {
						Logger.getLogger("Exception").log(Level.SEVERE, "Cannot get inserted unit id ");
					}
				}
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
		return 0;
	}

	/**
	 * Executing update
	 * @param unit to be updated
	 * @param command update command
	 * @return update result
	 */
	private static boolean executeUpdate(Units unit, String command){
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, unit);
			int i = ps.executeUpdate();
			return i >= 1;

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
	 * Getting list of units in a company
	 * @param companyID to be drived from
	 * @return list of units
	 */
	public static List<Units> getUnits(int companyID)
	{
		Connection conn = DBConnection.getConnection();
		List<Units> units = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" UNITS" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND Comp_ID = " + companyID;
			ResultSet rs = stmt.executeQuery(command);
			fillUnits(rs, units);
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
		return units;
	}

	/**
	 * Getting unit from its id
	 * @param unitID intended unit id
	 * @return unit object
	 */
	public static Units getUnit(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		Units unit = new Units();
		try
		{
			Statement stmt =conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" UNITS" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND ID = " + unitID;
			ResultSet rs = stmt.executeQuery(command);
			fillSingleUnit(rs, unit);
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
		return unit;
	}

	public static int getCompanyID(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		int companyID = 0;
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" Comp_ID" +
					" FROM" +
					" UNITS" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND ID = " + unitID;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				companyID = rs.getInt(1);
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
		return companyID;
	}

	private static void prepare(PreparedStatement preparedStatement, Units unit){
		try {
			preparedStatement.setInt(1, unit.getCompanyID());
			preparedStatement.setString(2, unit.getUnitName());
			preparedStatement.setInt(3, unit.getUnitDuration());
			preparedStatement.setBoolean(4, unit.getActive());
			preparedStatement.setString(5, unit.getPictureURL());
			preparedStatement.setString(6, unit.getRemark());
			preparedStatement.setObject(7, unit.getCreateDate());
			preparedStatement.setObject(8, unit.getUpdateDate());
			preparedStatement.setObject(9, unit.getRemoveDate());
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
			unit.setCreateDate(resultSet.getObject(8, LocalDateTime.class));
			unit.setUpdateDate(resultSet.getObject(9, LocalDateTime.class));
			unit.setRemoveDate(resultSet.getObject(10, LocalDateTime.class));
			unit.setServices(TableService.getUnitServices(unit.getID()));
			unit.setUnitComments(TableComment.getUnitComments(unit.getID()));
		}catch(SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill single unit got from database
	 * @param resultSet return from executing query
	 * @param unit returned filled object
	 */

	private static void fillSingleUnit(ResultSet resultSet, Units unit) {
		try {
			while (resultSet.next()) {
				fillUnit(resultSet, unit);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill a list of units got from database
	 * @param resultSet returned from JDBC
	 * @param units injected to be filed
	 */
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