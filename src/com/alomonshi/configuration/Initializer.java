package com.alomonshi.configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Initializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String applicationPath = servletContextEvent.getServletContext().getRealPath("");
        ReadPropertiesFile config = new ReadPropertiesFile(applicationPath);
        ConfigurationParameter.databaseName = config.getKey("database.properties.dataBaseName");
        ConfigurationParameter.dataBaseUsername = config.getKey("programming.status.development").equals("on")
                ? config.getKey("database.properties.localUsername")
                : config.getKey("database.properties.serverUsername");
        ConfigurationParameter.databasePassword = config.getKey("programming.status.development").equals("on")
                ? config.getKey("database.properties.localPassword")
                : config.getKey("database.properties.serverPassword");


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    private static class ReadPropertiesFile {

        private static Properties property = new Properties();

        private ReadPropertiesFile(String applicationPath) {
            loadProperties(applicationPath);
        }

        private void loadProperties(String applicationPath) {
            File configFile = new File(applicationPath, "config.properties");
            try {
                InputStream stream = new FileInputStream(configFile);
                property.load(stream);
            } catch (IOException e) {
                Logger.getLogger("Exception").log(Level.SEVERE, "Can not read property file " + e);
            }
        }

        public String getKey(String key) {
            return property.getProperty(key);
        }
    }
}
