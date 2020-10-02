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
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.tableobjects.Units;
import com.alomonshi.utility.DateTimeUtility;

public class TableUnit {

	public static int insertUnit(Units unit){
		String command = "INSERT INTO UNITS(" +
				" Comp_ID," +
				" unit_name," +
				" unit_step_time," +
				" IS_ACTIVE," +
				" PICTURE_URL," +
				" REMARK, " +
				" CREATE_DATE," +
				" UPDATE_DATE," +
				" REMOVE_DATE," +
				" ONLINE_RESERVE" +
				") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return executeInsert(unit, command);
	}

	public static boolean updateUnit(Units unit){
		String command = "UPDATE UNITS SET" +
				" Comp_ID = ?," +
				" unit_name = ?," +
				" unit_step_time = ?," +
				" IS_ACTIVE = ?," +
				" PICTURE_URL = ?," +
				" REMARK = ?," +
				" CREATE_DATE = ?," +
				" UPDATE_DATE = ?," +
				" REMOVE_DATE = ?," +
				" ONLINE_RESERVE = ?" +
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
	public static List<Units> getUnits(int companyID, boolean withServicesAndComments) {
		Connection conn = DBConnection.getConnection();
		List<Units> units = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" UNITS" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND Comp_ID = " + companyID;
			ResultSet rs = stmt.executeQuery(command);
			if (withServicesAndComments)
				fillUnitsWithServiceAndComments(rs, units);
			else
				fillUnitsWithoutServiceAndComments(rs, units);
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
		return units;
	}

	/**
	 * Getting unit from its id with services and comments
	 * @param unitID intended unit id
	 * @return unit object
	 */
	public static Units getUnit(int unitID, boolean withServicesAndComments)
	{
		Connection conn = DBConnection.getConnection();
		Units unit = new Units();
		try {
			Statement stmt =conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" UNITS" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND ID = " + unitID;
			ResultSet rs = stmt.executeQuery(command);
			if (withServicesAndComments)
				fillSingleUnitWithServicesAndComments(rs, unit);
			else
				fillSingleUnitWithoutServicesAndComments(rs, unit);
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
		return unit;
	}

	/**
	 * Getting unit from its id with services and comments
	 * @param unitID intended unit id
	 * @return unit object
	 */
	public static Units getUnitWithServices(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		Units unit = new Units();
		try {
			Statement stmt =conn.createStatement();
			String command = "SELECT " +
					" UNIT.*, COMP.*" +
					" FROM" +
					" UNITS UNIT" +
					" LEFT JOIN" +
					" COMPANIES COMP ON UNIT.COMP_ID = COMP.ID" +
					" AND COMP.IS_ACTIVE IS TRUE" +
					" WHERE" +
					" UNIT.ID = " + unitID +
					" AND UNIT.IS_ACTIVE IS TRUE";
			ResultSet rs = stmt.executeQuery(command);
			fillSingleUnitWithServices(rs, unit);
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
		return unit;
	}

	/**
	 * Getting company id of a unit
	 * @param unitID input
	 * @return company id of the unit
	 */
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
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next()) {
				companyID = rs.getInt(1);
			}
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
		return companyID;
	}

	/**
	 * Getting unit from its id for admin (some fields is required)
	 * @param managerID intended manager id
	 * @return unit object
	 */
	public static List<Units> getAdminUnit(int managerID)
	{
		Connection conn = DBConnection.getConnection();
		List<Units> units = new ArrayList<>();
		try
		{
			Statement stmt =conn.createStatement();
			String command = "SELECT" +
					" unit.ID AS unitID," +
					" unit.UNIT_NAME AS unitName" +
					" FROM" +
					" UNITS unit," +
					" adminunits au" +
					" WHERE" +
					" au.MNG_ID = " + managerID +
					" AND unit.ID = au.unit_id" +
					" AND au.IS_ACTIVE IS TRUE" +
					" AND unit.IS_ACTIVE IS TRUE;";
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				Units unit = new Units();
				unit.setID(rs.getInt("unitID"));
				unit.setUnitName(rs.getString("unitName"));
				units.add(unit);
			}
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
		return units;
	}

	/**
	 * prepare statement to be executed in database
	 * @param preparedStatement JDBC object to be prepared
	 * @param unit that JDBC object to be prepared with
	 */
	private static void prepare(PreparedStatement preparedStatement, Units unit){
		try {
			preparedStatement.setInt(1, unit.getCompanyID());
			preparedStatement.setString(2, unit.getUnitName());
			preparedStatement.setInt(3, unit.getUnitDuration());
			preparedStatement.setBoolean(4, unit.isActive());
			preparedStatement.setString(5, unit.getPictureURL());
			preparedStatement.setString(6, unit.getRemark());
			preparedStatement.setObject(7, unit.getCreateDate());
			preparedStatement.setObject(8, unit.getUpdateDate());
			preparedStatement.setObject(9, unit.getRemoveDate());
			preparedStatement.setBoolean(10, unit.isOnlineReserve());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Filling unit object got from database
	 * @param resultSet returned from JDBC
	 * @param unit to be filled
	 */

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
			unit.setOnlineReserve(resultSet.getBoolean(11));
		}catch(SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Filling company of unit object got from database
	 * @param resultSet returned from JDBC
	 * @param unit to be filled
	 */

	private static void fillCompany(ResultSet resultSet, Units unit){
		Company company = new Company();
		try {
			company.setID(resultSet.getInt(12));
			company.setCompanyCatID(resultSet.getInt(13));
			company.setCompanyName(resultSet.getString(14));
			company.setCompanyAddress(resultSet.getString(15));
			company.setCompanyPhoneNo1(resultSet.getString(16));
			company.setCompanyPhoneNo2(resultSet.getString(17));
			company.setCompanyPhoneNo3(resultSet.getString(18));
			company.setLocationLat(resultSet.getFloat(19));
			company.setLocationLon(resultSet.getFloat(20));
			company.setWebsite(resultSet.getString(21));
			company.setRate(resultSet.getFloat(22));
			company.setLogoURL(resultSet.getString(23));
			company.setLocality(resultSet.getString(24));
			company.setCityID(resultSet.getInt(25));
			company.setDistrictID(resultSet.getInt(26));
			company.setActive(resultSet.getBoolean(27));
			company.setCommercialCode(resultSet.getString(28));
			company.setPicURL(resultSet.getString(29));
			company.setCoverURL(resultSet.getString(30));
			unit.setCompany(company);
		}catch(SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill service and comments that should be got from other tables
	 * @param unit to be set
	 */
	private static void fillUnitServicesAndComments (Units unit) {
		unit.setServices(TableService.getUnitServices(unit.getID(),
				DateTimeUtility.getCurrentGregorianDate()));
		unit.setUnitComments(TableComment.getUnitComments(unit.getID()));
	}

	/**
	 * Fill service that should be got from other tables
	 * @param unit to be set
	 */
	private static void fillUnitServices (Units unit) {
		unit.setServices(TableService.getUnitServices(unit.getID(),
				DateTimeUtility.getCurrentGregorianDate()));
	}


	/**
	 * Fill single unit got from database with services and comments
	 * @param resultSet return from executing query
	 * @param unit returned filled object
	 */

	private static void fillSingleUnitWithServicesAndComments(ResultSet resultSet, Units unit) {
		try {
			while (resultSet.next()) {
				fillUnit(resultSet, unit);
				fillUnitServicesAndComments(unit);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill single unit got from database with services and comments
	 * @param resultSet return from executing query
	 * @param unit returned filled object
	 */

	private static void fillSingleUnitWithServices(ResultSet resultSet, Units unit) {
		try {
			while (resultSet.next()) {
				fillUnit(resultSet, unit);
				fillUnitServices(unit);
				fillCompany(resultSet, unit);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill single unit got from database without services and comments
	 * @param resultSet return from executing query
	 * @param unit returned filled object
	 */

	private static void fillSingleUnitWithoutServicesAndComments(ResultSet resultSet, Units unit) {
		try {
			while (resultSet.next()) {
				fillUnit(resultSet, unit);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill a list of units got from database with unit services and comments
	 * @param resultSet returned from JDBC
	 * @param units injected to be filed
	 */
	private static void fillUnitsWithServiceAndComments(ResultSet resultSet, List<Units> units){
		try{
			while (resultSet.next()){
				Units unit = new Units();
				fillUnit(resultSet, unit);
				fillUnitServicesAndComments(unit);
				units.add(unit);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill a list of units got from database without unit services and comments
	 * @param resultSet returned from JDBC
	 * @param units injected to be filed
	 */
	private static void fillUnitsWithoutServiceAndComments(ResultSet resultSet, List<Units> units){
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
