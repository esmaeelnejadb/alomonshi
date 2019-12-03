package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.Users;

public abstract class TableAdmin {

	public static boolean insertUser(Users user){
		String command = "insert into ADMININFO (user_id, name, botusername, username, password, phonNo, email, Comp_ID, "
				+ "user_action, mng_stat) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(user, command);
	}

	public static boolean updateUser(Users user){
		String command = "UPDATE ADMININFO SET user_id = ?, name = ?, botusername = ?, username = ?, password = ?, phonNo = ?" +
				", email = ?, Comp_ID = ?, user_action = ?, mng_stat = ?";
		return executeInsertUpdate(user, command);
	}


	/**
	 * Author Behzad
	 * This function identify if a manager has been registered before or not
	 * @return true if a manager has been registered
	 */
	public static boolean isRegistered(int userID)
	{
		String command = "select ID from ADMININFO where USER_ID = " + userID + " limit 1";
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs=  stmt.executeQuery(command);
			int count = 0;
			while(rs.next())
			{
				count++;
			}
			return count != 0;
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

	/**
	 * Author Behzad
	 * insert new manager into database
	 * @param user information of new user
	 * @return true if inserting process has been done properly
	 */

	private static boolean executeInsertUpdate(Users user, String command)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, user);
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

	//Setting the new client's name

	/**
	 * Author Behzad
	 * @param name name of the user to be inserted into database
	 * @return true if inserting process has been done properly
	 */

	public static boolean setName(String name, int userID)
	{
		String command="update ADMININFO set name=? where USER_ID = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, name);
			ps.setInt(2, userID);
			int i=ps.executeUpdate();
			return i==1;

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

	//Setting the new Email

	/**
	 * Author Behzad
	 * @param Email Email of the user to be inserted into database
	 * @return true if inserting process has been done properly
	 */

	public static boolean setEmail(String Email, int userID)
	{
		String command="update ADMININFO set EMAIL=? where USER_ID =?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, Email);
			ps.setInt(2, userID);
			int i=ps.executeUpdate();
			return i==1;

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
	 * Setting the password
	 * @param password of the user to be inserted into database
	 * @return true if inserting process has been done properly
	 */

	public static boolean setPassword(String password, int userID)
	{
		String command = "update ADMININFO set password=? where USER_ID =?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, password);
			ps.setInt(2, userID);
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


	/**
	 * Author Behzad
	 * @param catID catergory ID of the company which the manager is managed that company
	 * @return true if inserting process has been done properly
	 */

	public static boolean setCompanyCatID(int catID, int userID) {
		String command = "update ADMININFO set category_id = ? where USER_ID = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, catID);
			ps.setInt(2, userID);
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

	//Getting name of the user

	/**
	 * Author Behzad
	 *  Getting name of the user
	 * @return name of the user
	 */

	public static String getName(int userID)
	{
		String command = "select name from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while (rs.next()){
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

	//Getting password of the user

	/**
	 * Getting password of the user
	 * Author Behzad
	 * @return password of the user
	 */

	public static String getPassword(int userID)
	{
		String command = "select password from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()){
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


	/**
	 * Author
	 * Getting company category ID of the company manager
	 * @return category ID of the company manager
	 */

	public static int getCompanyCatID(int userID) {
		String command = "select category_id from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			return rs.getInt(1);
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		}
	}
	//

	/**
	 * Author Behzad
	 * Getting Phone No. of the user
	 * @return phone number of the user
	 */

	public static long getPhoneNo(int userID)
	{
		String command = "select phonNo from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			return rs.getLong(1);

		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * Delete manager from the manager table
	 * @return true if process has been done properly
	 */

	public static boolean deleteUser(int userID) {
		String command = "delete from ADMININFO where USER_ID = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, userID);
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

	/**
	 * Author Behzad
	 * @return object user by ID of the user
	 */

	public static Users getUser(int userID) {
		String command = "select * from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection();
		Users user = new Users();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillUser(rs, user);
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

	/**
	 * Getting user id of the user by phone number
	 * @param phoneNo phone number of the intended manager
	 * @return user id of the intended user
	 */

	public static int getUserIDByPhone(String phoneNo) {
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			String command="select USER_ID from ADMININFO where phonNo = " + phoneNo ;
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()){
				return rs.getInt(1);
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
		return 0;
	}


	/**
	 * Author Behzad
	 * Setting company id of new manager
	 * @param companyID id of company of the company manager
	 * @return true if process has been done properly
	 */

	public static boolean setCompanyID(int companyID, int userID) {
		String command="update ADMININFO set Comp_ID=? where USER_ID =?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, companyID);
			ps.setInt(2, userID);
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


	/**
	 * Author Behzad
	 * Setting unit id of the manager
	 * @param unitID unit id of the intended manager
	 * @return true if the process has been done properly
	 */

	public static boolean setUnitID(int unitID, int userID) {
		String command = "update ADMININFO set mod_unit_ID = ? where USER_ID = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, unitID);
			ps.setInt(2, userID);
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


	/**
	 * Author Behzad
	 * Setting level of the company admin
	 * @param stateID level of the company admin
	 * @return true if the process has been done properly
	 */

	public static boolean setAdminState( int stateID, int userID) {
		String command="update ADMININFO set mng_stat = ? where USER_ID = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, stateID);
			ps.setInt(2, userID);
			int i=ps.executeUpdate();
			return i==1;

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
	 * Getting id of the company of the company admin
	 * @return id of the company
	 */

	public static int getCompanyID(int userID) {
		String command = "select Comp_ID from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			return rs.getInt(1);
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Author Behzad
	 * Getting unit id of the company manager
	 * @return unit id of the company manager
	 */

	public int getUnitID(int userID) {
		String command = "select mod_unit_ID from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			return rs.getInt(1);
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Author Behzad
	 * Getting level of the company manager
	 * @return level of the company manager
	 */

	public static int getStatus(int userID) {
		String command ="select mng_stat from ADMININFO where USER_ID = " + userID;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			return rs.getInt(1);
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					e.printStackTrace();	
				}
			}	
		}
	}

	private static void prepare(PreparedStatement preparedStatement, Users user){
		try {
			preparedStatement.setInt(1, user.getUserID());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getBotUsername());
			preparedStatement.setString(4, user.getUsername());
			preparedStatement.setString(5, user.getPassword());
			preparedStatement.setString(6, user.getPhoneNo());
			preparedStatement.setString(7, user.getEmail());
			preparedStatement.setInt(8, user.getCompanyID());
			preparedStatement.setInt(9, user.getUserAction());
			preparedStatement.setInt(10, user.getStatus());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
	private static void fillUser(ResultSet resultSet, Users user){
		try {
			user.setID(resultSet.getInt(1));
			user.setUserID(resultSet.getInt(2));
			user.setName(resultSet.getString(4));
			user.setBotUsername(resultSet.getString(5));
			user.setUsername(resultSet.getString(6));
			user.setPassword(resultSet.getString(7));
			user.setPhoneNo(resultSet.getString(8));
			user.setEmail(resultSet.getString(9));
			user.setCompanyID(resultSet.getInt(10));
			user.setUserAction(resultSet.getInt(11));
			user.setStatus(resultSet.getInt(12));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	public static void fillUsers(ResultSet resultSet, List<Users> users){
		try{
			while (resultSet.next()){
				Users user = new Users();
				fillUser(resultSet, user);
				users.add(user);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
}
