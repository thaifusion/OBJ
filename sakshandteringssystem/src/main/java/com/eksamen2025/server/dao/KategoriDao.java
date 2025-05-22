package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** 
 * @author Jørgen
 * Klassen oppretter forbindelse med databasen for å hente kategorier, som er en egen tabell.
 * Har en metode for å hente alle kategorier fra databasen.
 */

public class KategoriDao {

    private final Connection kobling;

    public KategoriDao(Connection kobling) {
        this.kobling = kobling;
    }

    /**
     * Henter alle kategorier fra databasen.
     * @return Liste av kategorinavn (String)
     * @throws SQLException hvis noe går galt med databasen
     */
    public List<String> hentAlleKategorier() throws SQLException {
        List<String> kategorier = new ArrayList<>();
        String sql = "SELECT navn FROM kategori";
        try (PreparedStatement stmt = kobling.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                kategorier.add(rs.getString("navn"));
            }
        }
        return kategorier;
    }
}