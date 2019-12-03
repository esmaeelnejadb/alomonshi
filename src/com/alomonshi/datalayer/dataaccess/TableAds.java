package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableAds {

	private int companyID;
	Connection conn;

	public TableAds() {
		conn = DBConnection.getConnection();
	}

	public TableAds setCompanyID(int companyID)
	{
		this.companyID = companyID;
		return this;
	}


	/**
	 * Author Behzad
	 * Inserting new ads to database
	 * @param url picture url
	 * @param caption ad caption
	 * @param date date of upload
	 * @return true if process has been done properly
	 */
	public boolean insertAds(String url, String caption, Date date)
	{
		try
		{
			String command="insert into ADS (COMP_ID, URL, CAPTION, DATE) values(?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, companyID);
			ps.setString(2, url);
			ps.setString(3, caption);
			ps.setDate(4, date);
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

	public String getURL() {
		String command="select URL from ADS where COMP_ID =" + companyID;

		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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

	public String getCaption() {
		String command="select CAPTION from ADS where COMP_ID =" + companyID;

		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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

	public Date getDate() {
		String command="select DATE from ADS where COMP_ID =" + companyID;

		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getDate(1);
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

	public boolean setURL(String url)
	{
		String command="update ADS set URL = ? where COMP_ID = ?";

		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, url);
			ps.setInt(2, companyID);
			int i=ps.executeUpdate();
			return i==1?true:false;

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

	public boolean setCaption(String caption)
	{
		String command="update ADS set CAPTION = ? where COMP_ID = ?";

		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, caption);
			ps.setInt(2, companyID);
			int i=ps.executeUpdate();
			return i==1?true:false;

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

	public boolean setDaten(Date date)
	{
		String command="update ADS set DATE = ? where COMP_ID = ?";
		 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setDate(1, date);
			ps.setInt(2, companyID);
			int i=ps.executeUpdate();
			return i==1?true:false;
			
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
}
