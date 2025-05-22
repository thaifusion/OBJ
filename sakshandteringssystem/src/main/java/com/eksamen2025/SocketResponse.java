package com.eksamen2025;

import java.io.Serializable;

/** @author Ranem
 * Klassen {@code SocketResponse} svar som sender fra server til klient
 * Klassen immlementer {@link Serializable} for å sende over nettverk 
 */
public class SocketResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private Object result;

    /**
     * 
     * @param success
     * @param result
     */
    public SocketResponse(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 
     * @return Object
     */
    public Object getResult() {
        return result;
    }
}
