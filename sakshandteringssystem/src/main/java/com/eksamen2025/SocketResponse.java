package com.eksamen2025;

import java.io.Serializable;

/**
 * SocketResponse: Et svar sendt fra serveren til klienten.
 * Inneholder informasjon om suksess og eventuelle resultater (eller feilmelding).
 */
public class SocketResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private Object result;

    public SocketResponse(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }
}
