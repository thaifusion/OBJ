package com.eksamen2025.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.eksamen2025.felles.Bruker;
import com.eksamen2025.felles.Rolle;


/** @author Jørgen
 *  Klassen oppretter forbindelse til databasen for brukere. 
 *  Metoder for å hente alle brukere, samt legge til nye.
 * 
 */
public class BrukerDao {
    
    private final Connection conn;
    private List<Bruker> bruker = new ArrayList<>();
    private String brukernavn;
    private String rolleString;

    /**
     * 
     * @param conn Databaseforbindelse
     */
    public BrukerDao(Connection conn) {
        this.conn = conn;
    }


    /**
     * 
     * @return en arraylist med alle brukerne
     * @throws SQLException Hvis en feilmelding fra databasen blir kastet
     */
    public List<Bruker> hentAlleBrukere() throws SQLException {
        String sql = "SELECT * FROM brukere";
        try(Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt. executeQuery(sql);

            while (rs.next()) {
                brukernavn = rs.getString("Brukernavn");
                rolleString = rs.getString("Rolle");
                Rolle rolle = Rolle.valueOf(rolleString.toUpperCase());
                bruker.add(new Bruker(brukernavn, rolle));
            }
        }

        return bruker;
    }

    /**
     * 
     * @param bruker brukernavn
     * @return null
     */
    public Bruker getBrukerFraBrukernavn(String bruker) {
        String sql = "SELECT * FROM brukere WHERE brukernavn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, brukernavn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    brukernavn = rs.getString("Brukernavn");
                    rolleString = rs.getString("Rolle");
                    Rolle rolle = Rolle.valueOf(rolleString.toUpperCase());
                    return new Bruker(brukernavn, rolle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void leggTilBruker(Bruker bruker) throws SQLException {
        String sql = "INSERT INTO brukere (Brukernavn, Rolle) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bruker.getBrukernavn());
            stmt.setString(2, bruker.getRolle().toString()); //.toString for å konvertere Rolle-enumobjekt til String
            stmt.executeUpdate();
        }
    }
}
