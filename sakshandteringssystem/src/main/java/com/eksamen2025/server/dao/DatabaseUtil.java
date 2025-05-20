//Monica A. Johansen
package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseUtil {
    private static final String CONFIG_FILE = "db.properties";

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(CONFIG_FILE); 
        props.load(fis);
        
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }

}
