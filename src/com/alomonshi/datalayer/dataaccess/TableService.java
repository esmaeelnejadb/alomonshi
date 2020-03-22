package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.Services;
import com.alomonshi.object.uiobjects.AdminReport;

public class TableService {

	/**
	 * Inserting a service into data base
	 * @param service to be inserted
	 * @return true if inserted correctly
	 */
	public static boolean insertService(Services service){
		Connection connection = DBConnection.getConnection();
		String insertCommand = "INSERT INTO SERVICES(" +
				"UNIT_ID" +
				", SERVICE_NAME" +
				", SERVICE_TIME" +
				", SERVICE_PRICE" +
				", IS_ACTIVE" +
				", REMARK " +
				", CREATE_DATE " +
				", UPDATE_DATE " +
				", REMOVE_DATE " +
				") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		boolean response = executeInsertUpdate(service, insertCommand, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Updating service into database
	 * @param service to be updated
	 * @return true if update truly
	 */
	public static boolean updateService(Services service){
		Connection connection = DBConnection.getConnection();
		String updateCommand = "UPDATE SERVICES SET" +
				" UNIT_ID = ?" +
				", SERVICE_NAME = ?" +
				", SERVICE_TIME = ?" +
				", SERVICE_PRICE = ?" +
				", IS_ACTIVE = ?" +
				", REMARK = ? " +
				", CREATE_DATE = ?" +
				", UPDATE_DATE = ?" +
				", REMOVE_DATE = ?" +
				" WHERE ID = " + service.getID();
		boolean response = executeInsertUpdate(service, updateCommand + service.getID(), connection);
		DBConnection.closeConnection(connection);
		return response;
	}


	/**
	 * Delete a service from database
	 * @param service to be deleted
	 * @return true if delete correctly
	 */
	public static boolean delete(Services service){
		service.setActive(false);
		return updateService(service);
	}

	/**
	 * Executing insert update
	 * @param service to be inserted or updated
	 * @param command related command
	 * @return update result
	 */
	private static boolean executeInsertUpdate(Services service, String command, Connection connection){
		try {
			PreparedStatement ps = connection.prepareStatement(command);
			prepare(ps, service);
			int i = ps.executeUpdate();
			return i == 1;
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}
	}

	/**
	 * Deleting services of a unit
	 * @param unitID which its services to be deleted
	 * @return true if executed truly
	 */
	public static boolean deleteUnitServices(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		try {
			String command = "UPDATE SERVICES" +
					" SET" +
					" REMOVE_DATE = now()," +
					" IS_ACTIVE = FALSE" +
					" WHERE" +
					" UNIT_ID = " + unitID;
			PreparedStatement ps = conn.prepareStatement(command);
			int i = ps.executeUpdate();
			return i >= 0;

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
	 * Getting service object from database
	 * @param serviceID to be got from database
	 * @return service object
	 */
	public static Services getService(int serviceID) {
		Services service = new Services();
		Connection conn = DBConnection.getConnection();
		try {
			Statement stmt =conn.createStatement();
			String command = "SELECT" +
					" s.*, sd.ID as discountID, sd.DISCOUNT as discount" +
					" FROM" +
					" services s" +
					" LEFT JOIN" +
					" servicediscount sd ON s.id = sd.service_id" +
					" AND s.IS_ACTIVE IS TRUE" +
					" AND sd.IS_ACTIVE IS TRUE" +
					" AND NOW() BETWEEN sd.CREATE_DATE AND sd.EXPIRE_DATE" +
					" WHERE" +
					" s.ID =" + serviceID;
			ResultSet rs = stmt.executeQuery(command);
			fillObject(rs, service);
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
		return service;
	}

	/**
	 * Get unit services
	 * @param unitID intended unit id
	 * @return list of services
	 */
	public static List<Services> getUnitServices(int unitID)
	{
		List<Services> services = new ArrayList<>();
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" s.*, sd.ID as discountID, sd.DISCOUNT as discount" +
					" FROM" +
					" services s" +
					" LEFT JOIN" +
					" servicediscount sd ON s.id = sd.service_id" +
					" AND s.IS_ACTIVE IS TRUE" +
					" AND sd.IS_ACTIVE IS TRUE" +
					" AND NOW() BETWEEN sd.CREATE_DATE AND sd.EXPIRE_DATE" +
					" WHERE" +
					" s.IS_ACTIVE IS TRUE AND s.UNIT_ID = " + unitID;
			ResultSet rs = stmt.executeQuery(command);
			fillServices(rs, services);
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
		return services;
	}

	/**
	 * Get unit service IDs
	 * @param unitID intended unit id
	 * @return list of service IDs
	 */
	public static List<Integer> getUnitServiceIDs(int unitID)
	{
		List<Integer> serviceIDs = new ArrayList<>();
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" ID AS id" +
					" FROM" +
					" SERVICES" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND unit_id = " + unitID;
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				serviceIDs.add(rs.getInt("id"));
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
		return serviceIDs;
	}

	public static List<AdminReport> getAdminReport (int unitID, int startDate, int endDate) {
		Connection conn = DBConnection.getConnection();
		List<AdminReport> adminReports = new ArrayList<>();
		try {
			String command = "SELECT" +
					" s.ID AS serviceID," +
					" s.SERVICE_NAME AS serviceName," +
					" SUM(s.SERVICE_TIME) / (" + startDate + " - " + endDate + " + 1) AS dayDuration," +
					" SUM(s.SERVICE_TIME) AS totalDuration," +
					" SUM(s.SERVICE_PRICE) / (" + startDate + " - " + endDate + " + 1) AS dayIncome," +
					" SUM(s.SERVICE_PRICE) AS totalIncome" +
					" FROM" +
					" units u" +
					" LEFT JOIN" +
					" services s ON u.id = s.unit_id AND s.is_active IS TRUE" +
					" LEFT JOIN" +
					" reservetimeservices rs ON s.id = rs.service_id" +
					" AND rs.is_active IS TRUE" +
					" LEFT JOIN" +
					" reservetimes rt ON rs.res_time_id = rt.id AND rt.status = "
					+ ReserveTimeStatus.RESERVED.getValue() +
					" AND rt.day_id BETWEEN "+ startDate +" AND " + endDate +
					" WHERE" +
					" u.id = " + unitID +
					" GROUP BY s.id" +
					" ORDER BY s.ID DESC";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillAdminReportList(rs, adminReports);
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
		return adminReports;
	}

	private static void prepare(PreparedStatement preparedStatement, Services service){
		try {
			preparedStatement.setInt(1, service.getUnitID());
			preparedStatement.setString(2, service.getServiceName());
			preparedStatement.setInt(3, service.getServiceDuration());
			preparedStatement.setInt(4, service.getServicePrice());
			preparedStatement.setBoolean(5, service.isActive());
			preparedStatement.setString(6, service.getRemark());
			preparedStatement.setObject(7, service.getCreateDate());
			preparedStatement.setObject(8, service.getUpdateDate());
			preparedStatement.setObject(9, service.getRemoveDate());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillService(ResultSet resultSet, Services service){
		try {
			service.setID(resultSet.getInt(1));
			service.setUnitID(resultSet.getInt(2));
			service.setServiceName(resultSet.getString(3));
			service.setServiceDuration(resultSet.getInt(4));
			service.setServicePrice(resultSet.getInt(5));
			service.setActive(resultSet.getBoolean(6));
			service.setRemark(resultSet.getString(7));
			service.setCreateDate(resultSet.getObject(8, LocalDateTime.class));
			service.setUpdateDate(resultSet.getObject(9, LocalDateTime.class));
			service.setRemoveDate(resultSet.getObject(10, LocalDateTime.class));
			service.setDiscountID(resultSet.getInt("discountID"));
			service.setDiscount(resultSet.getInt("discount"));
			service.setPictureURLs(TableServicePicture.getServicePictures(service.getID()));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill admin report object
	 * @param resultSet result from executing query got from JDBC
	 * @param adminReport to be filled object
	 */
	private static void fillAdminReport(ResultSet resultSet, AdminReport adminReport){
		try {
			adminReport.setServiceID(resultSet.getInt("serviceID"));
			adminReport.setServiceName(resultSet.getString("serviceName"));
			adminReport.setAverageDayHourWorking(resultSet.getString("dayDuration"));
			adminReport.setTotalHourWorking(resultSet.getString("totalDuration"));
			adminReport.setAverageDayIncome(resultSet.getInt("dayIncome"));
			adminReport.setTotalIncome(resultSet.getInt("totalIncome"));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fiulling admin report list got from database
	 * @param resultSet returned from JDBC
	 * @param adminReportList to be filled
	 */
	private static void fillAdminReportList (ResultSet resultSet,
											 List<AdminReport> adminReportList) {
		try {
			while (resultSet.next()){
				AdminReport adminReport = new AdminReport();
				fillAdminReport(resultSet,adminReport);
				adminReportList.add(adminReport);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillObject(ResultSet resultSet, Services service) {
		try {
			while (resultSet.next()) {
				fillService(resultSet, service);
			}
		}catch (SQLException e) {
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
