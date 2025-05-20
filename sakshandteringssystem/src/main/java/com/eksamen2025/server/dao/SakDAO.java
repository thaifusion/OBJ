package com.eksamen2025.server.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;

import com.eksamen2025.felles.Prioritet;
import com.eksamen2025.server.dao.DatabaseUtil;
import com.eksamen2025.felles.Sak;
import com.eksamen2025.felles.Sak.Oppsett;

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
            
                // leser alle kolonner fra databasen
            while (rs.next()) {
                
                String id = rs.getString("id");
                String tittel = rs.getString("tittel");
                String beskrivelse = rs.getString("beskrivelse");
                String priString= rs.getString("prioritet");
                String kategori = rs.getString("kategori");
                String status = rs.getString("status");
                String innsender = rs.getString("innsender");
                String mottaker = rs.getString("mottaker");
                Date opprettet = rs.getDate("opprettet").toLocalDate();
                Date oppdatertDate = rs.getDate("oppdatert");
                LocalDate oppdatert = oppdatertDate != null
                        ? oppdatertDate.toLocalDate() : null; // setter oppdatert til null hvis det ikke finnes

                String kommentar = rs.getString("kommentar");
                String tilbakemelding = rs.getString("tilbakemelding");
                
                // Konverterer prioritet fra String til enum
                Prioritet prioritet = priString != null 
                ? Prioritet.valueOf(priString) : null;


                // Oppretter nytt Sak-objekt via oppsettSak() metoden
                Sak sak = new Oppsett()
                    .id(id)
                    .tittel(tittel)
                    .beskrivelse(beskrivelse)
                    .prioritet(prioritet)
                    .kategori(kategori)
                    .status(status)
                    .innsender(innsender)
                    .mottaker(mottaker)
                    .opprettet(opprettet)
                    .oppdatert(oppdatert)
                    .kommentar(kommentar)
                    .tilbakemelding(tilbakemelding)
                    .build(); // Bygger Sak-objektet

                // Legger til saken i listen
                saker.add(sak); 
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
        
        } catch (SQLException | IOException e) {
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

    /**
     * Henter status på en sak basert på sakens ID.
     * 
     * @param sakId ID-en til saken som statusen skal hentes for.
     * @return Statusen til saken som en String, eller null hvis saken ikke finnes.
     */
    public static String hentStatus(int sakId) {
        String spørring = "SELECT status FROM sak WHERE id = ?";
        
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(spørring)) {
            
            pstmt.setInt(1, sakId); // Setter sakId i spørringen
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status"); // Returnerer statusen til saken
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null; // Returnerer null hvis saken ikke finnes
    }
    
    /*
     * @param 
     * Henter prioritet på en sak
     * @return
    */
    public static Prioritet hentPrioritet(int sakId) {
      String spørring = "SELECT prioritet FROM sak WHERE id = ?";
    
      try (Connection conn = DatabaseUtil.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(spørring)) {
            
            pstmt.setInt(1, sakId); // binder parameter til sakId
            
            // Utfører spørringen og henter resultat
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String verdi = rs.getString("prioritet"); 
                    if (verdi != null) {
                        return Prioritet.valueOf(verdi); // Konverterer String til enum
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null; // Returnerer null hvis saken ikke finnes
    }

    /*
     * @param
     * Henter kategori på en sak
     * @return
     * */
    public static String hentKategori(int sakId) {
       String spørring = "SELECT kategori FROM sak WHERE id = ?";

       try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(spørring)) {
            
            pstmt.setInt(1, sakId); // Setter sakId i spørringen
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("kategori"); // Returnerer statusen til saken
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return null; // Returnerer null hvis saken ikke finnes
    }
}

