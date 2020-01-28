package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.CompanyCategories;

public class TableCompanyCategory {

	public static boolean insert(CompanyCategories companyCategories){
		String command = "insert into COMPANYCATS(CAT_NAME) values(?)";
		return executeInsertUpdateCommand(companyCategories, command);
	}

	public static boolean update(CompanyCategories companyCategory){
		String command = "update COMPANYCATS set CAT_NAME = ?, IS_ACTIVE = ?";
		return executeInsertUpdateCommand(companyCategory, command);
	}

	public static boolean delete(CompanyCategories companyCategories){
		companyCategories.setActive(false);
		String command = "update COMPANYCATS set CAT_NAME = ?, IS_ACTIVE = ?";
		return executeInsertUpdateCommand(companyCategories, command);
	}

    private static boolean executeInsertUpdateCommand(CompanyCategories companyCats, String command)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
            PreparedStatement ps = conn.prepareStatement(command);
            prepare(ps, companyCats);
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
	
	public static List<CompanyCategories> getCompanyCategoryList(int typeID)
	{
		String command="select * from COMPANYCATS WHERE CAT_TYPE = " + typeID + " AND IS_ACTIVE IS TRUE ";
		List<CompanyCategories> companyCategories = new ArrayList<>();
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ResultSet rs = ps.executeQuery(command);
            fillCompanyCategories(rs, companyCategories);
			return companyCategories;
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
	
	public static CompanyCategories getCompanyCategory(int categoryID)
	{
		String command="select * from COMPANYCATS WHERE ID = " + categoryID + " AND IS_ACTIVE IS TRUE ";
		Connection conn = DBConnection.getConnection();
        CompanyCategories category = new CompanyCategories();
		try
		{
			PreparedStatement ps =conn.prepareStatement(command);
			ResultSet rs = ps.executeQuery(command);
			while (rs.next()){
				fillCompanyCategory(rs, category);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
			return category;
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
		return category;
	}
	
	public static String getName(int catID)
	{
		String command="select CAT_NAME from COMPANYCATS where id = " + catID + " AND IS_ACTIVE IS TRUE ";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ResultSet rs = ps.executeQuery(command);
			while(rs.next())
			{
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
	
	public static int getID(String catName)
	{
		String command="select id from COMPANYCATS where CAT_NAME = '" + catName + "'" + " AND IS_ACTIVE IS TRUE ";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps =conn.prepareStatement(command);
			ResultSet rs=ps.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1);
			}
			
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
		return 0;
	}

	private static void prepare(PreparedStatement ps, CompanyCategories companyCats){
	    try{
	        ps.setString(1, companyCats.getCategoryName());
        }catch (SQLException e){
	        e.printStackTrace();
        }
	}

	private static void fillCompanyCategory(ResultSet resultSet, CompanyCategories companyCats){
	    try{
			companyCats.setID(resultSet.getInt(1));
			companyCats.setCategoryName(resultSet.getString(2));
			companyCats.setActive(resultSet.getBoolean(3));
			companyCats.setCompanySize(TableCompanies.getCompanyNumbers(companyCats.getID()));
        }catch (SQLException e){
	        e.printStackTrace();
        }
    }

    private static void fillCompanyCategories(ResultSet resultSet, List<CompanyCategories> companyCats){
        try{
            while (resultSet.next()){
                CompanyCategories companyCat = new CompanyCategories();
                fillCompanyCategory(resultSet, companyCat);
                companyCats.add(companyCat);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
