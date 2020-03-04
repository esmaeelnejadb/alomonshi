package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.UnitAdmins;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableUnitAdmins {

	private static String insertCommand = "insert into adminunits(MNG_ID, UNIT_ID, IS_ACTIVE) values(?, ?, ?)";
	private static String updateCommand = "UPDATE adminunits SET MNG_ID = ?, , UNIT_ID = ?, IS_ACTIVE = ? WHERE ID = ";

	/**
	 * inserting admin unit
	 * @param unitAdmins object to be inserted
	 * @return true if truly
	 */

	public static boolean insertUnitAdmin(UnitAdmins unitAdmins){
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(unitAdmins, insertCommand, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Inserting a list of admins for a unit
	 * @param unitAdmins to be inserted
	 * @return true if all admins inserted truly
	 */
	public static boolean insertUnitAdmins(List<UnitAdmins> unitAdmins) {
		Connection connection = DBConnection.getConnection();
		for (UnitAdmins unitAdmin : unitAdmins) {
			if (!executeInsertUpdate(unitAdmin, insertCommand, connection)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * updating admin unit
	 * @param unitAdmin admin unit to be updated
	 * @return true if updated truly
	 */

	private static boolean updateUnitAdmin(UnitAdmins unitAdmin){
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(unitAdmin
				, updateCommand + unitAdmin.getId()
				, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Update unit admins
	 * @param unitAdmins to be updated
	 * @return true if update done truly
	 */

	private static boolean updateUnitAdmins(List<UnitAdmins> unitAdmins) {
		Connection connection = DBConnection.getConnection();
		for (UnitAdmins unitAdmin : unitAdmins) {
			if (!executeInsertUpdate(unitAdmin, updateCommand + unitAdmin.getId(), connection)) {
				DBConnection.closeConnection(connection);
				return false;
			}
		}
		DBConnection.closeConnection(connection);
		return true;
	}

	/**
	 * delete an admin unit
	 * @param unitAdmins admin unit to be deleted
	 * @return true if deleted truly
	 */
	public static boolean deleteUnitAdmin(UnitAdmins unitAdmins){
		unitAdmins.setActive(false);
		return updateUnitAdmin(unitAdmins);
	}

    /**
     * execute insert update commands
     * @param unitAdmins admin unit to be executed
     * @param command command to be executed
     * @return true id executed truly
     */

	private static boolean executeInsertUpdate(UnitAdmins unitAdmins, String command, Connection connection)
	{
		try {
			PreparedStatement ps = connection.prepareStatement(command);
			prepare(ps, unitAdmins);
			int i = ps.executeUpdate();
			return i == 1;
		}catch(SQLException e)
		{
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
			return false;
		}finally
		{
			if(connection != null)
			{
				try
				{
						connection.close();
				} catch (SQLException e)
				{
                    Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
				}
			}
		}
	}

	/**
	 * Author Behzad
	 * @param unitID intended unit id that should be deleted
	 * @return true if process has been done properly
	 */
	public static boolean deleteUnit(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
			String command = "UPDATE adminunits" +
					" SET" +
					" IS_ACTIVE = FALSE" +
					" WHERE" +
					" unit_id = " + unitID;
			PreparedStatement ps = conn.prepareStatement(command);
			int i = ps.executeUpdate();
			return i >= 0 ;

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
	 * Author Behzad
	 * Deleting an admin form company
	 * @return true if process has been done properly
	 */
	public static boolean deleteAdmin(int managerID)
	{
		String command="update adminunits set IS_ACTIVE = false where mng_id = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, managerID);

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

	/**
	 * Author Behzad
	 * Getting admin unit list of a company
	 * @return list of unit belong to an admin in a company
	 */

	public static List<UnitAdmins> getManagerUnits(int managerID)
	{
		Connection conn = DBConnection.getConnection();
		List<UnitAdmins> unitAdmins = new ArrayList<>();
		try
		{
			String command="select * from adminunits where MNG_ID =" + managerID;
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			fillAdminUnits(rs, unitAdmins);
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
		return unitAdmins;
	}

	/**
	 * Author Behzad
	 * @param unitID unit id of intended company
	 * @return list of admins of a unit
	 */

	public static List<UnitAdmins> getUnitManagers(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		List<UnitAdmins> unitAdmins = new ArrayList<>();
		try
		{
			String command="select * from adminunits where UNIT_ID = " + unitID;
			Statement stmt =conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillAdminUnits(rs, unitAdmins);
			return unitAdmins;
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

	/**
	 * Filling admin object
	 * @param resultSet returned from JDBC
	 * @param unitAdmins filled object
	 */
	private static void fillAdminUnit(ResultSet resultSet, UnitAdmins unitAdmins){
	    try {
	        unitAdmins.setId(resultSet.getInt(1));
	        unitAdmins.setManagerID(resultSet.getInt(2));
	        unitAdmins.setUnitID(resultSet.getInt(3));
	        unitAdmins.setActive(resultSet.getBoolean(4));
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

	/**
	 * Filling single admin unit with resultset
	 * @param resultSet returned from JDBC
	 * @param unitAdmins filled object
	 */
    private static void fillSingleAdminUnit(ResultSet resultSet, UnitAdmins unitAdmins){
	    try {
	     while (resultSet.next()){
	         fillAdminUnit(resultSet, unitAdmins);
         }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

	/**
	 * Filling list of unit admins returned from database
	 * @param resultSet returned from JDBC
	 * @param unitAdmins filled list
	 */
	private static void fillAdminUnits(ResultSet resultSet, List<UnitAdmins> unitAdmins){
	    try {
	     while (resultSet.next()){
	         UnitAdmins unitAdmin = new UnitAdmins();
	         fillAdminUnit(resultSet, unitAdmin);
	         unitAdmins.add(unitAdmin);
         }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

	/**
	 * Prepare statement for executing query
	 * @param preparedStatement to be prepared
	 * @param unitAdmins to be prepared with
	 */

	private static void prepare(PreparedStatement preparedStatement, UnitAdmins unitAdmins){
	    try {
	        preparedStatement.setInt(1, unitAdmins.getManagerID());
	        preparedStatement.setInt(2, unitAdmins.getUnitID());
	        preparedStatement.setBoolean(3, unitAdmins.isActive());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }
}