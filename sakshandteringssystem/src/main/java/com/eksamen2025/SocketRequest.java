package com.eksamen2025;

import java.io.Serializable;

public class SocketRequest implements Serializable {
    private String action;
    private Object payload;
    private String sender;

    public SocketRequest(String action, Object payload, String sender) {
        this.action = action;
        this.payload = payload;
        this.sender = sender;
    }

    public String getAction() {
        return action;
    }

    public Object getPayload() {
        return payload;
    }

    public String getSender() {
        return sender;
    }
}

