package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.enums.MediaType;
import com.alomonshi.object.tableobjects.ServicePicture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableServicePicture {

    public static void insertServicePic(ServicePicture servicePicture){
        String command = "INSERT INTO servicepictures (serviceID," +
                " pictureURL," +
                " isActive, " +
                " mediaType" +
                ")" +
                " values(?, ?, ?, ?)";
        executeInsertUpdate(servicePicture, command);
    }

    public static void updateServicePic(ServicePicture servicePicture){
        String command = "UPDATE servicepictures SET " +
                "serviceID = ?," +
                " pictureURL = ?," +
                " isActive = ?, " +
                " mediaType = ?" +
                " WHERE ID = " + servicePicture.getID();
        executeInsertUpdate(servicePicture, command);
    }

    public static void delete(ServicePicture servicePicture){
        servicePicture.setActive(false);
        updateServicePic(servicePicture);
    }

    private static void executeInsertUpdate(ServicePicture servicePicture, String command)
    {
        Connection conn = DBConnection.getConnection();
        try
        {
            PreparedStatement ps = conn.prepareStatement(command);
            prepare(ps, servicePicture);
            ps.executeUpdate();

        }catch(SQLException e)
        {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }finally
        {
            if(conn != null)
            {
                try
                {
                    conn.close();
                } catch (SQLException e)
                {
                    Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
                }
            }
        }
    }

    static List<ServicePicture> getServicePictures(int serviceID){
        String command = "SELECT * FROM servicepictures where serviceID = " + serviceID + " AND isActive IS TRUE";
        Connection conn = DBConnection.getConnection();
        List<ServicePicture> servicePictures = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            fillServicePictures(rs, servicePictures);
            if (!servicePictures.isEmpty())
                return servicePictures;
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

    private static void prepare(PreparedStatement preparedStatement, ServicePicture servicePicture){
        try{
            preparedStatement.setInt(1, servicePicture.getServiceID());
            preparedStatement.setString(2, servicePicture.getPicURL());
            preparedStatement.setBoolean(3, servicePicture.isActive());
            preparedStatement.setInt(4, servicePicture.getMediaType().getValue());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }
    }

    private static void fillServicePicture(ResultSet resultSet, ServicePicture servicePicture){
        try {
            servicePicture.setID(resultSet.getInt(1));
            servicePicture.setServiceID(resultSet.getInt(2));
            servicePicture.setPicURL(resultSet.getString(3));
            servicePicture.setActive(resultSet.getBoolean(4));
            servicePicture.setMediaType(MediaType.getByValue(resultSet.getInt(5)));
        } catch (SQLException e) {
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
        }
    }

    private static void fillSingleServicePicture(ResultSet resultSet, ServicePicture servicePicture) {
        try {
            while (resultSet.next()){
                fillServicePicture(resultSet, servicePicture);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception :" + e);
        }
    }

    private static void fillServicePictures(ResultSet resultSet, List<ServicePicture> servicePictures){
        try {
            while (resultSet.next()){
                ServicePicture servicePicture = new ServicePicture();
                fillServicePicture(resultSet, servicePicture);
                servicePictures.add(servicePicture);
            }
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception :" + e);
        }
    }
}
