package com.eksamen2025.server.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;


// Trenger en databaseManager for å håndtere databaseoperasjoner, 

// Update the import below to match the actual location of Sak.java, for example:

import com.eksamen2025.felles.Sak;

/**
 * @author Sara
 * 
 * SakDAO: Klasse for å håndtere databaseoperasjoner relatert til Sak.
 * Inkluderer metoder for å opprette, lese, oppdatere og slette sak.
 * 
 */

public class SakDAO {

    /*
    * @param 
    * Henter alle saker fra databasen
    * @return saker
    */
   
    public List<Sak> hentAlleSaker() {
        List<Sak> saker = new ArrayList<>(); // Oppretter en liste for å lagre sakene
        String spørring = "SELECT * FROM sak"; // SQL-spørring for å hente alle saker fra databasen

        // Henter en kobling til databasen, bruker try-with-resources for å lukke ressursene automatisk
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(spørring);
             ResultSet rs = pstmt.executeQuery(spørring)) {
            
            while (rs.next()) {
                Sak sak = new Sak(); // Oppretter et nytt saks-objekt
                sak.setId(rs.getInt("id")); // Setter ID-en til saken
                sak.setTittel(rs.getString("tittel")); // Setter tittel
                sak.setBeskrivelse(rs.getString("beskrivelse")); // Setter beskrivelse

                // Konverterer prioritet, enum til String
                String pri = rs.getString("prioritet"); // Henter prioritet som String
                sak.setPrioritet(pri != null ? Prioritet.valueOf(pri) : null); // Setter prioritet
           
                sak.setKategori(rs.getString("kategori")); // Setter kategori
                sak.setStatus(rs.getString("status")); // Setter status
                sak.setInnsender(rs.getString("innsender")); // Setter innsender
                sak.setMottaker(rs.getString("mottaker")); // Setter mottaker
                sak.setOpprettet(rs.getDate("opprettet").toLocalDate()); // Setter opprettet dato

                Date oppdatertDate = rs.getDate("oppdatert"); // Henter oppdatert dato
                if (oppdatertDate != null) {
                    sak.setOppdatert(oppdatertDate.toLocalDate()); // Setter oppdatert dato
                }

                sak.setKommentar(rs.getString("kommentar")); // Setter kommentar
                sak.setTilbakemelding(rs.getString("tilbakemelding")); // Setter tilbakemelding
                saker.add(sak); // Legger til saken i listen
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return saker; // Returnerer listen med saker
    }

    /**
     * Opprettet og lagrer en ny sak i databasen.
     * Denne metoden tar inn nødvendige data for en sak og lagrer den i databasen.
     * 
     * @param sakData Dataobjekt som inneholder informasjon om saken som skal lagres.
     * @return id til den opprettede saken, eller -1 hvis det oppstod en feil.
     */

    public static int opprettSak(Sak sakData) {
        // Oppretter en ny sak med SQL som lagres i tabellen sak
      String spørring = "INSERT INTO sak (tittel, beskrivelse, prioritet, kategori, status, innsender, mottaker, opprettet, oppdatert, kommentar, tilbakemelding) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      // Henter en kobling til databasen, Bruker preparedStatement for å unngå SQL-injeksjon
      try (Connection conn = DatabaseUtil.getConnection(); // må være riktig filnavn for kobling til databasen
           PreparedStatement pstmt = conn.prepareStatement(spørring, Statement.RETURN_GENERATED_KEYS)) { // ber om å returnere genererte nøkler
        
            // Setter inn verdier i spørringen, binder parameterne til verdiene i sakData
        pstmt.setString(1, sakData.getTittel());
        pstmt.setString(2, sakData.getBeskrivelse());
        // Prioritet er enum, så må konverteres til String
        pstmt.setString(3, sakData.getPrioritet().name());
        pstmt.setString(4, sakData.getKategori());
        pstmt.setString(5, sakData.getStatus());
        pstmt.setString(6, sakData.getInnsender());
        pstmt.setString(7, sakData.getMottaker());
        pstmt.setDate(8, java.sql.Date.valueOf(sakData.getOpprettet()));
        pstmt.setDate(9, java.sql.Date.valueOf(sakData.getOppdatert()));
        pstmt.setString(10, sakData.getKommentar());
        pstmt.setString(11, sakData.getTilbakemelding());
        
        // Utfører spørringen og får antall rader som ble påvirket
         // Hvis rader > 0, betyr det at saken ble opprettet
         // Henter den genererte nøkkelen (ID-en) for den nye saken
         // Returnerer ID-en til den opprettede saken
        int rader = pstmt.executeUpdate();
        if (rader == 0) {
            return -1; // Ingen rader ble opprettet
        }

        // Henter generert nøkkel for den nye saken
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1); // Returnerer ID-en til den opprettede saken
            } 
        }
        } catch (SQLException e) {
            e.printStackTrace();

        } 
        return -1; // Returnerer -1 hvis det oppstod en feil
    }            
            
   
    /*
     * @param 
     * Oppdaterer en eksisterende sak i databasen, men bare de nødvendige feltene.
     * sakId, nyStatus, kommentar og tilbakemelding. (Dato oppdaters av MySQL)
     * @return true eller false avhengig av om oppdateringen var vellykket.
    */
    public static boolean oppdaterSak(int sakId, String nyStatus, String kommentar, String tilbakemelding) {
       // Oppdaterer en eksisterende sak i databasen
       String spørring = "UPDATE sak SET status = ?, oppdatert = NOW(), kommentar = ?, tilbakemelding = ? WHERE id = ?";
       
       try (Connection conn = DatabaseUtil.getConnection(); // må være riktig filnavn for kobling til databasen
            PreparedStatement pstmt = conn.prepareStatement(spørring)) {
            
            pstmt.setString(1, nyStatus);
            pstmt.setString(2, kommentar);
            pstmt.setString(3, tilbakemelding);
            pstmt.setInt(4, sakId);

            int rader = pstmt.executeUpdate(); 
            return rader > 0; // Returnerer true hvis oppdateringen var vellykket
        
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Returnerer false hvis det oppstod en feil
        }
    }

    /*
     * @param 
     * Søker etter en sak i databasen basert på spesifikke kriterier:
     * Tittel, prioritet, status, opprettet år og opprettet dato.
     * @return
    */
    public void søkEtterSak() {
       // må denne metoden tillate søk på flere søkekriterier? 
    }

    /*
     * @param 
     * Henter status på en sak
     * @return
    */
    public void hentStatus() {
        String spørring = "SELECT status FROM sak WHERE id = ?";
    }
    
    /*
     * @param 
     * Henter prioritet på en sak
     * @return
    */
    public void hentPrioritet() {
      String spørring = "SELECT prioritet FROM sak WHERE id = ?";
    }

    /*
     * @param
     * Henter kategori på en sak
     * @return
     */
    public void hentKategori() {
       String spørring = "SELECT kategori FROM sak WHERE id = ?";
    }
}
