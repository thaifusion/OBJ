package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDBKobling {
    public static void main(String[] args) {
        try (Connection conn = databaseKobling.hentKobling()) {
            if (conn != null) {
                System.out.println("Databasen er koblet til.");
            }

        } catch (SQLException e) {
            System.out.println("Feil ved tilkobling til databasen: " + e.getMessage());
        }
        
            
    }
    
}
