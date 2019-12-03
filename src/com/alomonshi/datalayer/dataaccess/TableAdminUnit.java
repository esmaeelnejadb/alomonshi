package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class TableAdminUnit {
	
	private int mngID;
	public TableAdminUnit setMngID(int mngID)
	{
		this.mngID = mngID;
		return this;
	}

	protected TableAdminUnit()
	{}

	/**
	 * Author Behzad
	 * @param unitID unit id of new admin
	 * @return true if process has been done properly
	 */

	public boolean insertAdmin(int unitID)
	{
		String command="insert into ADMINUNITS(mng_id, unit_id) values(?, ?)";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, mngID);
			ps.setInt(2, unitID);

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
	 * @param unitID inteded unit id that should be deleted
	 * @return true if process has been done properly
	 */
	protected boolean deleteUnit(int unitID)
	{
		String command="delete from ADMINUNITS where unit_id = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, unitID);

			int i=ps.executeUpdate();
			return i == 1 ;

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
	 * Deleting an admin form company
	 * @return true if process has been done properly
	 */
	public boolean deleteAdmin()
	{
		String command="delete from ADMINUNITS where mng_id = ?";
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, mngID);

			int i=ps.executeUpdate();
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
	 * Getting admin unit list of a company
	 * @return list of unit belong to an admin in a company
	 */

	public List<Integer> getAdminUnitIDs()
	{
		Connection conn = DBConnection.getConnection();
		List<Integer> adminUnits = new ArrayList<>();
		try
		{
			String command="select unit_id from ADMINUNITS where mng_id=" + mngID;
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				adminUnits.add(rs.getInt(1));
			}
			return adminUnits;
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
	}

	/**
	 * Author Behzad
	 * @param unitID unit id of intended company
	 * @return list of admins of a unit
	 */

	public List<Integer> getAdminIDs(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		List<Integer> adminUnits = new ArrayList<>();
		try
		{
			String command="select mng_id from ADMINUNITS where unit_id =" + unitID;
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				adminUnits.add(rs.getInt(1));
			}
			return adminUnits;
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
	}	
}