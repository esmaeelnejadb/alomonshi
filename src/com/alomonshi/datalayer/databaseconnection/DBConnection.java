package com.alomonshi.datalayer.databaseconnection;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

/**
 * Connect to Database
 */

public class DBConnection {
	private static String databaseName = "ALOMONSHI";
/*	private static String dbUsername = "root";
	private static String dbPass = "aloroot009";*/
    private static String dbUsername="root";
    private static String dbPass="bB1!3259622";
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
