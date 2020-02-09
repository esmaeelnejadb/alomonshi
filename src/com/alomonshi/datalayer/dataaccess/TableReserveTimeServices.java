package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.ReserveTime;
import com.alomonshi.object.tableobjects.ReserveTimeServices;
import com.alomonshi.object.tableobjects.Services;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TableReserveTimeServices {

	private static final String insertCommand = "insert into RESERVETIMESERVICES(RES_TIME_ID, SERVICE_ID, UNIT_ID, CLIENT_ID, IS_ACTIVE)" +
			" values(?, ?, ?, ?, ?)";
	private static final String updateCommand = "update RESERVETIMESERVICES set " +
			"RES_TIME_ID = ?, SERVICE_ID = ?, UNIT_ID = ?, CLIENT_ID = ?, IS_ACTIVE = ?" +
			" where id = ";
	/**
	 * Insert a reserve time service object into database
	 * @param reserveTimeService to be injected into database
	 * @return true id correctly br inserted
	 */
	public static boolean insert(ReserveTimeServices reserveTimeService){
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(reserveTimeService, connection, insertCommand);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Insert a list of reserve time services
	 * @param reserveTimeServices to be inserted
	 * @return true if ok
	 */

	public static synchronized boolean insertList(List<ReserveTimeServices> reserveTimeServices) {
		Connection connection = DBConnection.getConnection();
		for (ReserveTimeServices reserveTimeService: reserveTimeServices) {
			if (!executeInsertUpdate(reserveTimeService, connection, insertCommand)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * update a reserve time service object into database
	 * @param reserveTimeService to be injected into database
	 * @return true id correctly br updated
	 */
	public static boolean update(ReserveTimeServices reserveTimeService) {
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(reserveTimeService, connection, updateCommand + reserveTimeService.getId());
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Update a list of reserve time services
	 * @param reserveTimeServices to be updated
	 * @return true if ok
	 */

	public static synchronized boolean updateList(List<ReserveTimeServices> reserveTimeServices) {
		Connection connection = DBConnection.getConnection();
		for (ReserveTimeServices reserveTimeService: reserveTimeServices) {
			if (!executeInsertUpdate(reserveTimeService, connection
					, updateCommand + reserveTimeService.getId())) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * Delete a reserve time services
	 * @param reserveTimeServices object to be deleted
	 * @return true if delete correctly
	 */

	public static boolean delete(ReserveTimeServices reserveTimeServices){
		reserveTimeServices.setActive(false);
		return update(reserveTimeServices);
	}

	/**
	 * Delete a list of reserve time services
	 * @param reserveTimeServices to be deleted
	 * @return true if ok
	 */

	public static synchronized boolean deleteList(List<ReserveTimeServices> reserveTimeServices) {
		Connection connection = DBConnection.getConnection();
		for (ReserveTimeServices reserveTimeService: reserveTimeServices) {
			reserveTimeService.setActive(false);
			if (!executeInsertUpdate(reserveTimeService, connection
					, updateCommand + reserveTimeService.getId())) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	public static boolean deleteReserveTimeServices(ReserveTime reserveTime) {
		Connection connection = DBConnection.getConnection();
		String command = "update RESERVETIMESERVICES set IS_ACTIVE = false where RES_TIME_ID = ?";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(command);
			preparedStatement.setInt(1, reserveTime.getID());
			return preparedStatement.executeUpdate() > 0;
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally {
			DBConnection.closeConnection(connection);
		}
		return false;
	}


	private static boolean executeInsertUpdate(ReserveTimeServices reserveTimeService, Connection connection, String command) {
		try {
			PreparedStatement ps = connection.prepareStatement(command);
			prepare(ps, reserveTimeService);
			int i = ps.executeUpdate();
			return i >= 1;
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}
	}
	

	public static List<Services> getServices(int reserveTimeID)
	{
		String command = "SELECT " +
				"    rs.SERVICE_ID AS serviceID," +
				"    s.SERVICE_NAME AS serviceName," +
				"    s.SERVICE_TIME AS duration," +
				"    s.SERVICE_PRICE AS cost" +
				" FROM" +
				"    alomonshi.reservetimeservices rs," +
				"    alomonshi.services s " +
				"WHERE" +
				"    rs.is_active IS TRUE" +
				"        AND rs.service_id = s.id" +
				"        AND rs.res_time_id = " + reserveTimeID +
				" order by rs.ID desc";
		Connection conn = DBConnection.getConnection();
		List<Services> services = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
		 	fillServiceList(rs, services);
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
		return services;
	}

	public static List<ReserveTimeServices> getClientInAService(int serviceID)
	{
		String command = "select * from RESERVETIMESERVICES where IS_ACTIVE is true AND SERVICE_ID = " + serviceID;
		Connection conn = DBConnection.getConnection();
		List<ReserveTimeServices> reserveTimeServices = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillObjectList(rs, reserveTimeServices);
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
		return reserveTimeServices;
	}
	
	public static List<ReserveTimeServices> getReservedServicesInAUnit(int unitID)
	{
		String command = "select * from RESERVETIMESERVICES where IS_ACTIVE is true AND UNIT_ID = " + unitID;
		Connection conn = DBConnection.getConnection();
		List<ReserveTimeServices> reserveTimeServices = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillObjectList(rs, reserveTimeServices);
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
		return reserveTimeServices;
	}

	
	public List<ReserveTimeServices> getClientReservedServicesInAUnit(int unitID, int clientID)
	{
		String command="select * from RESERVETIMESERVICES where IS_ACTIVE is true AND UNIT_ID = " + unitID + " AND CLIENT_ID = " + clientID;
		Connection conn = DBConnection.getConnection();
		List<ReserveTimeServices> reserveTimeServices = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillObjectList(rs, reserveTimeServices);
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
		return reserveTimeServices;
	}
	
	public List<ReserveTimeServices> getClientReservedTimesInAService(int serviceID, int clientID)
	{
		String command = "select RES_TIME_ID from RESERVETIMESERVICES where IS_ACTIVE is true AND SERVICE_ID = " + serviceID + " AND CLIENT_ID = " + clientID;
		Connection conn = DBConnection.getConnection();
		List<ReserveTimeServices> reserveTimeServices = new ArrayList<>();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillObjectList(rs, reserveTimeServices);
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
		return reserveTimeServices;
	}

	public static void prepare(PreparedStatement preparedStatement, ReserveTimeServices reserveTimeService) {
		try {
			preparedStatement.setInt(1, reserveTimeService.getReserveTimeID());
			preparedStatement.setInt(2, reserveTimeService.getServiceID());
			preparedStatement.setInt(3, reserveTimeService.getUnitID());
			preparedStatement.setInt(4, reserveTimeService.getClientID());
			preparedStatement.setBoolean(5, reserveTimeService.isActive());
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Error in preparation statement " + e);
		}
	}

	private static void fillReserveTimeServices(ResultSet resultSet, ReserveTimeServices reserveTimeService){
		try {
			reserveTimeService.setId(resultSet.getInt(1));
			reserveTimeService.setReserveTimeID(resultSet.getInt(2));
			reserveTimeService.setServiceID(resultSet.getInt(3));
			reserveTimeService.setUnitID(resultSet.getInt(4));
			reserveTimeService.setActive(resultSet.getBoolean(5));
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Error in filling result  " + e);
		}
	}

	private static void fillSingleObject(ResultSet resultSet, ReserveTimeServices reserveTimeService) {
		try {
			while (resultSet.next()) {
				fillReserveTimeServices(resultSet, reserveTimeService);
			}
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Error in filling result  " + e);
		}
	}

	private static void fillObjectList(ResultSet resultSet, List<ReserveTimeServices> reserveTimeServices) {
		try {
			while (resultSet.next()) {
				ReserveTimeServices reserveTimeService = new ReserveTimeServices();
				fillReserveTimeServices(resultSet, reserveTimeService);
				reserveTimeServices.add(reserveTimeService);
			}
		}catch (SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Error in filling result  " + e);
		}
	}

	private static void fillService(ResultSet resultSet, Services service){
		try {
			service.setID(resultSet.getInt("serviceID"));
			service.setServiceName(resultSet.getString("serviceName"));
			service.setServiceDuration(resultSet.getInt("duration"));
			service.setServicePrice(resultSet.getInt("cost"));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillServiceList(ResultSet resultSet, List<Services> services){
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