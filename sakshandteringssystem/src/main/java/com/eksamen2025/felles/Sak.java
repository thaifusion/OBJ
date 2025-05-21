package com.eksamen2025.felles;

import java.io.Serializable;
import java.sql.Timestamp;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



/** @author Jørgen
 * Klassen representerer en sak.
 * Objektet er serialiserbart for å kunne sendes mellom server og klient.
 * 
 */
public class Sak implements Serializable {
    private String id;
    private String tittel;
    private String beskrivelse;
    private Prioritet prioritet;
    private String kategori;
    private String status;
    private String innsender;
    private String mottaker;
    private Timestamp opprettet;
    private Timestamp oppdatert;
    private String kommentar;
    private String tilbakemelding;

    /** Konstruktøren oppretter et sak-objekt med alle attributtene.
     * @param oppsett Oppsett-objektet som inneholder informasjon om saken
     */
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

    /** @author Sara
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }


    /** @author Sara
     * 
     * @return id
     */
    public String getId() {
        return id;
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
     * @param kategori
     */
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    /**
     * 
     * @return kategori
     */
    public String getKategori() {
        return kategori;
    }

    /**
     * 
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param innsender
     */
    public void setInnsender(String innsender) {
        this.innsender = innsender;
    }

    /**
     * 
     * @return innsender
     */
    public String getInnsender() {
        return innsender;
    }

    /**
     * 
     * @param mottaker
     */
    public void setMottaker(String mottaker) {
        this.mottaker = mottaker;
    }

    /**
     * 
     * @return mottaker
     */
    public String getMottaker() {
        return mottaker;
    }

    /**
     * 
     * @param opprettet
     */
    public void setOpprettet(Timestamp opprettet) {
        this.opprettet = opprettet;
    }

    /**
     * 
     * @return opprettet
     */
    public Timestamp getOpprettet() {
        return opprettet;
    }

    /**
     * 
     * @param oppdatert
     */
    public void setOppdatert(Timestamp oppdatert) {
        this.oppdatert = oppdatert;
    }

    /**
     * 
     * @return oppdatert
     */
    public Timestamp getOppdatert() {
        return oppdatert;
    }

    /**
     * 
     * @param kommentar
     */
    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    /**
     * 
     * @return kommentar
     */
    public String getKommentar() {
        return kommentar;
    }

    /**
     * 
     * @param tilbakemelding
     */
    public void setTilbakemelding(String tilbakemelding) {
        this.tilbakemelding = tilbakemelding;
    }
    
    /**
     * 
     * @return tilbakemelding
     */
    public String getTilbakemelding() {
        return tilbakemelding;
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
        private Timestamp opprettet;
        private Timestamp oppdatert;
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
        public Oppsett opprettet(Timestamp opprettet) { this.opprettet = opprettet; return this; }

        /**
         * 
         * @param oppdatert
         * @return this oppsett instanse
         */
        public Oppsett oppdatert(Timestamp oppdatert) { this.oppdatert = oppdatert; return this; }

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
         * @return nytt saks-objekt med "this" oppsett
         */
        public Sak bygg() {
            return new Sak(this);
        }
    }

    public StringProperty saksIdProperty() {
    return new SimpleStringProperty(id);
}

public StringProperty tittelProperty() {
    return new SimpleStringProperty(tittel);
}
 
public ObjectProperty<Prioritet> prioritetProperty() {
    return new SimpleObjectProperty<>(prioritet);
}

public StringProperty kategoriProperty() {
    return new SimpleStringProperty(kategori);
}

public StringProperty statusProperty() {
    return new SimpleStringProperty(status);
}

public StringProperty rapportorProperty() {
    return new SimpleStringProperty(innsender);
}

public StringProperty mottakerProperty() {
    return new SimpleStringProperty(mottaker);
}
}

