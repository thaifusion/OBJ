package com.eksamen2025.server.dao;

import com.eksamen2025.felles.Sak;
import com.eksamen2025.felles.Prioritet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for håndtering av Sak-objekter mot databasen.
 */
public class SakDao {

    private final Connection conn;

    public SakDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Legger til en sak i databasen.
     * 
     */
    public void leggTilSak(Sak sak) throws SQLException {
        String sql = "INSERT INTO sak (tittel, beskrivelse, prioritet_id, kategori_id, status_id, reporter_id, mottaker_id, opprettetTid, oppdatertTid, utviklerkommentar, testerTilbakemelding) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            stmt.setDate(8, sak.getOpprettet() != null ? Date.valueOf(sak.getOpprettet()) : null);
            stmt.setDate(9, sak.getOppdatert() != null ? Date.valueOf(sak.getOppdatert()) : null);
            stmt.setString(10, sak.getKommentar());
            stmt.setString(11, sak.getTilbakemelding());
            stmt.executeUpdate();
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
        try (Statement stmt = conn.createStatement();
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
                        .opprettet(rs.getDate("opprettetTid") != null ? rs.getDate("opprettetTid").toLocalDate() : null)
                        .oppdatert(rs.getDate("oppdatertTid") != null ? rs.getDate("oppdatertTid").toLocalDate() : null)
                        .kommentar(rs.getString("utviklerkommentar"))
                        .tilbakemelding(rs.getString("testerTilbakemelding"))
                        .bygg();
                saker.add(sak);
            }
        }
        return saker;
    }

    // Hjelpemetoder for å finne id-er basert på navn/enum

    private int hentPrioritetId(Prioritet prioritet) throws SQLException {
        String sql = "SELECT id FROM prioritet WHERE LOWER(navn) = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, prioritet.name().toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke prioritet: " + prioritet);
    }

    private int hentKategoriId(String kategori) throws SQLException {
        String sql = "SELECT id FROM kategori WHERE navn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kategori);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke kategori: " + kategori);
    }

    private int hentStatusId(String statusKode) throws SQLException {
        String sql = "SELECT id FROM status WHERE kode = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, statusKode);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke status: " + statusKode);
    }

    private int hentBrukerId(String brukernavn) throws SQLException {
        String sql = "SELECT id FROM bruker WHERE brukernavn = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, brukernavn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new SQLException("Fant ikke bruker: " + brukernavn);
    }
}
