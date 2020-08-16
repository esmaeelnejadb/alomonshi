package com.alomonshi.configuration;

/**
 * Configuration parameter to be set at program startup
 */
public class ConfigurationParameter {
    //*********Database Configuration***********/
    public static String databaseName = "";
    public static String dataBaseUsername = "";
    public static String databasePassword = "";

    //*********SMS Panel Configuration***********/
    public static String smsPanelUserName = "";
    public static String smsPanelPassword = "";
    public static String smsPanelFromNumber = "";

    //*********Reserve Time Configuration***********/
    public static int couldBeCanceledPeriod = 0; //in hour

    //*********Companies limitation number***********/
    public static int homePageCompaniesLimitationNumber = 10; //
}