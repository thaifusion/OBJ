//Monica A. Johansen
package com.eksamen2025.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Monica
 * Klassen håndterer tilkoblingen til databasen.
 * Den laster inn konfigurasjonen fra en db.properties-fil og oppretter en forbindelse til databasen.
 */
public class DatabaseUtil {
    private static final String CONFIG_FILE = "sakshandteringssystem/src/main/java/com/eksamen2025/ressurser/db.properties";

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(CONFIG_FILE); 
        props.load(fis);
        
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, user, password);
    }

}
