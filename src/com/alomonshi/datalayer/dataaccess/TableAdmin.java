package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.UserLevels;
import com.alomonshi.object.tableobjects.Admin;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.uiobjects.CompanyAdmin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableAdmin {

	private static final String insertCommand = "INSERT INTO MANAGER (" +
													" MNG_ID," +
													" COMPANY_ID," +
													" MANAGER_LEVEL," +
													" IS_ACTIVE" +
													")" +
													" VALUES(?, ?, ?, ?)";

	private static final String updateCommand = "UPDATE MANAGER SET " +
													"MNG_ID = ?," +
													" COMPANY_ID= ?," +
													" MANAGER_LEVEL= ?," +
													" IS_ACTIVE = ?" +
													" WHERE ID = ";

	/**
	 * insert a manager into manager table
	 * @param admin manager to be inserted
	 * @return true if inserted correctly
	 */

	public static boolean insertManager(Admin admin){
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(admin, insertCommand, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Update admin
	 * @param admin to be updated
	 * @return update result
	 */

	private static boolean updateManager(Admin admin){
		Connection connection = DBConnection.getConnection();
		boolean response = executeInsertUpdate(admin, updateCommand + admin.getID(), connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	public static boolean deleteAdmin(Admin admin){
	    admin.setActive(false);
	    return updateManager(admin);
    }

	/**
	 * Executing insert update command
	 * @param admin to be inserted
	 * @param command command to be executed
	 * @param connection JDBC connection
	 * @return Execution result
	 */
	private static boolean executeInsertUpdate(Admin admin, String command, Connection connection)
	{
		try {
			PreparedStatement ps = connection.prepareStatement(command);
			prepare(ps, admin);
			return ps.executeUpdate() == 1;
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}
	}

	/**
	 * Getting a manager of a company
	 * @param adminID intended manager
	 * @param companyID intended company
	 * @return list of manager object
	 */

	public static Admin getAdminOfCompany(int adminID, int companyID){
		Connection conn = DBConnection.getConnection();
		Admin admin = new Admin();
		try
		{
			String command = "SELECT" +
					" *" +
					" FROM" +
					" manager" +
					" WHERE" +
					" MNG_ID =  " + adminID +
					" AND COMPANY_ID =  " + companyID +
					" AND IS_ACTIVE IS TRUE";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillSingleAdmin(rs, admin);
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
		return admin;
	}

	/**
	 * Getting a manager companies
	 * @param adminID intended manager
	 * @return list of manager object
	 */
	public static List<Integer> getAdminCompanyIDs(int adminID){
		String command = "SELECT" +
				" COMPANY_ID AS companyID" +
				" FROM" +
				" manager" +
				" WHERE" +
				" MNG_ID =  " + adminID +
				" AND IS_ACTIVE IS TRUE";
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
	 * Getting a manager companies
	 * @param adminID intended manager
	 * @return list of manager object
	 */
	public static List<Company> getAdminCompanies(int adminID){
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try
		{
			String command = "SELECT" +
					" comp.id AS companyID, comp.comp_name AS companyName" +
					" FROM" +
					" manager mng" +
					" LEFT JOIN" +
					" companies comp ON mng.company_id = comp.id" +
					" AND mng.IS_ACTIVE IS TRUE" +
					" AND comp.is_active IS TRUE" +
					" WHERE" +
					" mng.mng_id = " + adminID;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				Company company = new Company();
				company.setID(rs.getInt("companyID"));
				company.setCompanyName(rs.getString("companyName"));
				companies.add(company);
			}
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
		return companies;
	}

	/**
	 * Getting list of a company managers
	 * @param companyID intended company
	 * @return list of manager object
	 */

    public static List<CompanyAdmin> getCompanyAdmin(int companyID){
        String command = "SELECT" +
				" mng.MNG_ID AS adminID," +
				" cl.NAME as adminName," +
				" cl.PHONE as adminPhone" +
				" FROM" +
				" manager AS mng," +
				" companies AS comp," +
				" clientinfo AS cl" +
				" WHERE" +
				" comp.id = " + companyID +
				" AND mng.COMPANY_ID = comp.id" +
				" AND cl.ID = mng.MNG_ID" +
				" AND mng.IS_ACTIVE IS TRUE" +
				" AND comp.IS_ACTIVE IS TRUE" +
				" AND cl.IS_ACTIVE IS TRUE";
        Connection conn = DBConnection.getConnection();
        List<CompanyAdmin> companyAdmins = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillCompanyAdminsForUI(rs, companyAdmins);
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
        return companyAdmins;
    }

	/**
	 * Filling manager object with manager got from database
	 * @param resultSet returned from JDBC
	 * @param admin object to be filled
	 */
	private static void fillAdmin(ResultSet resultSet, Admin admin){
		try {
			admin.setID(resultSet.getInt(1));
			admin.setManagerID(resultSet.getInt(2));
			admin.setCompanyID(resultSet.getInt(3));
			admin.setManagerLevel(UserLevels.getByValue(resultSet.getInt(4)));
			admin.setActive(resultSet.getBoolean(5));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	/**
	 * Fill admin object
	 * @param resultSet JDBC returned object
	 * @param admin to be filled
	 */
	private static void fillSingleAdmin(ResultSet resultSet, Admin admin){
	    try{
            while (resultSet.next()){
                fillAdmin(resultSet, admin);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

	/**
	 * fill admin list got from database
	 * @param resultSet JDBC returned object
	 * @param admins list to be filled
	 */
	public static void fillAdmins(ResultSet resultSet, List<Admin> admins){
        try{
            while (resultSet.next()){
                Admin admin = new Admin();
                fillAdmin(resultSet, admin);
                admins.add(admin);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

	/**
	 * Fill company admin list for ui
	 * @param resultSet JDBC object returned from database
	 * @param companyAdminList to be filled
	 */
	private static void fillCompanyAdminsForUI(ResultSet resultSet
			, List<CompanyAdmin> companyAdminList) {
		try {
			while (resultSet.next()) {
				CompanyAdmin companyAdmin = new CompanyAdmin();
				companyAdmin.setAdminID(resultSet.getInt("adminID"));
				companyAdmin.setAdminName(resultSet.getString("adminName"));
				companyAdmin.setAdminPhone(resultSet.getString("adminPhone"));
				companyAdmin.setAdminUnit(TableUnit.getAdminUnit(resultSet.getInt("adminID")));
				companyAdminList.add(companyAdmin);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}


	/**
	 * Prepare statement object to be filled by intended object
	 * @param preparedStatement JDBC object to be prepared for execution
	 * @param admin to be prepared with
	 */
	private static void prepare(PreparedStatement preparedStatement, Admin admin){
		try {
			preparedStatement.setInt(1, admin.getManagerID());
			preparedStatement.setInt(2, admin.getCompanyID());
			preparedStatement.setInt(3, admin.getManagerLevel().getValue());
			preparedStatement.setBoolean(4, admin.isActive());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
}