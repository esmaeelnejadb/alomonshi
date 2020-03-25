package com.alomonshi.datalayer.dataaccess;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Users;

public class TableClient {

	/**
	 * Inserting new user
	 * Author Behzad
	 * @param user new user information
	 * @return true if executes properly
	 */
	public static boolean insert(Users user) {
		String command = "INSERT INTO CLIENTINFO (VERIFICATION_CODE," +
				" NAME," +
				" USERNAME," +
				" PASSWORD," +
				" PHONE," +
				" EMAIL," +
				" CLIENT_STAT," +
				" TOKEN," +
				" EXPIRATION_DATE," +
				" IS_ACTIVE) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(user, command);
	}

	/**
	 * updating user information
	 * @param user to be modified
	 * @return true if executes properly
	 */

	public static boolean update(Users user) {
		String command = "UPDATE CLIENTINFO SET" +
				" VERIFICATION_CODE = ?," +
				" NAME = ?," +
				" USERNAME = ?," +
				" PASSWORD = ?," +
				" PHONE = ?," +
				" EMAIL = ?," +
				" CLIENT_STAT = ?," +
				" TOKEN = ?," +
				" EXPIRATION_DATE = ?," +
				" IS_ACTIVE = ?" +
				" WHERE ID = "
				+ user.getClientID();
		return executeInsertUpdate(user, command);
	}


	/**
	 * deleting a user
	 * @param user to be deleted
	 * @return true if executes properly
	 */
	public static boolean delete(Users user){
		user.setActive(false);
		return update(user);
	}

	/**
	 * Executing insert or update a row of table user
	 * @param user to be inserted in database
	 * @param command to be executed
	 * @return true id query is executed correctly
	 */
	private static boolean executeInsertUpdate(Users user, String command)
	{
		Connection conn = DBConnection.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, user);
			return ps.executeUpdate() == 1;
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}finally {
			DBConnection.closeConnection(conn);
		}
	}

	/**
	 * Author Behzad
	 * Getting user by user id
	 * @return user
	 */
	public static Users getUser(int userID) {
		Connection conn = DBConnection.getConnection();
		Users user = new Users();
		try
		{
			String command = "SELECT" +
					" *" +
					" FROM" +
					" CLIENTINFO" +
					" WHERE" +
					" ID = " + userID;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillSingleUser(rs, user);
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally
		{
			DBConnection.closeConnection(conn);
		}
		return user;
	}

	/**
	 * Getting user with his/her phone number
	 * @param phoneNumber to be got user from
	 * @return user object
	 */
	public static Users getUser(String phoneNumber) {
		String command = "SELECT" +
				" *" +
				" FROM" +
				" CLIENTINFO" +
				" WHERE" +
				" PHONE = '" + phoneNumber + "'";
		Connection conn = DBConnection.getConnection();
		Users user = new Users();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillSingleUser(rs, user);
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally {
			DBConnection.closeConnection(conn);
		}
		return user;
	}

	/**
	 * Preparing statement object for executing update insert actions
	 * @param preparedStatement JDBC object
	 * @param user to be inserted or updated
	 */
	private static void prepare(PreparedStatement preparedStatement, Users user){
		try {
			preparedStatement.setInt(1, user.getVerificationCode());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getUsername());
			preparedStatement.setString(4, user.getPassword());
			preparedStatement.setString(5, user.getPhoneNo());
			preparedStatement.setString(6, user.getEmail());
			preparedStatement.setInt(7, user.getUserLevel().getValue());
			preparedStatement.setString(8, user.getToken());
			preparedStatement.setObject(9, user.getExpirationDate());
			preparedStatement.setBoolean(10, user.isActive());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
		}
	}

	/**
	 * filling user object with result returned from database
	 * @param resultSet JDBC object returned from database
	 * @param user to be filled
	 */
	private static void fillUser(ResultSet resultSet, Users user){
		try {
			user.setClientID(resultSet.getInt(1));
			user.setVerificationCode(resultSet.getInt(2));
			user.setName(resultSet.getString(3));
			user.setUsername(resultSet.getString(4));
			user.setPassword(resultSet.getString(5));
			user.setPhoneNo(resultSet.getString(6));
			user.setEmail(resultSet.getString(7));
			user.setUserLevel(UserLevels.getByValue(resultSet.getInt(8)));
			user.setToken(resultSet.getString(9));
			user.setExpirationDate(resultSet.getObject( 10, LocalDateTime.class ));
			user.setActive(resultSet.getBoolean(11));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
		}
	}

	/**
	 * Filling single user object
	 * @param resultSet JDBC object returned from database
	 * @param user to be filled
	 */
	private static void fillSingleUser(ResultSet resultSet, Users user) {
		try {
			while (resultSet.next()) {
				fillUser(resultSet, user);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
		}
	}
}
