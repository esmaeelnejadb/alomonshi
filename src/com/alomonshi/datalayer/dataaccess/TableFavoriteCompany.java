package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.FavoriteCompany;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableFavoriteCompany {

    /**
     * Inserting a favorite company into database
     * @param favoriteCompany to be inserted into database
     * @return result
     */
    public static boolean insertFavoriteCompany (FavoriteCompany favoriteCompany) {
        Connection connection = DBConnection.getConnection();
        String insertCommand = "INSERT INTO FAVORITECOMPANIES(" +
                "ID," +
                "COMPANY_ID," +
                "CLIENT_ID," +
                "IS_ACTIVE" +
                ") VALUES(?, ?, ?, ?)";
        boolean result = executeInsertUpdate(favoriteCompany, insertCommand, connection);
        DBConnection.closeConnection(connection);
        return result;
    }

    /**
     * update favorite company into database
     * @param favoriteCompany to be updated
     * @return result
     */
    private static boolean updateFavoriteCompany(FavoriteCompany favoriteCompany) {
        Connection connection = DBConnection.getConnection();
        String updateCommand = "UPDATE FAVORITECOMPANIES SET " +
                "ID = ?," +
                "COMPANY_ID = ?, " +
                "CLIENT_ID = ?, " +
                "IS_ACTIVE = ? " +
                "WHERE ID = ";
        boolean result = executeInsertUpdate(
                favoriteCompany,
                updateCommand + favoriteCompany.getID(),
                connection);
        DBConnection.closeConnection(connection);
        return result;
    }

    /**
     * Delete favorite company
     * @param favoriteCompany to be deleted
     * @return result
     */
    public static boolean deleteFavoriteCompany (FavoriteCompany favoriteCompany) {
        favoriteCompany.setActive(false);
        return updateFavoriteCompany(favoriteCompany);
    }

    /**
     * Insert or update object into database
     * @param favoriteCompany to be inserted or updated
     * @param command to be executed
     * @param connection JDBC connection
     * @return result
     */
    private static boolean executeInsertUpdate (
            FavoriteCompany favoriteCompany,
            String command,
            Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement(command);
            prepare(ps, favoriteCompany);
            int i = ps.executeUpdate();
            return i == 1;
        }catch(SQLException e)
        {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            return false;
        }
    }

    /**
     * Get a favorite company
     * @param favoriteCompany to be got from database
     */
    public static void getFavoriteCompany(FavoriteCompany favoriteCompany) {
        Connection connection = DBConnection.getConnection();
        try {
            Statement stmt = connection.createStatement();
            String command = "SELECT" +
                    " * " +
                    " FROM" +
                    " favoritecompanies " +
                    " WHERE CLIENT_ID = " + favoriteCompany.getClientID() +
                    " AND COMPANY_ID = " + favoriteCompany.getCompanyID() +
                    " AND IS_ACTIVE IS TRUE";
            ResultSet rs = stmt.executeQuery(command);
            fillFavoriteCompany(rs, favoriteCompany);
        }catch(SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
                }
            }
        }
    }

    /**
     * Preparing object for insert
     * @param preparedStatement JDBC Object to be prepared
     * @param favoriteCompany to be inserted or updated object
     */
    private static void prepare (PreparedStatement preparedStatement, FavoriteCompany favoriteCompany) {
        try {
            preparedStatement.setInt(1, favoriteCompany.getID());
            preparedStatement.setInt(2, favoriteCompany.getCompanyID());
            preparedStatement.setInt(3, favoriteCompany.getClientID());
            preparedStatement.setBoolean(4, favoriteCompany.isActive());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    /**
     * Fill favorite company object
     * @param resultSet returned from JDBC
     * @param favoriteCompany to be filled by JDBC result
     */
    private static void fillObject (ResultSet resultSet, FavoriteCompany favoriteCompany) {
        try {
            favoriteCompany.setID(resultSet.getInt(1));
            favoriteCompany.setCompanyID(resultSet.getInt(2));
            favoriteCompany.setClientID(resultSet.getInt(3));
            favoriteCompany.setActive(resultSet.getBoolean(4));
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    /**
     * Filling favorite company object
     * @param resultSet returned from JDBC
     * @param favoriteCompany to be filled
     */

    private static void fillFavoriteCompany (ResultSet resultSet, FavoriteCompany favoriteCompany) {
        try {
            while (resultSet.next()) {
                fillObject(resultSet, favoriteCompany);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }
}
