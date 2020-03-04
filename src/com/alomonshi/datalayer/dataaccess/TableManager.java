package com.alomonshi.datalayer.dataaccess;


import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableManager {

	/**
	 * insert a manager into manager table
	 * @param manager manager to be inserted
	 * @return true if inserted correctly
	 */
	public static boolean insertManager(Manager manager){
		String command = "insert into manager (MNG_ID, COMPANY_ID, MANAGER_LEVEL, IS_ACTIVE) "
				+ "values(?, ?, ?, ?)";
		return executeInsertUpdate(manager, command);
	}

	public static boolean updateManager(Manager manager){
		String command = "update manager set MNG_ID = ?, COMPANY_ID= ?, MANAGER_LEVEL= ?, IS_ACTIVE = ? where ID = " + manager.getID();
		return executeInsertUpdate(manager, command);
	}

	public static boolean deleteManager(Manager manager){
	    manager.setActive(false);
	    return updateManager(manager);
    }

	private static boolean executeInsertUpdate(Manager manager, String command)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, manager);
			return ps.executeUpdate() == 1;

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
	 * Getting a manager companies
	 * @param managerID intended manager
	 * @return list of manager object
	 */

	public static List<Integer> getManagerCompanies(int managerID){
		String command = "SELECT" +
				" COMPANY_ID AS companyID" +
				" FROM" +
				" manager" +
				" WHERE" +
				" MNG_ID =  " + managerID ;
		Connection conn = DBConnection.getConnection();
		List<Integer> companies = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while (rs.next()) {
				companies.add(rs.getInt("companyID"));
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
		return companies;
	}

	/**
	 * Getting list of a company managers
	 * @param companyID intended company
	 * @return list of manager object
	 */

    public static List<Integer> getCompanyManagers(int companyID){
        String command = "SELECT" +
				" MNG_ID as managerID" +
				" FROM" +
				" manager" +
				" WHERE" +
				" COMPANY_ID = " + companyID ;
        Connection conn = DBConnection.getConnection();
        List<Integer> managers = new ArrayList<>();
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            while (rs.next()) {
            	managers.add(rs.getInt("managerID"));
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
        return managers;
    }

	/**
	 * Filling manager object with manager got from database
	 * @param resultSet returned from JDBC
	 * @param manager object to be filled
	 */
	private static void fillManager(ResultSet resultSet, Manager manager){
		try {
			manager.setID(resultSet.getInt(1));
			manager.setManagerID(resultSet.getInt(2));
			manager.setCompanyID(resultSet.getInt(3));
			manager.setManagerLevel(UserLevels.getByValue(resultSet.getInt(4)));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	public static void fillSingleManager(ResultSet resultSet, Manager manager){
	    try{
            while (resultSet.next()){
                fillManager(resultSet, manager);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    public static void fillManagers(ResultSet resultSet, List<Manager> managers){
        try{
            while (resultSet.next()){
                Manager manager = new Manager();
                fillManager(resultSet, manager);
                managers.add(manager);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

	private static void prepare(PreparedStatement preparedStatement, Manager manager){
		try {
			preparedStatement.setInt(1, manager.getManagerID());
			preparedStatement.setInt(2, manager.getCompanyID());
			preparedStatement.setInt(3, manager.getManagerLevel().getValue());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
}