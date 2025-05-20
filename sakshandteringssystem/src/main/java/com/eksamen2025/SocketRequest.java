package com.eksamen2025;

import java.io.Serializable;

/**
 * SocketRequest: En forespørsel sendt fra klienten til serveren.
 * Inneholder type handling, eventuelle data (f.eks. en Sak), og hvem som sendte det.
 */
public class SocketRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private Object data;
    private String brukernavn;

    public SocketRequest(String type, Object data, String brukernavn) {
        this.type = type;
        this.data = data;
        this.brukernavn = brukernavn;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public String getBrukernavn() {
        return brukernavn;
    }
}
