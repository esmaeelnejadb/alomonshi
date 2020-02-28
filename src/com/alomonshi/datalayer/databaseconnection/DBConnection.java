package com.alomonshi.datalayer.databaseconnection;

import com.alomonshi.configuration.ConfigurationParameter;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connect to Database
 */

public class DBConnection {


    /**
     * Get a connection to database
     * @return Connection object
     */
		
    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/"
                + ConfigurationParameter.databaseName
                + "?useSSL=false&&useUnicode=true&amp;characterEncoding=utf8mb4&amp";
      try {
          Class.forName("com.mysql.jdbc.Driver");
          return DriverManager.getConnection(url
                  , ConfigurationParameter.dataBaseUsername
                  , ConfigurationParameter.databasePassword);
      } catch (SQLException | ClassNotFoundException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
    }

    /**
     * Closing a connection
     * @param connection to be closed
     */

    public static void closeConnection(Connection connection){
        if(connection != null)
        {
            try
            {
                connection.close();
            } catch (SQLException e)
            {
                Logger.getLogger("Exception").log(Level.SEVERE, "Exception " + e);
            }
        }
    }
}