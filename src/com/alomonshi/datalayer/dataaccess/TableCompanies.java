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
import com.alomonshi.object.enums.FilterItem;
import com.alomonshi.object.tableobjects.Company;
import com.alomonshi.object.tableobjects.CompanyCategories;

public class TableCompanies {

	public static boolean insert(Company company){
		String command = "INSERT INTO COMPANIES (" +
				"COMP_CAT_ID," +
				" COMP_NAME," +
				" COMP_ADDRESS," +
				" COMP_PHONE1," +
				" COMP_PHONE2," +
				" COMP_PHONE3," +
				" LOCATION_LAT," +
				" LOCATION_LON, " +
				" COMP_WEBSITE," +
				" COMP_RATE, COMP_STAT, COMP_PICURL," +
				" COMP_LOCALITY," +
				" CITY_ID," +
				" DISTRICT_ID," +
				" IS_ACTIVE," +
				" COMP_COMMERTIALCODE" +
                " )"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		return executeInsertUpdateCommand(company, command);
	}

	public static boolean update(Company company){
		String command = "UPDATE COMPANIES SET COMP_CAT_ID = ?," +
				" COMP_NAME = ?," +
				" COMP_ADDRESS = ?," +
				" COMP_PHONE1 = ?," +
				" COMP_PHONE2 = ?," +
				" COMP_PHONE3 = ?," +
				" LOCATION_LAT = ?," +
				" LOCATION_LON = ?," +
				" COMP_WEBSITE = ?," +
				" COMP_RATE = ?," +
				" COMP_STAT = ?," +
				" COMP_PICURL = ?," +
				" COMP_LOCALITY = ?," +
				" CITY_ID = ?," +
				" DISTRICT_ID = ?," +
				" IS_ACTIVE = ?," +
				" COMP_COMMERTIALCODE = ?" +
				" WHERE ID = "  + company.getID();
		return executeInsertUpdateCommand(company, command);
	}

	public static boolean delete(Company company){
		company.setActive(false);
		return update(company);
	}


