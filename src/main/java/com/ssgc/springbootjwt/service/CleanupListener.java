package com.ssgc.springbootjwt.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * This listener is used to clean up JDBC drivers when the web application is shut down.
 * It implements the ServletContextListener interface to perform actions when the web
 * application's context is initialized and destroyed.
 */
@WebListener // Marks this class as a servlet listener
public class CleanupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // No operation needed during context initialization.
        // This method can be used for initialization tasks if required.
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // This method is called when the web application context is destroyed.

        // Get the enumeration of registered JDBC drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        
        // Iterate through the drivers and deregister each one
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                // Deregister the JDBC driver
                DriverManager.deregisterDriver(driver);
                System.out.println("Deregistered driver: " + driver);
            } catch (SQLException e) {
                // Print the stack trace if there is an error deregistering the driver
                e.printStackTrace();
            }
        }
    }
}
