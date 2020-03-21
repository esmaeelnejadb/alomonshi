package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.tableobjects.CompanyPicture;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableCompanyPicture {

    public static void insertCompanyPicture(CompanyPicture companyPicture){
        String command = "INSERT INTO companypics (" +
                " COMPANY_ID," +
                " PICTURE_URL, " +
                " IS_ACTIVE," +
                " CREATE_DATE," +
                " UPDATE_DATE," +
                " REMOVE_DATE" +
                ") values(?, ?, ?, ?, ?, ?)";
        executeInsertUpdate(companyPicture, command);
    }

    public static void updateCompanyPicture(CompanyPicture companyPicture){
        String command = "UPDATE servicepictures SET" +
                " COMPANY_ID = ?," +
                " PICTURE_URL = ?," +
                " IS_ACTIVE = ?" +
                " CREATE_DATE = ?" +
                " UPDATE_DATE = ?" +
                " REMOVE_DATE = ?" +
                " WHERE ID = " + companyPicture.getID();
        executeInsertUpdate(companyPicture, command);
    }

    public static void delete(CompanyPicture companyPicture){
        companyPicture.setActive(false);
        updateCompanyPicture(companyPicture);
    }

    private static void executeInsertUpdate(CompanyPicture companyPicture, String command) {
        Connection conn = DBConnection.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(command);
            prepare(ps, companyPicture);
            ps.executeUpdate();

        }catch(SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
                }
            }
        }
    }

    /**
     * Getting list of company pictures
     * @param companyID intended company
     * @return list company pictures
     */

    static List<CompanyPicture> getCompanyPictures(int companyID){
        Connection conn = DBConnection.getConnection();
        List<CompanyPicture> companyPictures = new ArrayList<>();
        try {
            String command = "SELECT" +
                    " *" +
                    " FROM" +
                    " companypics" +
                    " WHERE" +
                    " COMPANY_ID = " + companyID + " AND IS_ACTIVE IS TRUE";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillCompanyPictures(rs, companyPictures);
            if (!companyPictures.isEmpty())
                return companyPictures;
            else return null;
        }catch(SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            return null;
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


    private static void prepare(PreparedStatement preparedStatement, CompanyPicture companyPicture){
        try{
            preparedStatement.setInt(1, companyPicture.getCompanyID());
            preparedStatement.setString(2, companyPicture.getPictureURL());
            preparedStatement.setBoolean(3, companyPicture.isActive());
            preparedStatement.setObject(4, companyPicture.getCreateDate());
            preparedStatement.setObject(5, companyPicture.getUpdateDate());
            preparedStatement.setObject(6, companyPicture.getRemoveDate());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }
    }

    private static void fillServicePicture(ResultSet resultSet, CompanyPicture companyPicture){
        try {
            companyPicture.setID(resultSet.getInt(1));
            companyPicture.setCompanyID(resultSet.getInt(2));
            companyPicture.setPictureURL(resultSet.getString(3));
            companyPicture.setActive(resultSet.getBoolean(4));
            companyPicture.setCreateDate(resultSet.getObject(5, LocalDateTime.class));
            companyPicture.setCreateDate(resultSet.getObject(6, LocalDateTime.class));
            companyPicture.setRemoveDate(resultSet.getObject(7, LocalDateTime.class));
        } catch (SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    private static void fillSingleCompanyPicture(ResultSet resultSet, CompanyPicture companyPicture) {
        try {
            while (resultSet.next()){
                fillServicePicture(resultSet, companyPicture);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception :" + e);
        }
    }

    private static void fillCompanyPictures(ResultSet resultSet, List<CompanyPicture> companyPictures){
        try {
            while (resultSet.next()){
                CompanyPicture companyPicture = new CompanyPicture();
                fillServicePicture(resultSet, companyPicture);
                companyPictures.add(companyPicture);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception :" + e);
        }
    }
}