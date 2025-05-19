package com.eksamen2025.felles;

/** @author Jørgen
 * Klassen representerer en bruker
 * 
 * 
 */

public class Bruker {
    
    private String brukernavn;
    private Rolle rolle;
    
    public Bruker(String brukernavn, Rolle rolle) {
        this.brukernavn = brukernavn;
        this.rolle = rolle;
    }

    /**
     * 
     * @return brukernavn
     */
    public String getBrukernavn() {
        return brukernavn;
    }

    /**
     *
     * @param brukernavn
     */
    public void setBrukernavn(String brukernavn) {
        this.brukernavn = brukernavn;
    }


    /**
     *
     * @return Rolle
     */
    public Rolle getRolle() {
        return rolle;
    }


    /**
     *
     * @param rolle 
     */
    public void setRolle(Rolle rolle) {
        this.rolle = rolle;
    }


}
