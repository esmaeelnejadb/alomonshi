package com.alomonshi.datalayer.databaseconnection;

import com.alomonshi.utility.AppConfiguration;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connect to Database
 */

public class DBConnection {
	private static String databaseName = AppConfiguration.getValue("database.properties.dataBaseName");
	private static String dbUsername =  AppConfiguration.getValue("database.properties.username");
	private static String dbPass = AppConfiguration.getValue("programming.status.development").equals("on")
            ? AppConfiguration.getValue("database.properties.developmentPassword")
            : AppConfiguration.getValue("database.properties.productPassword")  ;
	private static String url = "jdbc:mysql://localhost:3306/" + databaseName + "?useSSL=false&&useUnicode=true&amp;characterEncoding=utf8mb4&amp";

    /**
     * Get a connection to database
     * @return Connection object
     */
		
    public static Connection getConnection()
    {
      try {
          Class.forName("com.mysql.jdbc.Driver");
          return DriverManager.getConnection(url, dbUsername, dbPass);
      } catch (SQLException | ClassNotFoundException ex) {
          throw new RuntimeException("Error connecting to the database", ex);
      }
    }	
}
