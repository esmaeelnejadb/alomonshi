package com.alomonshi.utility;
import java.util.Properties;

/**
 * Configuration class for setting some parameter in application
 */

public class AppConfiguration {

    private static Properties configFile;

    private AppConfiguration() {
        configFile = new java.util.Properties();
        try {
            configFile.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        } catch (Exception eta) {
            eta.printStackTrace();
        }
    }

    /**
     * Getting key value property form related file
     * @param key for getting related value form property file
     * @return value of intended key
     */

    public static String getValue(String key) {
        return configFile.getProperty(key);
    }
}