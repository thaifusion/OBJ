package com.eksamen2025.klient.nettverk;

import java.io.ObjectOutputStream;

public class KlientSender {

    private ObjectOutputStream objektUt;

    public KlientSender (ObjectOutputStream objektUt) {
        this.objektUt = objektUt;
    }

    public void sendeEnForespørsel () {
        try {
            String forespørsel = "Hei server, jeg er en klient!";
            objektUt.writeObject(forespørsel);
            objektUt.flush();
            System.out.println("Forespørsel sendt til serveren!" );
        } catch (Exception e) {
            System.out.println("Feil under sending av forespørsel: " + e.getMessage());
            e.printStackTrace();
        }
    }    
}