	/**
	 * Executing insert update company
	 * @param company to be inserted or updated
	 * @param command to be executed
	 * @return execution result
	 */
	private static boolean executeInsertUpdateCommand(Company company, String command)
	{		
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(ps, company);
			int i = ps.executeUpdate();
			return i == 1;
			
		}catch(SQLException e)
		{
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
     * Getting best list of each category
     * @return list of best companies in each category
     */

	public static List<CompanyCategories> getBestList(int limitNumber) {
        List<CompanyCategories> companyCategories = TableCompanyCategory
                .getCompanyCategoryList(2);
        Connection connection = DBConnection.getConnection();
        for (CompanyCategories companyCategory : companyCategories) {
            companyCategory.setCompanies(getTopBestCompanies(
                    companyCategory.getID(),
                    limitNumber,
                    connection));
            companyCategory.setCompanySize(companyCategory.getCompanies().size());
        }
        DBConnection.closeConnection(connection);
        return companyCategories;
    }

    /**
     * Getting discount list of each category
     * @return list of discount companies in each category
     */

    public static List<CompanyCategories> getDiscountList(int limitNumber) {
        List<CompanyCategories> companyCategories = TableCompanyCategory
                .getCompanyCategoryList(2);
        Connection connection = DBConnection.getConnection();
        for (CompanyCategories companyCategory : companyCategories) {
            companyCategory.setCompanies(getDiscountCompanies(
                    companyCategory.getID(),
                    limitNumber,
                    connection));
            companyCategory.setCompanySize(companyCategory.getCompanies().size());
        }
        DBConnection.closeConnection(connection);
        return companyCategories;
    }

    /**
     * Getting newest list of each category
     * @return list of newest companies in each category
     */

    public static List<CompanyCategories> getNewestList(int limitNumber) {
        List<CompanyCategories> companyCategories = TableCompanyCategory
                .getCompanyCategoryList(2);
        Connection connection = DBConnection.getConnection();
        for (CompanyCategories companyCategory : companyCategories) {
            companyCategory.setCompanies(getNewestCompanies(
                    companyCategory.getID(),
                    limitNumber,
                    connection));
            companyCategory.setCompanySize(companyCategory.getCompanies().size());
        }
        DBConnection.closeConnection(connection);
        return companyCategories;
    }

    /**
	 * Getting company
	 * @param companyID to be got from database
	 * @return company object
	 */
	public static Company getCompany(int companyID) {
		Connection conn = DBConnection.getConnection();
		Company company = new Company();
		try {
            String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
					" WHERE comp.ID = " + companyID;
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next()) {
				fillCompany(rs, company, true);
			}
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}finally {
			if(conn != null) {
				try  {
						conn.close();
				} catch (SQLException e) {
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}	
		}
		return company;
	}

	/**
	 * Getting list of companies in a category
	 * @param categoryID to got
	 * @return list of company objects
	 */
	public static List<Company> getCompanies(int categoryID) {
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try {
            String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
					" WHERE comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.ID" +
					" ORDER BY comp.ID";
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

	/**
	 * Getting top best companies sorted by their rate for first page
	 * @param limitNumber limitation number
	 * @return list of best companies
	 */
	private static List<Company> getTopBestCompanies(int categoryID,
                                                    int limitNumber,
                                                    Connection connection) {
		List<Company> companies = new ArrayList<>();
		try {
            String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
                    " WHERE comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.ID" +
					" ORDER BY comp.COMP_RATE DESC" +
                    " LIMIT " + limitNumber;
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
		return companies;
	}

	/**
     * Getting discount companies
     * @param limitNumber number of company should be returned
     * @return list of company object
     */
    private static List<Company> getDiscountCompanies(int categoryID,
                                                      int limitNumber,
                                                      Connection connection) {
        List<Company> companies = new ArrayList<>();
        try {
            String command = "SELECT" +
                    " comp.*," +
                    " max(servdis.discount) as discount" +
                    " FROM" +
                    " companies comp," +
                    " units unit," +
                    " services serv," +
                    " servicediscount servdis" +
                    " WHERE" +
                    " comp.id = unit.comp_id" +
                    " AND unit.id = serv.unit_id" +
                    " AND serv.id = servdis.service_id" +
                    " AND unit.is_active IS TRUE" +
                    " AND serv.is_active IS TRUE" +
                    " AND comp.is_active IS TRUE" +
                    " AND servdis.is_active IS TRUE" +
                    " AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
                    " AND comp.COMP_CAT_ID = " + categoryID +
                    " GROUP BY comp.id" +
                    " ORDER BY comp.id" +
                    " LIMIT " + limitNumber;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillCompanies(rs, companies);
        }catch(SQLException e)
        {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
        return companies;
    }

    /**
     * Getting all discount companies
     * @param limitNumber number of company should be returned
     * @return list of company object
     */
    public static List<Company> getAllDiscountCompanies(int limitNumber) {
        Connection connection = DBConnection.getConnection();
        List<Company> companies = new ArrayList<>();
        try {
            String command = "SELECT" +
                    " comp.*," +
                    " max(servdis.discount) as discount" +
                    " FROM" +
                    " companies comp," +
                    " units unit," +
                    " services serv," +
                    " servicediscount servdis" +
                    " WHERE" +
                    " comp.id = unit.comp_id" +
                    " AND unit.id = serv.unit_id" +
                    " AND serv.id = servdis.service_id" +
                    " AND unit.is_active IS TRUE" +
                    " AND serv.is_active IS TRUE" +
                    " AND comp.is_active IS TRUE" +
                    " AND servdis.is_active IS TRUE" +
                    " AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
                    " GROUP BY comp.id" +
                    " ORDER BY comp.id" +
                    " LIMIT " + limitNumber;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillCompanies(rs, companies);
        }catch(SQLException e)
        {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
        DBConnection.closeConnection(connection);
        return companies;
    }


	/**
	 * Getting newest companies
	 * @param limitNumber number of company should be returned
	 * @return list of company object
	 */
	private static List<Company> getNewestCompanies(int categoryID,
                                                    int limitNumber,
                                                    Connection connection) {
		List<Company> companies = new ArrayList<>();
		try {
			String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
                    " WHERE comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.ID" +
					" ORDER BY comp.ID DESC" +
					" LIMIT " + limitNumber;
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
		return companies;
	}

	/**
	 * Getting top best companies sorted by their rate for first page
	 * @return list of best companies
	 */
	public static List<Company> getFilteredBestCompanies(int categoryID) {
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try {
            String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
					" WHERE comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.ID" +
					" ORDER BY comp.COMP_RATE DESC";
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

	/**
	 * Getting searched companies
	 * @param lon location longitude
	 * @param lat location latitude
	 * @param categoryID intended category to be searched
	 * @return list of company object
	 */
	public static List<Company> getFilteredNearestCompanies(float lat, float lon, int categoryID) {
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try {
			String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount," +
					" SQRT(POW(comp.location_lat - "+ lat +", 2) + POW(comp.location_lon - "+ lon +", 2)) AS distance" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
					" WHERE comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.ID" +
					" ORDER BY distance ASC";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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
	 * Getting filtered cheapest company
	 * @param categoryID intended category to be searched
	 * @return list of company object
	 */
	public static List<Company> getFilteredCheapestCompanies(int categoryID) {
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try {

			String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount," +
					" AVG(serv.service_price) AS price" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
					" WHERE comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.ID" +
					" ORDER BY price ASC";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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
	 * Getting filtered most expensive company
	 * @param categoryID intended category to be searched
	 * @return list of company object
	 */
	public static List<Company> getFilteredMostExpensiveCompanies(int categoryID) {
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try {

			String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount," +
					" AVG(serv.service_price) AS price" +
					" FROM" +
					" companies comp" +
					" LEFT JOIN" +
					" units unit ON comp.id = unit.comp_id" +
					" AND unit.is_active IS TRUE" +
					" LEFT JOIN" +
					" services serv ON unit.id = serv.unit_id" +
					" AND serv.is_active IS TRUE" +
					" LEFT JOIN" +
					" servicediscount servdis ON serv.id = servdis.service_id" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
					" WHERE comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.ID" +
					" ORDER BY price DESC ";

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			fillCompanies(rs, companies);
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
	 * Getting newest companies
	 * @param categoryID searched category
	 * @return list of company object
	 */
	public static List<Company> getFilteredDiscountCompanies(int categoryID) {
		Connection conn = DBConnection.getConnection();
		List<Company> companies = new ArrayList<>();
		try {
			String command = "SELECT" +
					" comp.*," +
					" max(servdis.discount) as discount" +
					" FROM" +
					" companies comp," +
					" units unit," +
					" services serv," +
					" servicediscount servdis" +
					" WHERE" +
					" comp.id = unit.comp_id" +
					" AND unit.id = serv.unit_id" +
					" AND serv.id = servdis.service_id" +
					" AND unit.is_active IS TRUE" +
					" AND serv.is_active IS TRUE" +
					" AND comp.is_active IS TRUE" +
					" AND servdis.is_active IS TRUE" +
					" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
					" AND comp.COMP_CAT_ID = " + categoryID +
					" GROUP BY comp.id" +
					" ORDER BY comp.id";
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


    /**
     * Getting searched companies
     * @param companyName name of company to be searched
     * @param categoryID intended category to be searched
     * @param lat location latitude
     * @param lon location longitude
     * @param serviceName searched service name
     * @return list of company object
     */
    public static List<Company> getSearchedCompanies(String companyName,
                                                     String serviceName,
                                                     float lat,
                                                     float lon,
                                                     int categoryID) {
        Connection conn = DBConnection.getConnection();
        List<Company> companies = new ArrayList<>();
        try {
			serviceName = getFilterAndSearchServiceName(serviceName);
			companyName = getFilterAndSearchCompanyName(companyName);

            String middleQuery = "";

            String command = getSearchAndFilterCommand(
                    companyName,
                    serviceName,
                    lat,
                    lon,
                    categoryID,
                    middleQuery,
                    getFilterAndSearchOrder(companyName, serviceName, lat, lon));
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillCompanies(rs, companies);
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
     * Getting best searched companies
     * @param companyName name of company to be searched
     * @param categoryID intended category to be searched
     * @param lat location latitude
     * @param lon location longitude
     * @param serviceName searched service name
     * @return list of company object
     */
    public static List<Company> getFilteredSearchedCompanies(String companyName,
                                                             String serviceName,
                                                             float lat,
                                                             float lon,
                                                             int categoryID,
                                                             FilterItem filterItem) {
        Connection conn = DBConnection.getConnection();
        List<Company> companies = new ArrayList<>();
        try {
            serviceName = getFilterAndSearchServiceName(serviceName);
            companyName = getFilterAndSearchCompanyName(companyName);

            String middleQuery = "";

            //Getting second sorting order based on filter item
            String filterSecondaryOrder = "";
            switch (filterItem) {
                case BEST:
                    filterSecondaryOrder = " , comp.comp_rate DESC";
                    break;
                case CHEAPEST:
                    filterSecondaryOrder = " , price ASC";
                    break;
                case EXPENSIVE:
                    filterSecondaryOrder = " , price DESC";
                    break;
				case DISCOUNT:
					filterSecondaryOrder = " , discount DESC";
					middleQuery = " AND servdis.is_active IS TRUE ";
                default:
                    break;
            }

            String order = getFilterAndSearchOrder(companyName, serviceName, lat, lon) + filterSecondaryOrder;

            String command = getSearchAndFilterCommand(
                    companyName,
                    serviceName,
                    lat,
                    lon,
                    categoryID,
                    middleQuery,
                    order);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillCompanies(rs, companies);
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
	 * Getting number of company in a category
	 * @param catID category id
	 * @return company numbers
	 */
	static int getCompanyNumbers(int catID) {
		String command = "SELECT" +
                " COUNT(*)" +
                " FROM" +
                " COMPANIES" +
                " WHERE" +
                " IS_ACTIVE IS TRUE AND COMP_CAT_ID = " + catID;
		Connection conn = DBConnection.getConnection();
		int count = 0;
		try {
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next()) {
				count =  rs.getInt(1);
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
		return count;
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
			preparedStatement.setString(11, company.getLogoURL());
			preparedStatement.setString(12, company.getLocality());
			preparedStatement.setInt(13, company.getCityID());
			preparedStatement.setInt(14, company.getDistrictID());
			preparedStatement.setBoolean(15, company.isActive());
			preparedStatement.setString(16, company.getCommercialCode());
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillCompany(ResultSet resultSet, Company company, boolean isUnitSet){
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
			company.setLogoURL(resultSet.getString(12));
			company.setLocality(resultSet.getString(13));
			company.setCityID(resultSet.getInt(14));
			company.setDistrictID(resultSet.getInt(15));
			company.setActive(resultSet.getBoolean(16));
			company.setCommercialCode(resultSet.getString(17));
			company.setDiscount(resultSet.getInt("discount"));
			if (isUnitSet)
			    company.setUnits(TableUnit.getUnits(company.getID(), true));
			company.setCompanyPictures(TableCompanyPicture.getCompanyPictures(company.getID()));
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillCompanies(ResultSet resultSet,
                                      List<Company> companies){
		try{
			while (resultSet.next()){
				Company company = new Company();
				fillCompany(resultSet, company, false);
				companies.add(company);
			}
		}catch (SQLException e){
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}


    /**
     * Getting command for search and filter request
     * @param companyName company name
     * @param serviceName service name
     * @param lat location latitude
     * @param lon location longitude
     * @param categoryID searched or filtered category id
     * @param middleQuery middle query determined by search type
     * @param order ordering sort determined by filter or search type
     * @return filter or search command
     */
	private static String getSearchAndFilterCommand (String companyName,
                                                     String serviceName,
                                                     float lat,
                                                     float lon,
                                                     int categoryID,
                                                     String middleQuery,
                                                     String order) {
        return "SELECT" +
				" comp.*," +
				" (match(comp.comp_name) against ('" + companyName + "' IN BOOLEAN MODE)" +
				" + match(serv.service_name) against ('" + serviceName + "' IN BOOLEAN MODE))/2 as point," +
				" SQRT(POW(comp.location_lat - "+ lat + ", 2) + POW(comp.location_lon - "+ lon +", 2)) AS distance," +
				" AVG(serv.service_price) AS price," +
				" max(servdis.discount) as discount," +
				" AVG(serv.service_price) AS price" +
				" FROM" +
				" companies comp" +
				" LEFT JOIN" +
				" units unit ON comp.id = unit.comp_id" +
				" AND unit.is_active IS TRUE" +
				" LEFT JOIN" +
				" services serv ON unit.id = serv.unit_id" +
				" AND serv.is_active IS TRUE" +
				" LEFT JOIN" +
				" servicediscount servdis ON serv.id = servdis.service_id" +
				" AND servdis.is_active IS TRUE" +
				" AND NOW() BETWEEN servdis.create_date AND servdis.expire_date" +
				" WHERE comp.COMP_CAT_ID = " + categoryID +
				middleQuery +
				" GROUP BY comp.id" +
				" ORDER BY "+ order;
    }

	/**
	 * Getting order for filter and search requests sorting data
	 * @param companyName search item
	 * @param serviceName search item
	 * @param lat location latitude
	 * @param lon location longitude
	 * @return order for sorting data
	 */
    private static String getFilterAndSearchOrder(String companyName,
												  String serviceName,
												  float lat,
												  float lon) {
		String order = "id DESC";
		//If no location is entered
		if(lat == 0 || lon == 0) {
			//If service and company names are not entered
			if (!companyName.equals("") || !serviceName.equals("")) {
				order = "point DESC";
			}
		} else {
			if (!companyName.equals("") || !serviceName.equals("")) {
				order = "point * distance DESC";
			}
			else
				order = "distance DESC";
		}
		return order;
	}

	private static String getFilterAndSearchCompanyName (String companyName) {
		return companyName == null ? ""
				:  (companyName.equals("") ? companyName : "*" + companyName + "*");
	}

	private static String getFilterAndSearchServiceName (String serviceName) {
    	return serviceName == null ? ""
				: (serviceName.equals("") ? serviceName : "*" + serviceName + "*") ;
	}


}