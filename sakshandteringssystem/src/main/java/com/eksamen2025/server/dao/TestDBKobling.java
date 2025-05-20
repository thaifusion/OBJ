//Dette er en testfil for å teste database kobligen internt. 
//Brukes for å teste kobling til MySQL databasen.
//Monica A. Johansen

package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

public class TestDBKobling {
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null) {
                System.out.println("Databasen er koblet til.");
            }
        } catch (SQLException | IOException e) {
            System.out.println("Feil ved tilkobling til databasen: " + e.getMessage());
        }
    }
}