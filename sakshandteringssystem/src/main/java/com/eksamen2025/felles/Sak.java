package com.eksamen2025.felles;

import java.time.LocalDate;

/** @author Jørgen
 * Klassen representerer en sak.
 * 
 * 
 */
public class Sak {
    private String id;
    private String tittel;
    private String beskrivelse;
    private Prioritet prioritet;
    private String kategori;
    private String status;
    private String innsender;
    private String mottaker;
    private LocalDate opprettet;
    private LocalDate oppdatert;
    private String kommentar;
    private String tilbakemelding;

    /**
     * 
     * @param oppsett
     */
    // Konstruktør
    public Sak(Oppsett oppsett) {
        this.id = oppsett.id;
        this.tittel = oppsett.tittel;
        this.beskrivelse = oppsett.beskrivelse;
        this.prioritet = oppsett.prioritet;
        this.kategori = oppsett.kategori;
        this.status = oppsett.status;
        this.innsender = oppsett.innsender;
        this.mottaker = oppsett.mottaker;
        this.opprettet = oppsett.opprettet;
        this.oppdatert = oppsett.oppdatert;
        this.kommentar = oppsett.kommentar;
        this.tilbakemelding = oppsett.tilbakemelding;
    }

    /**
     * 
     * @param tittel
     */
    public void setTittel(String tittel) {
        this.tittel = tittel;
    }

    /**
     * 
     * @return tittel
     */
    public String getTittel() {
        return tittel;
    }

    /**
     * 
     * @param beskrivelse
     */
    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    /**
     * 
     * @return beskrivelse
     */
    public String getBeskrivelse() {
        return beskrivelse;
    }

    /**
     * 
     * @param prioritet
     */
    public void setPrioritet(Prioritet prioritet) {
        this.prioritet = prioritet;
    }

    /**
     * 
     * @return prioritet (LAV, MIDDELS, HØY)
     */
    public Prioritet getPrioritet() {
        return prioritet;
    }

    /**
     * 
     * @return String-representasjon av en sak
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" + 
               "Tittel: " + tittel + "\n" +
               "Beskrivelse: " + beskrivelse + "\n" +
               "Prioritet: " + prioritet + "\n" +
               "Kategori: " + kategori + "\n" +
               "Status: " + status + "\n" +
               "Innsender: " + innsender + "\n" +
               "Mottaker: " + mottaker + "\n" +
               "Opprettet: " + opprettet + "\n" +
               "Oppdatert: " + oppdatert + "\n" +
               "Kommentar: " + kommentar + "\n" +
               "tilbakemelding: " + tilbakemelding + "\n";
    }

    /**
     * Indre klasse for oppsett av en sak
     */ 
    public static class Oppsett {
        private String id;
        private String tittel;
        private String beskrivelse;
        private Prioritet prioritet;
        private String kategori;
        private String status;
        private String innsender;
        private String mottaker;
        private LocalDate opprettet;
        private LocalDate oppdatert;
        private String kommentar;
        private String tilbakemelding;

        /**
         * ----------- Konstruktører for hvert attributt -----------
         * 
         * @param id
         * @return this oppsett instanse
         */
        
        public Oppsett id(String id) { this.id = id; return this; }
        /**
         * 
         * @param tittel
         * @return this oppsett instanse
         */
        public Oppsett tittel(String tittel) { this.tittel = tittel; return this; }

        /**
         * 
         * @param beskrivelse
         * @return this oppsett instanse
         */
        public Oppsett beskrivelse(String beskrivelse) { this.beskrivelse = beskrivelse; return this; }

        /**
         * 
         * @param prioritet
         * @return this oppsett instanse
         */
        public Oppsett prioritet(Prioritet prioritet) { this.prioritet = prioritet; return this; }

        /**
         * 
         * @param kategori
         * @return this oppsett instanse
         */
        public Oppsett kategori(String kategori) { this.kategori = kategori; return this; }

        /**
         * 
         * @param status
         * @return this oppsett instanse
         */
        public Oppsett status(String status) { this.status = status; return this; }

        /**
         * 
         * @param innsender
         * @return this oppsett instanse
         */
        public Oppsett innsender(String innsender) { this.innsender = innsender; return this; }

        /**
         * 
         * @param mottaker
         * @return this oppsett instanse
         */
        public Oppsett mottaker(String mottaker) { this.mottaker = mottaker; return this; }

        /**
         * 
         * @param opprettet
         * @return this oppsett instanse
         */
        public Oppsett opprettet(LocalDate opprettet) { this.opprettet = opprettet; return this; }

        /**
         * 
         * @param oppdatert
         * @return this oppsett instanse
         */
        public Oppsett oppdatert(LocalDate oppdatert) { this.oppdatert = oppdatert; return this; }

        /**
         * 
         * @param kommentar
         * @return this oppsett instanse
         */
        public Oppsett kommentar(String kommentar) { this.kommentar = kommentar; return this; }

        /**
         * 
         * @param tilbakemelding
         * @return this oppsett instanse
         */
        public Oppsett tilbakemelding(String tilbakemelding) { this.tilbakemelding = tilbakemelding; return this; }

        /**
         * 
         * @return nytt saks-objekt
         */
        public Sak bygg() {
            return new Sak(this);
        }
    }
}

