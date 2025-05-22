package com.eksamen2025.server.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.eksamen2025.felles.Prioritet;
import com.eksamen2025.felles.Sak;
import static java.sql.Types.INTEGER;

/** @author Jørgen
 * Klassen oppretter forbindelse med databasen for å hente saker.
 * Har metoder for å legge til saker og hente alle saker fra databasen.
 */
public class SakDao {

    private final Connection kobling;

    public SakDao(Connection kobling) {
        this.kobling = kobling;
    }

    /**
     * Legger til en sak i databasen.
     * id for saker er ikke med her fordi den blir automatisk inkrementert i databasen.
     * @param sak Sak-objektet som skal legges til
     */
    public void leggTilSak(Sak sak) throws SQLException {
        String sql = "INSERT INTO sak (tittel, beskrivelse, prioritet_id, kategori_id, status_id, reporter_id, mottaker_id, opprettetTid, oppdatertTid, utviklerkommentar, testerTilbakemelding) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = kobling.prepareStatement(sql)) {
            stmt.setString(1, sak.getTittel());
            stmt.setString(2, sak.getBeskrivelse());
            stmt.setInt(3, hentPrioritetId(sak.getPrioritet()));
            stmt.setInt(4, hentKategoriId(sak.getKategori()));
            stmt.setInt(5, hentStatusId(sak.getStatus()));
            stmt.setInt(6, hentBrukerId(sak.getInnsender()));
            if (sak.getMottaker() != null && !sak.getMottaker().isEmpty()) {
                stmt.setInt(7, hentBrukerId(sak.getMottaker()));
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setTimestamp(8, sak.getOpprettet());
            stmt.setTimestamp(9, sak.getOppdatert());
            stmt.setString(10, sak.getKommentar());
            stmt.setString(11, sak.getTilbakemelding());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Feil ved innsetting av sak: " + e.getMessage(), e);
        }
    }

    public boolean oppdaterSak(Sak sak) {
        String sql = "UPDATE sak SET mottaker_id=?, status_id=? WHERE id=?";
        try (PreparedStatement stmt = kobling.prepareStatement(sql)) {
            if (sak.getMottaker() != null && !sak.getMottaker().isEmpty()) {
                stmt.setInt(1, hentBrukerId(sak.getMottaker()));
            } else {
                stmt.setNull(1, INTEGER);
            }
            stmt.setInt(2, hentStatusId(sak.getStatus()));
            stmt.setInt(3, Integer.parseInt(sak.getId()));
            int rows = stmt.executeUpdate();
            System.out.println("Antall rader oppdatert: " + rows + " for sak id: " + sak.getId());
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Feil ved oppdatering av sak med id: " + sak.getId());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Henter alle saker fra databasen.
     */
    public List<Sak> hentAlleSaker() throws SQLException {
        List<Sak> saker = new ArrayList<>();
        String sql = "SELECT s.*, p.navn AS prioritetNavn, k.navn AS kategoriNavn, st.kode AS statusKode, br1.brukernavn AS reporterNavn, br2.brukernavn AS mottakerNavn " +
                     "FROM sak s " +
                     "LEFT JOIN prioritet p ON s.prioritet_id = p.id " +
                     "LEFT JOIN kategori k ON s.kategori_id = k.id " +
                     "LEFT JOIN status st ON s.status_id = st.id " +
                     "LEFT JOIN bruker br1 ON s.reporter_id = br1.id " +
                     "LEFT JOIN bruker br2 ON s.mottaker_id = br2.id";
        try (Statement stmt = kobling.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Sak sak = new Sak.Oppsett()
                    .id(String.valueOf(rs.getInt("id")))
                    .tittel(rs.getString("tittel"))
                    .beskrivelse(rs.getString("beskrivelse"))
                    .prioritet(Prioritet.valueOf(rs.getString("prioritetNavn").toUpperCase()))
                    .kategori(rs.getString("kategoriNavn"))
                    .status(rs.getString("statusKode"))
                    .innsender(rs.getString("reporterNavn"))
                    .mottaker(rs.getString("mottakerNavn"))
                    .opprettet(rs.getTimestamp("opprettetTid"))
                    .oppdatert(rs.getTimestamp("oppdatertTid"))
                    .kommentar(rs.getString("utviklerkommentar"))
                    .tilbakemelding(rs.getString("testerTilbakemelding"))
                    .bygg();
                saker.add(sak);
            }
        } catch (SQLException e) {
            throw new SQLException("Feil ved henting av saker: " + e.getMessage(), e);
        }
        return saker;
    }

    /**
     * Henter ID til en prioritet basert på navnet.
     * @param prioritet Prioritet-enum
     * @return ID til prioriteten
     * @throws SQLException hvis noe går galt med databasen
     */
    private int hentPrioritetId(Prioritet prioritet) throws SQLException {
        String sql = "SELECT id FROM prioritet WHERE LOWER(navn) = ?";
        try (PreparedStatement stmt = kobling.prepareStatement(sql)) {
            stmt.setString(1, prioritet.name().toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke prioritet: " + prioritet);
    }

    /**
     * Henter ID til en kategori basert på navnet.
     * @param kategori Kategorinavn
     * @return ID til kategorien
     * @throws SQLException hvis noe går galt med databasen
     */
    private int hentKategoriId(String kategori) throws SQLException {
        String sql = "SELECT id FROM kategori WHERE navn = ?";
        try (PreparedStatement stmt = kobling.prepareStatement(sql)) {
            stmt.setString(1, kategori);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke kategori: " + kategori);
    }

    /**
     * Henter ID til en status basert på statuskoden.
     * @param statusKode Statuskode
     * @return ID til statusen
     * @throws SQLException hvis noe går galt med databasen
     */
    private int hentStatusId(String statusKode) throws SQLException {
        String sql = "SELECT id FROM status WHERE kode = ?";
        try (PreparedStatement stmt = kobling.prepareStatement(sql)) {
            stmt.setString(1, statusKode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke status: " + statusKode);
    }

    /**
     * Henter ID til en bruker basert på brukernavnet.
     * @param brukernavn Brukernavn
     * @return ID til brukeren
     * @throws SQLException hvis noe går galt med databasen
     */
    private int hentBrukerId(String brukernavn) throws SQLException {
        String sql = "SELECT id FROM bruker WHERE brukernavn = ?";
        try (PreparedStatement stmt = kobling.prepareStatement(sql)) {
            stmt.setString(1, brukernavn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke bruker: " + brukernavn);
    }

    /** @author Sara
     * Oppdaterer status og utviklerkommentar for en sak.
     * @param sakId Sak-ID
     * @param status Ny status
     * @param utviklerkommentar Utviklerkommentar
     * @return Antall rader oppdatert
     */
    public int oppdaterStatusOgKommentar(int sakId, String status, String kommentar) throws SQLException {
        System.out.println("DAO oppdaterer sak # " + sakId + ", status: " + status + ", kommentar: " + kommentar);

        String sql = "UPDATE sak SET status_id = ?, utviklerkommentar = ?, oppdatertTid = CURRENT_TIMESTAMP WHERE id = ?";

        try (PreparedStatement stmt = kobling.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(status));
            stmt.setString(2, kommentar);
            stmt.setInt(3, sakId);
            int count = stmt.executeUpdate();
            System.out.println("DAO executeUpdate returnerte:" + count);
            return count;

        } catch (SQLException e) {
            throw new SQLException("Feil ved oppdatering av sak: " + e.getMessage(), e);
        }
    }
}