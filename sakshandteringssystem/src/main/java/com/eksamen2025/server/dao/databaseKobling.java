//MONICA AZARIA JOHANSEN 

package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class databaseKobling {

    private static final String URL = "jdbc:mysql://localhost:3306/sakssystem?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    public static Connection hentKobling() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
