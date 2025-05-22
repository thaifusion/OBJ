package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Jørgen
 * Klassen oppretter forbindelse med databasen for å hente status, som er en egen tabell.
 */
public class StatusDao {
    private final Connection conn;
    
    public StatusDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Henter alle status fra databasen.
     * @return
     * @throws SQLException
     */
    public List<String> hentAlleStatus() throws SQLException {
        String sql = "SELECT etikett FROM status";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            List<String> statusListe = new ArrayList<>();
            while (rs.next()) {
                statusListe.add(rs.getString("etikett"));
            }
            return statusListe;
        }
    }

}
