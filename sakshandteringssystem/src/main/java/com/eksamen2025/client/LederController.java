package com.eksamen2025.client;

import com.eksamen2025.felles.Sak;

public class LederController {

    public static boolean oppdaterSak(Sak sak) {
        return NetworkClient.oppdaterSak(sak);
    }
}
