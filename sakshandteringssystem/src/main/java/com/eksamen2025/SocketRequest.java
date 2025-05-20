package com.eksamen2025;

import java.io.Serializable;

/** @author Ranem
 * SocketRequest: En forespørsel sendt fra klienten til serveren.
 * Inneholder type handling, eventuelle data (f.eks. en Sak), og hvem som sendte det.
 */
public class SocketRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private Object data;
    private String brukernavn;
    
    /**
     * Oppretter en ny SocketRequest.
     *
     * @param type typen handling som forespørselen representerer (f.eks. "INSERT", "LOGIN")
     * @param data objektet som sendes med forespørselen (kan være null)
     * @param brukernavn brukernavnet til brukeren som sender forespørselen
     */
    public SocketRequest(String type, Object data, String brukernavn) {
        this.type = type;
        this.data = data;
        this.brukernavn = brukernavn;
    }

    /**
     * Henter typen forespørsel.
     *
     * @return forespørselens type (f.eks. "INSERT", "HENT_BRUKERE")
     */
    public String getType() {
        return type;
    }

    /**
     * Henter objektet som er sendt med forespørselen.
     *
     * @return objektet som er sendt (kan være null)
     */
    public Object getData() {
        return data;
    }

    /**
     * Henter brukernavnet til avsenderen.
     *
     * @return brukernavn som tilhører klienten som sendte forespørselen
     */
    public String getBrukernavn() {
        return brukernavn;
    }
}
