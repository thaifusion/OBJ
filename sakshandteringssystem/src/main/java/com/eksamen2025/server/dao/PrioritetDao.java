package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.eksamen2025.felles.Prioritet;

/**
 * DAO for håndtering av Prioritet-objekter mot databasen.
 */
public class PrioritetDao {

    private final Connection conn;

    public PrioritetDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Henter alle prioriteringer fra databasen.
     * @return Liste av Prioritet-enumverdier
     * @throws SQLException hvis noe går galt med databasen
     */
    public List<Prioritet> hentAllePrioriteter() throws SQLException {
        List<Prioritet> prioriteter = new ArrayList<>();
        String sql = "SELECT navn FROM prioritet";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String navn = rs.getString("navn");
                try {
                    prioriteter.add(Prioritet.valueOf(navn.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // Ignorer ukjente verdier som ikke matcher enum
                }
            }
        }
        return prioriteter;
    }
}