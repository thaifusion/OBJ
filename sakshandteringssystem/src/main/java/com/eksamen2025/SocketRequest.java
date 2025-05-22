package com.eksamen2025;

import java.io.Serializable;

/** @author Ranem
 * Klassen {@code SocketRequest} forespørsel som sender fra klient til server
 * Klassen immlementer {@link Serializable} for å sende over nettverk  
 */
public class SocketRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private Object data;
    private String brukernavn;

    /**
     * 
     * @param type
     * @param data
     * @param brukernavn
     */
    public SocketRequest(String type, Object data, String brukernavn) {
        this.type = type;
        this.data = data;
        this.brukernavn = brukernavn;
    }

    /**
     * 
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @return Object
     */
    public Object getData() {
        return data;
    }

    /**
     * 
     * @return String
     */
    public String getBrukernavn() {
        return brukernavn;
    }
}
