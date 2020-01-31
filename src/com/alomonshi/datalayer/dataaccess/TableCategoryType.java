package com.alomonshi.datalayer.dataaccess;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.CategoryTypes;

public class TableCategoryType {
	
	private int typeID;
	Connection conn;
	
	public TableCategoryType()
	{
		conn = DBConnection.getConnection();
	}
	
	public TableCategoryType setTypeID(int typeID)
	{
		conn = DBConnection.getConnection();
		this.typeID = typeID;
		return this;
	}
	
	public boolean setName(String typeName)
	{
		String command="insert into CATTYPES(TYPE_NAME) values(?)";
		 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, typeName);
			int i=ps.executeUpdate();
			return i==1?true:false;
			
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
	
	public List<CategoryTypes> getCatTypeList()
	{
		String command="select * from CATTYPES";
		List<CategoryTypes> types = new ArrayList<CategoryTypes>();
		 
		try
		{
			PreparedStatement ps =conn.prepareStatement(command);
			ResultSet rs=ps.executeQuery(command);
			while(rs.next())
			{
				CategoryTypes type = new CategoryTypes();
				type.setID(rs.getInt(1));
				type.setTypeName(rs.getString(2));
				type.setCompanyCats(TableCompanyCategory.getCompanyCategoryList(type.getID()));
				types.add(type);
			}
			return types;
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
	
	public CategoryTypes getCatTypes()
	{
		String command="select * from CATTYPES WHERE ID = " + typeID;
		 
		try
		{
			PreparedStatement ps =conn.prepareStatement(command);
			ResultSet rs=ps.executeQuery(command);
			CategoryTypes type = new CategoryTypes();
			while(rs.next())
			{
				type.setID(rs.getInt(1));
				type.setTypeName(rs.getString(2));
				type.setCompanyCats(TableCompanyCategory.getCompanyCategoryList(type.getID()));
				return type;
			}
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
		return null;
	}
	
	public String getName()
	{
		String command="select TYPE_NAME from CATTYPES where id = " + typeID;
		 
		try
		{
			PreparedStatement ps =conn.prepareStatement(command);
			ResultSet rs=ps.executeQuery(command);
			while(rs.next())
			{
				return rs.getString(1);
			}
			
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
		return null;
	}
	
	public int getID(String typeName)
	{
		String command="select id from CATTYPES where TYPE_NAME = '" + typeName + "'";
		 
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
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
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
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}	
		}
		return 0;
	}
	
	public int getTypeNumbers()
	{
		String command="select id from CATTYPES";
		 
		try
		{
			PreparedStatement ps =conn.prepareStatement(command);
			ResultSet rs=ps.executeQuery(command);
			int count = 0;
			while(rs.next())
			{
				count++;
			}
			return count;
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
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
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}	
		}
	}
}
