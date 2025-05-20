package com.eksamen2025.klient.controller;

import com.eksamen2025.server.dao.SakDAO;

/*
 * @author: Sara
 * sakController: Controller-klasse for å håndtere sak-relaterte operasjoner i klienten.
 */

public class sakController {
    
    public void registrerSak() {
        // Logikk for å registrere en ny sak
        SakDAO sakDAO = new SakDAO();
        // sakDAO.opprettSak(sak);
    }

    public void oppdaterStatus() {
        // Logikk for å oppdatere status på en sak
        SakDAO sakDAO = new SakDAO();
        // sakDAO.oppdaterStatus(sakId, nyStatus);
    }

    public void henteSakBruker() {
        // Logikk for å hente sak for en spesifikk bruker
        SakDAO sakDAO = new SakDAO();
        // List<Sak> saker = sakDAO.hentSakBruker(brukerId);
    }
}
