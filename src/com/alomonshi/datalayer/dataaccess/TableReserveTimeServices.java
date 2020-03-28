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

	private static final String insertCommand = "INSERT INTO RESERVETIMESERVICES(" +
			"RES_TIME_ID," +
			" SERVICE_ID," +
			" UNIT_ID," +
			" CLIENT_ID," +
			" IS_ACTIVE," +
			" SERVICE_PRICE," +
			" DISCOUNT_ID" +
			")" +
			" VALUES(?, ?, ?, ?, ?, ?, ?)";
	private static final String updateCommand = "UPDATE RESERVETIMESERVICES SET " +
			"RES_TIME_ID = ?, " +
			"SERVICE_ID = ?," +
			" UNIT_ID = ?," +
			" CLIENT_ID = ?," +
			" IS_ACTIVE = ?," +
			" SERVICE_PRICE = ?" +
			" DISCOUNT_ID = ?" +
			" WHERE ID = ";
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

	public static boolean insertList(List<ReserveTimeServices> reserveTimeServices) {
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

	public static boolean updateList(List<ReserveTimeServices> reserveTimeServices) {
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

	public static boolean deleteList(List<ReserveTimeServices> reserveTimeServices) {
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

	/**
	 * Getting reserved services
	 * @param reserveTimeID to be got services
	 * @return list of returned services
	 */
	static List<Services> getServices(int reserveTimeID) {
		String command = "SELECT " +
				" rs.SERVICE_ID AS serviceID," +
				" s.SERVICE_NAME AS serviceName," +
				" s.SERVICE_TIME AS duration," +
				" s.SERVICE_PRICE AS cost" +
				" FROM" +
				" alomonshi.reservetimeservices rs," +
				" alomonshi.services s " +
				" WHERE" +
				" rs.is_active IS TRUE" +
				" AND rs.service_id = s.id" +
				" AND rs.res_time_id = " + reserveTimeID +
				" order by rs.ID desc";
		Connection conn = DBConnection.getConnection();
		List<Services> services = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
		 	fillServiceList(rs, services);
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return null;
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}	
		}
		return services;
	}

	public static void prepare(PreparedStatement preparedStatement, ReserveTimeServices reserveTimeService) {
		try {
			preparedStatement.setInt(1, reserveTimeService.getReserveTimeID());
			preparedStatement.setInt(2, reserveTimeService.getServiceID());
			preparedStatement.setInt(3, reserveTimeService.getUnitID());
			preparedStatement.setInt(4, reserveTimeService.getClientID());
			preparedStatement.setBoolean(5, reserveTimeService.isActive());
			preparedStatement.setInt(6, reserveTimeService.getServicePrice());
			preparedStatement.setInt(7, reserveTimeService.getDiscountID());
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
			reserveTimeService.setServicePrice(resultSet.getInt(6));
			reserveTimeService.setDiscountID(resultSet.getInt(7));
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