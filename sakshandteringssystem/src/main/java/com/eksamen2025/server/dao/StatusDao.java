package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatusDao {
    private final Connection conn;
    
    public StatusDao(Connection conn) {
        this.conn = conn;
    }

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
