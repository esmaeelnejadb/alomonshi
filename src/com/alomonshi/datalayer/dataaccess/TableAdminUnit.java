package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.AdminUnit;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableAdminUnit {

	/**
	 * inserting admin unit
	 * @param adminUnit object to be inserted
	 * @return true if truly
	 */

	public static boolean insertAdminUnit(AdminUnit adminUnit){
		String command = "insert into adminunits(MNG_ID, UNIT_ID, IS_ACTIVE) values(?, ?, ?)";
		return executeInsertUpdate(adminUnit, command);
	}

	/**
	 * updating admin unit
	 * @param adminUnit admin unit to be updated
	 * @return true if updated truly
	 */

	private static boolean updateAdminUnit(AdminUnit adminUnit){
		String command = "update adminunits set MNG_ID = ?, , UNIT_ID = ?, IS_ACTIVE = ? where id = " + adminUnit.getId();
		return executeInsertUpdate(adminUnit, command);
	}

	/**
	 * delete an admin unit
	 * @param adminUnit admin unit to be deleted
	 * @return true if deleted truly
	 */
	public static boolean deleteAdminUnit(AdminUnit adminUnit){
		adminUnit.setActive(false);
		return updateAdminUnit(adminUnit);
	}

    /**
     * execute insert update commands
     * @param adminUnit admin unit to be executed
     * @param command command to be executed
     * @return true id executed truly
     */

	private static boolean executeInsertUpdate(AdminUnit adminUnit, String command)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, adminUnit);
			int i = ps.executeUpdate();
			return i == 1;

		}catch(SQLException e)
		{
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
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
	private static boolean deleteUnit(int unitID)
	{
		String command = "update adminunits set IS_ACTIVE = false where unit_id = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, unitID);

			int i=ps.executeUpdate();
			return i == 1 ;

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

	public static List<AdminUnit> getManagerUnits(int managerID)
	{
		Connection conn = DBConnection.getConnection();
		List<AdminUnit> adminUnits = new ArrayList<>();
		try
		{
			String command="select * from adminunits where MNG_ID =" + managerID;
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			fillAdminUnits(rs, adminUnits);
			return adminUnits;
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
	 * Author Behzad
	 * @param unitID unit id of intended company
	 * @return list of admins of a unit
	 */

	public static List<AdminUnit> getUnitManagers(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		List<AdminUnit> adminUnits = new ArrayList<>();
		try
		{
			String command="select * from adminunits where UNIT_ID = " + unitID;
			Statement stmt =conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillAdminUnits(rs, adminUnits);
			return adminUnits;
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

	private static void fillAdminUnit(ResultSet resultSet, AdminUnit adminUnit){
	    try {
	        adminUnit.setId(resultSet.getInt(1));
	        adminUnit.setManagerID(resultSet.getInt(2));
	        adminUnit.setUnitID(resultSet.getInt(3));
	        adminUnit.setActive(resultSet.getBoolean(4));
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

    private static void fillSingleAdminUnit(ResultSet resultSet, AdminUnit adminUnit){
	    try {
	     while (resultSet.next()){
	         fillAdminUnit(resultSet, adminUnit);
         }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

    private static void fillAdminUnits(ResultSet resultSet, List<AdminUnit> adminUnits){
	    try {
	     while (resultSet.next()){
	         AdminUnit adminUnit = new AdminUnit();
	         fillAdminUnit(resultSet, adminUnit);
	         adminUnits.add(adminUnit);
         }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }

	private static void prepare(PreparedStatement preparedStatement, AdminUnit adminUnit){
	    try {
	        preparedStatement.setInt(1, adminUnit.getManagerID());
	        preparedStatement.setInt(2, adminUnit.getUnitID());
	        preparedStatement.setBoolean(3, adminUnit.isActive());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception" + e);
        }
    }
}