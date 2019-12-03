package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.Comments;

public class TableComment {

	public static boolean insertComment(Comments comment){
		String command = "insert into COMMENTS (CLIENT_ID, UNIT_ID, RES_TIME_ID, COMMENT" +
				", REPLY_COMMENT, SERVICE_RATE, IS_ACTIVE, COMMENT_DATE) values(?, ?, ?, ?, ?, ?, ?, ?)";
		return executeInsertUpdate(comment, command);
	}

	public static boolean updateComment(Comments comment){
		String command = "UPDATE COMMENTS SET CLIENT_ID = ?, UNIT_ID = ?, RES_TIME_ID = ?, COMMENT = ?" +
				", REPLY_COMMENT = ?, SERVICE_RATE = ?, IS_ACTIVE = ?, COMMENT_DATE = ?";
		return executeInsertUpdate(comment, command);
	}

	public static boolean delete(Comments comment){
		comment.setIsACtive(false);
		return updateComment(comment);
	}
	
	//importing users data into database
	private static boolean executeInsertUpdate(Comments comment, String command)
	{		
		Connection conn = DBConnection.getConnection();
		try
		{
			PreparedStatement ps = conn.prepareStatement(command);
			prepare(comment, ps);
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
	
	public static Comments getCommentByResTimeID(int resTimeID)
	{
		Connection conn = DBConnection.getConnection();
		Comments comment = new Comments();
		try
		{
			Statement stmt =conn.createStatement();
			String command = "SELECT * FROM COMMENTS where IS_ACTIVE IS TRUE AND RES_TIME_ID = " + resTimeID;
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				fillComment(rs, comment);
			}
			return comment;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return comment;
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
	
	public static List<Comments> getCommentsOfCompany(int companyID)
	{
		Connection conn = DBConnection.getConnection();
		List<Comments> comments = new LinkedList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT * FROM COMMENTS where IS_ACTIVE IS TRUE AND UNIT_ID IN (SELECT ID FROM units WHERE COMP_ID =  " + companyID + ")";
			ResultSet rs=stmt.executeQuery(command);
			fillComments(rs, comments);
			return comments;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return comments;
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

	public static List<Comments> getUnitComments(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		List<Comments> comments = new LinkedList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT * FROM COMMENTS WHERE IS_ACTIVE IS TRUE AND UNIT_ID = " + unitID;
			ResultSet rs=stmt.executeQuery(command);
			fillComments(rs, comments);
			return comments;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return comments;
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
	
	public static List<Comments> getClientComments(int clientID)
	{
		Connection conn = DBConnection.getConnection();
		List<Comments> comments = new LinkedList<>();
		try
		{
			Statement stmt =conn.createStatement();
			String command="SELECT * FROM COMMENTS where IS_ACTIVE IS TRUE AND CLIENT_ID = " + clientID;
			ResultSet rs=stmt.executeQuery(command);
			fillComments(rs, comments);
			return comments;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return comments;
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

	private static void fillComment(ResultSet rs, Comments comment) {
		try{
			comment.setID(rs.getInt(1));
			comment.setClientID(rs.getInt(2));
			comment.setUnitID(rs.getInt(3));
			comment.setResTimeID(rs.getInt(4));
			comment.setComment(rs.getString(5));
			comment.setReplyComment(rs.getString(6));
			comment.setServiceRate(rs.getFloat(7));
			comment.setIsACtive(rs.getBoolean(8));
			comment.setCommentDate(rs.getString(9));
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private static void fillComments(ResultSet resultSet, List<Comments> comments) {
		try{
			while (resultSet.next()){
				Comments comment = new Comments();
				fillComment(resultSet, comment);
				comments.add(comment);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

	private static void prepare(Comments comment, PreparedStatement ps) {
		try{
			ps.setInt(1, comment.getClientID());
			ps.setInt(2, comment.getUnitID());
			ps.setInt(3, comment.getResTimeID());
			ps.setString(4, comment.getComment());
			ps.setString(5, comment.getReplyComment());
			ps.setFloat(6, comment.getServiceRate());
			ps.setBoolean(7, comment.getIsACtive());
			ps.setString(8,comment.getCommentDate());
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}