package com.eksamen2025.client;

import com.eksamen2025.felles.Sak;

/** @author Jørgen
 * LederController: En klasse som håndterer oppdatering av saker.
 * Den bruker NetworkClient for å kommunisere med serveren.
 * En egen klasse for dette er nok litt overkill, men vi gjorde slik likevel for å følge designmønsteret.
 * Dette kan være nyttig hvis vi senere ønsker å legge til mer kompleks logikk for oppdatering av saker.
 */
public class LederController {

    public static boolean oppdaterSak(Sak sak) {
        return NetworkClient.oppdaterSak(sak);
    }
}
