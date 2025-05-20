package com.eksamen2025.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

/** @author Monica
 * Klassen fungerer som en test for å sjekke om vi klarer å koble til databasen.
 * Den oppretter en forbindelse til databasen og skriver ut en melding om tilkoblingen var vellykket.
 */
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