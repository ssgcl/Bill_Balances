package com.ssgc.springbootjwt.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;


@WebListener
public class CleanupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // No-op
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    	 // Get the drivers enumeration
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        
        // Iterate using a while loop (works like a for loop for Enumeration)
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("Deregistered driver: " + driver);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
