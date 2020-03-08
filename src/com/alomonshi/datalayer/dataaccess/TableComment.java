package com.alomonshi.datalayer.dataaccess;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.ReserveTimeStatus;
import com.alomonshi.object.tableobjects.Comments;
import com.alomonshi.object.tableobjects.Services;

public class TableComment {

	/**
	 * Insert new comment
	 * @param comment to be inserted
	 * @return result
	 */
	public static boolean insertComment(Comments comment){
		Connection connection = DBConnection.getConnection();
		String command = "insert into COMMENTS (" +
				"CLIENT_ID" +
				", RES_TIME_ID" +
				", COMMENT" +
				", REPLY_COMMENT" +
				", SERVICE_RATE" +
				", IS_ACTIVE" +
				", COMMENT_DATE" +
				", REPLY_DATE" +
				") values(?, ?, ?, ?, ?, ?, ?, ?)";
		boolean response = executeInsertUpdate(comment, command, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * update a comment object
	 * @param comment to be updated
	 * @return result
	 */
	public static boolean updateComment(Comments comment){
		Connection connection = DBConnection.getConnection();
		String command = "UPDATE COMMENTS SET " +
				"CLIENT_ID = ?" +
				", RES_TIME_ID = ?" +
				", COMMENT = ?" +
				", REPLY_COMMENT = ?" +
				", SERVICE_RATE = ?" +
				", IS_ACTIVE = ?" +
				", COMMENT_DATE = ?" +
				", REPLY_DATE = ?" +
				" where" +
				" id = " + comment.getID();
		boolean response = executeInsertUpdate(comment, command, connection);
		DBConnection.closeConnection(connection);
		return response;
	}

	/**
	 * Delete a comment from database(set is_active field to false)
	 * @param comment to be deleted
	 * @return result
	 */
	public static boolean delete(Comments comment){
		comment.setActive(false);
		return updateComment(comment);
	}


	/**
	 * Executing insert update in database
	 * @param comment to be executed
	 * @param command update or insert command
	 * @param connection objecd injected from JDBC
	 * @return result
	 */
	private static boolean executeInsertUpdate(Comments comment, String command, Connection connection)
	{
		try {
			PreparedStatement ps = connection.prepareStatement(command);
			prepare(comment, ps);
			int i = ps.executeUpdate();
			return i == 1;
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return false;
		}
	}

	/**
	 * Getting a comment from database
	 * @param ID to be got from database
	 * @return comment object
	 */
	public static Comments getComment(int ID) {
		Connection conn = DBConnection.getConnection();
		Comments comment = new Comments();
		try {
			Statement stmt =conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" COMMENTS" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND ID = " + ID;
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				fillComment(rs, comment);
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
		return comment;
	}
	
	public static Comments getCommentByResTimeID(int resTimeID)
	{
		Connection conn = DBConnection.getConnection();
		Comments comment = new Comments();
		try
		{
			Statement stmt =conn.createStatement();
			String command = "SELECT" +
					" *" +
					" FROM" +
					" COMMENTS" +
					" WHERE" +
					" IS_ACTIVE IS TRUE AND RES_TIME_ID = " + resTimeID;
			ResultSet rs = stmt.executeQuery(command);
			while (rs.next()) {
				fillComment(rs, comment);
			}
			return comment;
		}catch(SQLException e)
		{
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
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
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}	
		}
	}

	/**
	 * Get unit comments
	 * @param unitID which comments to be got
	 * @return lsit of unit comments
	 */

	public static List<Comments> getUnitComments(int unitID)
	{
		Connection conn = DBConnection.getConnection();
		List<Comments> comments = new LinkedList<>();
		try
		{
			Statement stmt = conn.createStatement();
			String command = "SELECT" +
					" rt.ID as reserveTimeID," +
					" com.ID as commentID," +
					" com.comment as comment," +
					" com.COMMENT_DATE as commentDate," +
					" com.reply_comment as replyComment," +
					" com.REPLY_DATE as replyDate," +
					" com.SERVICE_RATE as serviceRate," +
					" cl.NAME as clientName" +
					" FROM" +
					" alomonshi.comments com," +
					" alomonshi.reservetimes rt," +
					" alomonshi.clientinfo cl," +
					" alomonshi.units unit" +
					" where" +
					" com.RES_TIME_ID = rt.id" +
					" and rt.UNIT_ID = unit.id" +
					" and com.CLIENT_ID = cl.id" +
					" and unit.ID = " + unitID +
					" and com.IS_ACTIVE is true " +
					" AND rt.STATUS = " + ReserveTimeStatus.RESERVED.getValue() +
					" ORDER BY com.COMMENT_DATE DESC";
			ResultSet rs = stmt.executeQuery(command);
			fillUnitComments(rs, comments);
			return comments;
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
			return comments;
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
	 * Getting client comments
	 * @param clientID which its comments to be got
	 * @return list of comments
	 */
	
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
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
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
					Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
				}
			}
		}
	}

	/**
	 * Prepare object to be executed
	 * @param comment which object to be prepared with
	 * @param ps JDBC  prepared statement to be prepared
	 */

	private static void prepare(Comments comment, PreparedStatement ps) {
		try{
			ps.setInt(1, comment.getClientID());
			ps.setInt(2, comment.getReserveTimeID());
			ps.setString(3, comment.getComment());
			ps.setString(4, comment.getReplyComment());
			ps.setFloat(5, comment.getServiceRate());
			ps.setBoolean(6, comment.isActive());
			ps.setObject(7,comment.getCommentDate());
			ps.setObject(8, comment.getReplyDate());
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillComment(ResultSet rs, Comments comment) {
		try{
			comment.setID(rs.getInt(1));
			comment.setClientID(rs.getInt(2));
			comment.setReserveTimeID(rs.getInt(3));
			comment.setComment(rs.getString(4));
			comment.setReplyComment(rs.getString(5));
			comment.setServiceRate(rs.getFloat(6));
			comment.setActive(rs.getBoolean(7));
			comment.setCommentDate(rs.getObject(8, LocalDateTime.class));
			comment.setServiceNames(Objects.requireNonNull(TableReserveTimeServices
					.getServices(comment.getReserveTimeID()))
					.stream()
					.map(Services::getServiceName)
					.collect(Collectors.toList()));
			comment.setReplyDate(rs.getObject(9, LocalDateTime.class));
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
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
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillUnitComment(ResultSet resultSet, Comments comment) {
		try {
			comment.setReserveTimeID(resultSet.getInt("reserveTimeID"));
			comment.setID(resultSet.getInt("commentID"));
			comment.setComment(resultSet.getString("comment"));
			comment.setCommentDate(resultSet.getObject("commentDate", LocalDateTime.class));
			comment.setReplyComment(resultSet.getString("replyComment"));
			comment.setReplyDate(resultSet.getObject("replyDate", LocalDateTime.class));
			comment.setServiceRate(resultSet.getFloat("serviceRate"));
			comment.setClientName(resultSet.getString("clientName"));
			comment.setServiceNames(Objects.requireNonNull(TableReserveTimeServices
					.getServices(comment.getReserveTimeID()))
					.stream()
					.map(Services::getServiceName)
					.collect(Collectors.toList()));
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}

	private static void fillUnitComments(ResultSet resultSet, List<Comments> comments) {
		try {
			while (resultSet.next()) {
				Comments comment = new Comments();
				fillUnitComment(resultSet, comment);
				comments.add(comment);
			}
		}catch(SQLException e) {
			Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
		}
	}
}