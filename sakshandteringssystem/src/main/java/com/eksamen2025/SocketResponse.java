package com.eksamen2025;

import java.io.Serializable;

/** @author Ranem
 * SocketResponse: Et svar sendt fra serveren til klienten.
 * Inneholder informasjon om suksess og eventuelle resultater (eller feilmelding).
 */
public class SocketResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private Object result;

    /**
     * Oppretter en ny SocketResponse.
     *
     * @param success true hvis operasjonen på serveren var vellykket, ellers false
     * @param result resultatet av operasjonen, som kan være data, feilmelding eller null
     */
    public SocketResponse(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    /**
     * Returnerer om forespørselen var vellykket.
     *
     * @return true hvis operasjonen var suksessfull, ellers false
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returnerer resultatet som ble sendt fra serveren.
     *
     * @return et objekt med resultatet (kan være liste, enkeltobjekt, feilmelding, eller null)
     */
    public Object getResult() {
        return result;
    }
}
