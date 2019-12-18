package com.alomonshi.datalayer.dataaccess;

import com.alomonshi.datalayer.databaseconnection.DBConnection;
import com.alomonshi.object.entity.ServicePicture;
import com.alomonshi.object.entity.UnitPicture;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableServicePicture {

    public static void insertServicePic(ServicePicture servicePicture){
        String command = "insert into servicepictures (serviceID, pictureURL, isActive) values(?, ?, ?)";
        executeInsertUpdate(servicePicture, command);
    }

    public static void updateUnitPic(ServicePicture servicePicture){
        String command = "update servicepictures set serviceID = ?, pictureURL = ?, isActive = ?";
        executeInsertUpdate(servicePicture, command);
    }

    public static void delete(ServicePicture servicePicture){
        servicePicture.setActive(false);
        updateUnitPic(servicePicture);
    }

    private static void executeInsertUpdate(ServicePicture servicePicture, String command)
    {
        Connection conn = DBConnection.getConnection();
        try
        {
            PreparedStatement ps = conn.prepareStatement(command);
            for(String URL : servicePicture.getPicURL()){
                prepare(ps, servicePicture, URL);
                ps.executeUpdate();
            }

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

    public static List<String> getServicePictures(int serviceID){
        String command="select pictureURL from servicepictures where serviceID = " + serviceID + " AND isActive IS TRUE";
        Connection conn = DBConnection.getConnection();
        List<String> servicePictures = new ArrayList<>();
        try
        {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(command);
            while (rs.next()){
                servicePictures.add(rs.getString(1));
            }
            return servicePictures;
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

    private static void prepare(PreparedStatement preparedStatement, ServicePicture servicePicture, String URL){
        try{
            preparedStatement.setInt(2, servicePicture.getServiceID());
            preparedStatement.setString(3, URL);
            preparedStatement.setBoolean(4, servicePicture.isActive());
        }catch (SQLException e){
            Logger.getLogger("Exception").log(Level.SEVERE, "Exception : " + e);
        }
    }
}
