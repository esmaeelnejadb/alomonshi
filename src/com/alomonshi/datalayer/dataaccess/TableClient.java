package com.alomonshi.datalayer.dataaccess;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.entity.Users;

public abstract class TableClient {

	/**
	 * Inserting new user
	 * Author Behzad
	 * @param user new user information
	 * @return true if executes properly
	 */
	public static boolean insert(Users user) {
		String command = "insert into CLIENTINFO (USER_ID, VERIFICATION_CODE, NAME, USERNAME, PASSWORD, PHONE, EMAIL," +
				" CLIENT_STAT, TOKEN, EXPIRATION_DATE, IS_ACTIVE) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(user, command);
	}

	/**
	 * updating user information
	 * @param user to be modified
	 * @return true if executes properly
	 */

	public static boolean update(Users user) {
		String command = "update CLIENTINFO set USER_ID = ?, VERIFICATION_CODE = ?, NAME = ?, USERNAME = ?, PASSWORD = ?" +
				", PHONE = ?, EMAIL = ?, CLIENT_STAT = ?, TOKEN = ?, EXPIRATION_DATE = ?, IS_ACTIVE = ? WHERE ID = " + user.getID();
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
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, user);
			return ps.executeUpdate() == 1;
			
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

	/**
	 * Author Behzad
	 * Getting user by user id
	 * @return user
	 */
	
	public static Users getUser(int userID) {
		String command="select * from CLIENTINFO where USER_ID = " + userID ;
		Connection conn = DBConnection.getConnection();
		Users user = new Users();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				fillUser(rs, user);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
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
		return user;
	}

	public static Users getUser(String phoneNumber) {
		String command = "select * from CLIENTINFO where PHONE = '" + phoneNumber + "'";
		Connection conn = DBConnection.getConnection();
		Users user = new Users();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next())
			{
				fillUser(rs, user);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
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
		return user;
	}

	public static boolean isUserIDUnique(int newUserID) {
		String command = "select 1 ID from CLIENTINFO where USER_ID = " + newUserID;
		Connection conn = DBConnection.getConnection();
		int count = 0;
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next())
			{
				count ++;
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
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
		return count == 0;
	}

	/**
	 * Author Behzad
	 * Getting user by user id
	 * @return user
	 */

	public static boolean deleteUserTemporary(String phoneNumber) {
		String command="delete from CLIENTINFO where PHONE = '" + phoneNumber + "'" ;
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			return ps.executeUpdate() == 1;
		}catch(SQLException e)
		{
			e.printStackTrace();
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

	private static void prepare(PreparedStatement preparedStatement, Users user){
		try {
			preparedStatement.setInt(1, user.getUserID());
			preparedStatement.setInt(2, user.getVerificationCode());
			preparedStatement.setString(3, user.getName());
			preparedStatement.setString(4, user.getUsername());
			preparedStatement.setString(5, user.getPassword());
			preparedStatement.setString(6, user.getPhoneNo());
			preparedStatement.setString(7, user.getEmail());
			preparedStatement.setInt(8, user.getUserLevel());
			preparedStatement.setString(9, user.getToken());
			preparedStatement.setObject(10, user.getExpirationDate());
			preparedStatement.setBoolean(11, user.isActive());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
		}
	}

	private static void fillUser(ResultSet resultSet, Users user){
		try {
			user.setID(resultSet.getInt(1));
			user.setUserID(resultSet.getInt(2));
			user.setVerificationCode(resultSet.getInt(3));
			user.setName(resultSet.getString(4));
			user.setUsername(resultSet.getString(5));
			user.setPassword(resultSet.getString(6));
			user.setPhoneNo(resultSet.getString(7));
			user.setEmail(resultSet.getString(8));
			user.setUserLevel(resultSet.getInt(9));
			user.setToken(resultSet.getString(10));
			user.setExpirationDate(resultSet.getObject( 11 , LocalDateTime.class ));
			user.setActive(resultSet.getBoolean(12));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
		}
	}
}
