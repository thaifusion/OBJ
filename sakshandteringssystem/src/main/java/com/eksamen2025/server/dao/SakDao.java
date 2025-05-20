package com.eksamen2025.server.dao;

import java.sql.Connection;

public class SakDao {

    private final Connection conn;

    public SakDao(Connection conn) {
        this.conn = conn;
    }

    public void leggTilSak(Sak sak) {
        String sql = "INSERT INTO sak (id, tittel, beskrivelse, prioritet, kategori, status, innsender, mottaker, opprettet, oppdatert, kommentar, tilbakemelding) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    }

}
