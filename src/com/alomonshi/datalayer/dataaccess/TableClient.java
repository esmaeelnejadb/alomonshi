package com.alomonshi.datalayer.dataaccess;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alomonshi.bussinesslayer.tableutils.ClientUtils;
import com.alomonshi.bussinesslayer.tableutils.RestimeServiceUtils;
import com.alomonshi.object.*;

public abstract class TableClient {
	
	protected int user_id;
		
	public TableClient() {
	}


	/**
	 * Inserting new user
	 * Author Behzad
	 * @param info new user information
	 * @return true if process has been done properly
	 */
	public boolean insertNewUser(Users info)
	{
		Connection conn = DBConnection.getConnection();
		try
		{
			String command = "insert into CLIENTINFO (user_id, name, botusername, username, password, phonNo, email, user_action, client_stat) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";	
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, info.getUserID());
			ps.setString(2, info.getName());
			ps.setString(3, info.getBotUsername());
			ps.setString(4, info.getUsername());
			ps.setString(5, info.getPassword());
			ps.setString(6, info.getPhoneNo());
			ps.setString(7, info.getEmail());
			ps.setInt(8, info.getUserAction());
			ps.setInt(9, info.getStatus());
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


	/**
	 * Author Behzad
	 * Setting name of the client
	 * @param name name of the client
	 * @return true if process has been done properly
	 */

	public boolean setName(String name)
	{
		String command="update CLIENTINFO set name=? where USER_ID =?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, name);
			ps.setInt(2, (int)user_id);
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

	//Setting the user status

	/**
	 * Author Behzad
	 * Setting status of the client
	 * @param status status of the client
	 * @return true if process has been done properly
	 */

	public boolean setStatus(int status)
	{
		String command="update CLIENTINFO set client_stat = ? where USER_ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, status);
			ps.setInt(2, (int)user_id);
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
	
	//Set message ID

	/**
	 * Author Behzad
	 * Setting message id for registeration of new user
	 * @param message_id sent message id
	 * @return true if process has been done properly
	 */

	public boolean setMessageID(int message_id)
	{
		String command="update CLIENTINFO set message_id=? where USER_ID =?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, message_id);
			ps.setInt(2, (int)user_id);
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
	 * Setting email of new client
	 * @param email email of new client
	 * @return true if process has been done properly
	 */

	public boolean setEmail(String email)
	{
		String command="update CLIENTINFO set email=? where USER_ID =?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, email);
			ps.setInt(2, user_id);
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
	
	//Setting the password

	/**
	 * Author Behzad
	 * Setting password of client
	 * @param password password of client
	 * @return true if process has been done properly
	 */
	public boolean setPassword(String password)
	{
		String command="update CLIENTINFO set password=? where USER_ID =?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setString(1, password);
			ps.setInt(2, user_id);
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
	 * Getting user by user id
	 * @return user
	 */
	
	public Users getUser() {
		String command="select * from CLIENTINFO where USER_ID = " + user_id;
		Connection conn = DBConnection.getConnection();
		Users user=new Users();
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				user.setID(rs.getInt(1));
				user.setUserID(rs.getInt(2));
				user.setName(rs.getString(4));
				user.setBotUsername(rs.getString(5));
				user.setUsername(rs.getString(6));
				user.setPassword(rs.getString(7));
				user.setPhoneNo(rs.getString(8));
				user.setEmail(rs.getString(9));				
				user.setUserAction(rs.getInt(10));
				user.setStatus(rs.getInt(11));
				return user;
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

	/**
	 * Author Behzad
	 * Getting name of the user by user if
	 * @return name of the user
	 */
	public String getName()
	{
		String command="select name from CLIENTINFO where USER_ID = " + user_id;
		Connection conn = DBConnection.getConnection(); 
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

	/**
	 * Getting password of the user
	 * @return password of the user
	 */

	public String getPassword()
	{
		String command="select password from CLIENTINFO where USER_ID = " + user_id;
		Connection conn = DBConnection.getConnection(); 
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
					return null;
				}
			}	
		}
		return null;
	}

	/**
	 * Getting phone number of the user
	 * @return phone number of the user
	 */
	public long getPhoneNo()
	{
		String command="select phonNo from CLIENTINFO where USER_ID = " + user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getLong(1);
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
					return 0;
				}
			}	
		}
		return 0;
	}
	
	//Getting the unit to be modified
	public int getMessageID()
	{
		String command="select message_id from CLIENTINFO where USER_ID = " + user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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
					return 0;
				}
			}	
		}
		return 0;
	}
	
	public String getEmail()
	{
		String command="select email from CLIENTINFO where USER_ID =" + user_id;
		Connection conn = DBConnection.getConnection(); 
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
					return null;
				}
			}	
		}
		return null;
	}
	
	public int getUserAction()
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select user_action from CLIENTINFO where USER_ID ="+user_id;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					return 0;
				}
			}	
		}
		return 0;
	}
	
	public int getStatus()
	{
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select client_stat from CLIENTINFO where USER_ID = " + user_id;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					return 0;
				}
			}	
		}
		return 0;
	}

	
	public boolean deleteUser() {
		String command="delete from CLIENTINFO where USER_ID = ?";
		Connection conn = DBConnection.getConnection(); 
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			ps.setInt(1, (int)user_id);
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

	
	public int getUserIDByPhone(String phoneNo) {
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select USER_ID from CLIENTINFO WHERE PHONNO = " + phoneNo;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					return 0;
				}
			}	
		}
		return 0;	
	}
	
	public int getClientUserID(String phoneNo) {
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select USER_ID from CLIENTINFO WHERE PHONNO = " + phoneNo;
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					return 0;
				}
			}	
		}
		return 0;	
	}
	
	public int getIDByBotUsername(String useranme) {
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select ID from CLIENTINFO where botusername= '" + useranme + "'";
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					return 0;
				}
			}	
		}
		return 0;	
	}
	
	public int getUserIDByBotUsername(String useranme) {
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			String command="select user_id from CLIENTINFO where botusername= '" + useranme + "'";
			ResultSet rs=stmt.executeQuery(command);
			while(rs.next())
			{
				return rs.getInt(1);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
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
					return 0;
				}
			}	
		}
		return 0;	
	}
		
	
	public int getCompanyCatID() {
		String command="select category_id from CLIENTINFO where USER_ID = " + user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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
	
	public int getCompanyID() {
		String command="select comp_id from CLIENTINFO where USER_ID = " + user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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

	
	public int getUnitID() {
		String command="select unit_id from CLIENTINFO where USER_ID ="+user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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

	
	public List<Integer> getServieIDs() {
		String command="select service_id_1,service_id_2,service_id_3 from CLIENTINFO where USER_ID ="+user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
			List<Integer> serviceIDs = new ArrayList<Integer>();
			while(rs.next())
			{
				if(rs.getInt(1)!=0)
					serviceIDs.add(rs.getInt(1));
				if(rs.getInt(2)!=0)
					serviceIDs.add(rs.getInt(2));
				if(rs.getInt(3)!=0)
					serviceIDs.add(rs.getInt(3));
			}
			return serviceIDs;
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
					return null;
				}
			}	
		}
	}

	
	public int getDateID() {
		String command="select date_id from CLIENTINFO where USER_ID ="+user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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
	
	public int getRestimeID() {
		String command="select restime_id from CLIENTINFO where USER_ID ="+user_id;
		Connection conn = DBConnection.getConnection(); 
		try
		{
			Statement stmt =conn.createStatement();
			ResultSet rs=stmt.executeQuery(command);
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
	
	public String getReserveCode() {
		String command="select reserve_code from CLIENTINFO where USER_ID =" + user_id;
		Connection conn = DBConnection.getConnection(); 
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
	
	//Getting client list
	public void getClientList(String FilePath, int companyID)
	throws SQLException, FileNotFoundException, IOException
	{
		// Create new Excel workbook and sheet
		HSSFWorkbook xlsWorkbook = new HSSFWorkbook();
		HSSFSheet xlsSheet = xlsWorkbook.createSheet();
		short rowIndex = 0;
		// Get the list of column names and store them as the first
			// row of the spreadsheet.
			List<String> colNames = new ArrayList<String>();
			HSSFRow titleRow = xlsSheet.createRow(rowIndex++);
			String col1 = "شناسه مشتری";
			colNames.add(col1);
			String col2 = "نام مشتری";
			colNames.add(col2);
			String col3 = "نام کاربری";
			colNames.add(col3);
			String col4 = "شماره موبایل";
			colNames.add(col4);
			String col5 = "پست الکترونیک";
			colNames.add(col5);
		  
			for (int i = 1; i <= colNames.size(); i++) 
			{
				titleRow.createCell((short) (i-1)).setCellValue(new HSSFRichTextString(colNames.get(i-1)));
				xlsSheet.setColumnWidth((short) (i-1), (short) 4000);  
			}
			// Save all the data from the database table rows
			LinkedHashSet<Integer> hs = new RestimeServiceUtils().getClientInACompany(companyID);
			List<Integer> clientIDs  = new ArrayList<Integer>();
			clientIDs.addAll(hs);
			String coldata = new String();
			for (int i = 0 ; i < clientIDs.size() ; i++)
			{
				HSSFRow dataRow = xlsSheet.createRow(rowIndex++);
				short colIndex = 0;
				ClientUtils client = new ClientUtils();
				client.setUserID(clientIDs.get(i));
				for (int j = 0 ; j < colNames.size() ; j++)
				{
					switch(j)
					{
					case 0 :
						coldata = Integer.toString(clientIDs.get(i));
						break;
					case 1 :
						coldata = client.getName();
						break;
					case 2 :
//						coldata = client.getBotUsername();
						coldata = "";
						break;
					case 3 :
						coldata = Long.toString(client.getPhoneNo());
						break;
					case 4 :
						coldata = client.getEmail();
						break;
					}
					dataRow.createCell(colIndex++).setCellValue(new HSSFRichTextString(coldata));
				}
			}
	    // Write to disk
		xlsWorkbook.write(new FileOutputStream(FilePath));
		xlsWorkbook.close();
	}
}
