package com.eksamen2025.klient.controller;

import com.eksamen2025.server.dao.SakDAO;

/** *@author: Sara
 * 
 * sakController: Controller-klasse for å håndtere sak-relaterte operasjoner i klienten.
 * Bare validering (roller), kall og tilbakemelding til SakDAO. (ingen databaseoperasjoner)
 * 
 */

public class sakController {
    
    /*
     * @param
     * Henter alle saker fra databasen
     * @return
     */
    public void registrerSak() {
        SakDAO sakDAO = new SakDAO();
        // sakDAO.opprettSak(sak);
    }

    /*
     * @param
     * Oppdaterer status på en sak
     * @return
     */
    public void oppdaterStatus() {
        SakDAO sakDAO = new SakDAO();
        // sakDAO.oppdaterStatus(sakId, nyStatus);
    }

    /*
     * @param
     * Henter sak for en bruker
     * @return
     */
    public void henteSakBruker() {
        SakDAO sakDAO = new SakDAO();
        // List<Sak> saker = sakDAO.hentSakBruker(brukerId);
    }
}
