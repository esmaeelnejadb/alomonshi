package com.alomonshi.datalayer.dataaccess;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.Company;

public class TableCompanies {

	public TableCompanies()
	{}

	public static boolean insert(Company company){
		String command="insert into COMPANIES (COMP_CAT_ID, COMP_NAME, COMP_ADDRESS, COMP_PHONE1, COMP_PHONE2"
				+ ", COMP_PHONE3, LOCATION_LAT, LOCATION_LON, " +
				"COMP_WEBSITE, COMP_RATE, COMP_STAT, COMP_PICURL," +
				" COMP_LOCALITY, CITY_ID, DISTRICT_ID, IS_ACTIVE, COMP_COMMERTIALCODE)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return executeInsertUpdateCommand(company, command);
	}

	public static boolean update(Company company){
		String command = "UPDATE COMPANIES SET COMP_CAT_ID = ?, COMP_NAME = ?, COMP_ADDRESS = ?, " +
				"COMP_PHONE1 = ?, COMP_PHONE2 = ? , COMP_PHONE3 = ?, LOCATION_LAT = ?, LOCATION_LON = ?," +
				"COMP_WEBSITE = ?, COMP_RATE = ?, COMP_STAT = ?, COMP_PICURL = ?, COMP_LOCALITY = ?, " +
				"CITY_ID = ?, DISTRICT_ID = ?, IS_ACTIVE = ? , COMP_COMMERTIALCODE = ? WHERE ID = "  + company.getID();
		return executeInsertUpdateCommand(company, command);
	}

	public static boolean delete(Company company){
		company.setActive(false);
		return update(company);
	}

		
	private static boolean executeInsertUpdateCommand(Company company, String command)
	{		
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, company);
			int i = ps.executeUpdate();
			return i==1;
			
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
	public static Company getCompany(int companyID) {
		String command="select * from COMPANIES where ID = " + companyID;
		Connection conn = DBConnection.getConnection();
		Company company = new Company();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				fillCompany(rs, company);
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
		return company;
	}

	public static List<Company> getAllCompanies() {
		String command="select * from COMPANIES where IS_ACTIVE IS TRUE";
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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
	
	public static List<Company> getCompanies(int categoryID) {
		String command = "select * from COMPANIES where IS_ACTIVE IS TRUE AND COMP_CAT_ID = " + categoryID;
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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

	public static List<Company> getBestCompanies(int limitNumber) {
		String command="select * from COMPANIES where IS_ACTIVE IS TRUE ORDER BY COMP_RATE DESC LIMIT " + limitNumber;
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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
	
	public static List<Company> getNewestCompanies(int limitNumber) {
		String command="select * from COMPANIES where IS_ACTIVE IS TRUE ORDER BY id DESC LIMIT " + limitNumber;
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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
	
	public static List<Company> getSearchedCompanies(String compName, int categoryID) {
		String command="select * from COMPANIES where IS_ACTIVE is true AND COMP_NAME LIKE '%" + compName +  "%' AND COMP_CAT_ID = " + categoryID;
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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

	
	public static int getCompanyNumbers(int catID)
	{
		String command ="select count(*) from COMPANIES where IS_ACTIVE is true AND COMP_CAT_ID = " + catID;
		Connection conn = DBConnection.getConnection();
		int count = 0;
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				count =  rs.getInt(1);
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
		return count;
	}
	
	public static int getIDByUsername(String username)
	{
		String command="select ID from COMPANIES where comp_username = '" + username + "'";
		Connection conn = DBConnection.getConnection();
		int id = 0;
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				id = rs.getInt(1);
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
		return id;
	}
	
	public static int getID(String name, int categoryID)
	{
		String command="select ID from COMPANIES where comp_cat_ID = " + categoryID + " and comp_name = '" + name + "'";
		Connection conn = DBConnection.getConnection();
		int id = 0;
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				id  = rs.getInt(1);
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
		return id;
	}
	public static int getCategoryID(int companyID)
	{
		String command="select comp_cat_ID from COMPANIES where ID = " + companyID;
		Connection conn = DBConnection.getConnection();
		int id = 0;
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next())
			{
				id =  rs.getInt(1);
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
		return id;
	}


	public static String getName(int companyID)
	{
		String command = "select comp_name from COMPANIES where ID = " + companyID;
		Connection conn = DBConnection.getConnection();
		String name = null;
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				name = rs.getString(1);
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
		return name;
	}

	public static boolean setPhone(String phone, int companyID)
	{
		String command = "update COMPANIES set COMP_PHONE = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, phone);
			ps.setInt(2, companyID);
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
	
	public static boolean setName(String name, int companyID)
	{
		String command = "update COMPANIES set COMP_NAME = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, name);
			ps.setInt(2, companyID);
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

	public static boolean setWebSite(String website, int companyID)
	{
		String command = "update COMPANIES set COMP_WEBSITE = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, website);
			ps.setInt(2, companyID);
			int i = ps.executeUpdate();
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
	
	public static boolean setAddress(String address, int companyID)
	{
		String command = "update COMPANIES set COMP_ADDRESS = ? where ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, address);
			ps.setInt(2, companyID);
			int i = ps.executeUpdate();
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

	private static void prepare(PreparedStatement preparedStatement, Company company){
		try{
			preparedStatement.setInt(1, company.getCompanyCatID());
			preparedStatement.setString(2, company.getCompanyName());
			preparedStatement.setString(3, company.getCompanyAddress());
			preparedStatement.setString(4, company.getCompanyPhoneNo1());
			preparedStatement.setString(5, company.getCompanyPhoneNo2());
			preparedStatement.setString(6, company.getCompanyPhoneNo3());
			preparedStatement.setFloat(7, company.getLocationLat());
			preparedStatement.setFloat(8, company.getLocationLon());
			preparedStatement.setString(9, company.getWebsite());
			preparedStatement.setFloat(10, company.getRate());
			preparedStatement.setString(11, company.getPicURL());
			preparedStatement.setString(12, company.getLocality());
			preparedStatement.setInt(13, company.getCityID());
			preparedStatement.setInt(14, company.getDistrictID());
			preparedStatement.setBoolean(15, company.isActive());
			preparedStatement.setString(16, company.getCommercialCode());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillCompany(ResultSet resultSet, Company company){
		try{
			company.setID(resultSet.getInt(1));
			company.setCompanyCatID(resultSet.getInt(2));
			company.setCompanyName(resultSet.getString(3));
			company.setCompanyAddress(resultSet.getString(4));
			company.setCompanyPhoneNo1(resultSet.getString(5));
			company.setCompanyPhoneNo2(resultSet.getString(6));
			company.setCompanyPhoneNo3(resultSet.getString(7));
			company.setLocationLat(resultSet.getFloat(8));
			company.setLocationLon(resultSet.getFloat(9));
			company.setWebsite(resultSet.getString(10));
			company.setRate(resultSet.getFloat(11));
			company.setPicURL(resultSet.getString(12));
			company.setLocality(resultSet.getString(13));
			company.setCityID(resultSet.getInt(14));
			company.setDistrictID(resultSet.getInt(15));
			company.setActive(resultSet.getBoolean(16));
			company.setCommercialCode(resultSet.getString(17));
			company.setUnits(TableUnit.getUnits(company.getID()));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillCompanies(ResultSet resultSet, List<Company> companies){
		try{
			while (resultSet.next()){
				Company company = new Company();
				fillCompany(resultSet, company);
				companies.add(company);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
}