package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.UnitAdmin;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableUnitAdmin {

	private static String insertCommand = "insert into adminunits(MNG_ID, UNIT_ID, IS_ACTIVE) values(?, ?, ?)";
	private static String updateCommand = "UPDATE adminunits SET MNG_ID = ?, , UNIT_ID = ?, IS_ACTIVE = ? WHERE ID = ";

	/**
	 * inserting admin unit
	 * @param unitAdmin object to be inserted
	 * @return true if truly
	 */

	public static boolean insertUnitAdmin(UnitAdmin unitAdmin){
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(unitAdmin, insertCommand, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Inserting a list of admins for a unit
	 * @param unitAdmins to be inserted
	 * @return true if all admins inserted truly
	 */
	public static boolean insertUnitAdmins(List<UnitAdmin> unitAdmins) {
		Connection connection = DBConnection.getConnection();
		for (UnitAdmin unitAdmin : unitAdmins) {
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

	private static boolean updateUnitAdmin(UnitAdmin unitAdmin){
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

	private static boolean updateUnitAdmins(List<UnitAdmin> unitAdmins) {
		Connection connection = DBConnection.getConnection();
		for (UnitAdmin unitAdmin : unitAdmins) {
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
	 * @param unitAdmin admin unit to be deleted
	 * @return true if deleted truly
	 */
	public static boolean deleteUnitAdmin(UnitAdmin unitAdmin){
		unitAdmin.setActive(false);
		return updateUnitAdmin(unitAdmin);
	}

    /**
     * execute insert update commands
     * @param unitAdmin admin unit to be executed
     * @param command command to be executed
     * @return true id executed truly
     */

	private static boolean executeInsertUpdate(UnitAdmin unitAdmin, String command, Connection connection)
	{
		try {
			PreparedStatement ps = connection.prepareStatement(command);
			prepare(ps, unitAdmin);
			int i = ps.executeUpdate();
			return i == 1;
		}catch(SQLException e)
		{
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
			return false;
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
	public static boolean deleteAdmin(int managerID, int companyID)
	{
		Connection conn = DBConnection.getConnection();
		try {
            String command = "UPDATE adminunits" +
                    " SET" +
                    " IS_ACTIVE = FALSE" +
                    " WHERE" +
                    " MNG_ID = " + managerID +
                    " AND UNIT_ID " +
                    "in " +
                    "(SELECT ID FROM units WHERE COMP_ID = "+ companyID + " AND IS_ACTIVE IS TRUE)";
			PreparedStatement ps = conn.prepareStatement(command);
			int i = ps.executeUpdate();
			return i >= 0;

		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}finally {
			if(conn != null) {
				try {
						conn.close();
				} catch (SQLException e) {
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

	public static List<Integer> getAdminUnitIDs(int managerID)
	{
		Connection conn = DBConnection.getConnection();
		List<Integer> unitIDs = new ArrayList<>();
		try
		{
			String command = "SELECT" +
					" UNIT_ID as unitID" +
					" FROM" +
					" adminunits" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND MNG_ID = " + managerID;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				unitIDs.add(rs.getInt("unitID"));
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
		return unitIDs;
	}

	/**
	 * Author Behzad
	 * @param unitID unit id of intended company
	 * @return list of admins of a unit
	 */

	public static List<Integer> getUnitAdminIDs(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		List<Integer> unitAdmins = new ArrayList<>();
		try
		{
			String command = "SELECT" +
					" MNG_ID as managerID" +
					" FROM" +
					" adminunits" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND UNIT_ID = " + unitID;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				unitAdmins.add(rs.getInt("managerID"));
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
		return unitAdmins;
	}

	/**
	 * Prepare statement for executing query
	 * @param preparedStatement to be prepared
	 * @param unitAdmin to be prepared with
	 */

	private static void prepare(PreparedStatement preparedStatement, UnitAdmin unitAdmin){
	    try {
	        preparedStatement.setInt(1, unitAdmin.getManagerID());
	        preparedStatement.setInt(2, unitAdmin.getUnitID());
	        preparedStatement.setBoolean(3, unitAdmin.isActive());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }
}