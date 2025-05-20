package com.eksamen2025;

import java.io.Serializable;

public class SocketResponse implements Serializable {
    private boolean success;
    private Object result;
    private String message;

    public SocketResponse(boolean success, Object result, String message) {
        this.success = success;
        this.result = result;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}

