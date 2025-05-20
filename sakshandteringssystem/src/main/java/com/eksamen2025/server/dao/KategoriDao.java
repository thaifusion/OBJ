package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for håndtering av Kategori-objekter mot databasen.
 */
public class KategoriDao {

    private final Connection conn;

    public KategoriDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Henter alle kategorier fra databasen.
     * @return Liste av kategorinavn (String)
     * @throws SQLException hvis noe går galt med databasen
     */
    public List<String> hentAlleKategorier() throws SQLException {
        List<String> kategorier = new ArrayList<>();
        String sql = "SELECT navn FROM kategori";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                kategorier.add(rs.getString("navn"));
            }
        }
        return kategorier;
    }
}