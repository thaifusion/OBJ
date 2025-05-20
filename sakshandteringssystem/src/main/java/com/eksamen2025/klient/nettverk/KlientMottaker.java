package com.eksamen2025.klient.nettverk;

import java.io.IOException;
import java.io.ObjectInputStream;

public class KlientMottaker {

    private ObjectInputStream objektInn;

    public KlientMottaker(ObjectInputStream objektInn) {
        this.objektInn = objektInn;
    }

    public void mottaSvar() {
        try {
            Object svar = objektInn.readObject();
            if ( svar instanceof String) {
                System.out.println("Mottatt svar fra serveren: " + svar);
            } else {
                System.out.println("Mottatt objekt er ikke en String.");
            }    
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Feil under mottak av svar: " + e.getMessage());
            e.printStackTrace();
        }
    }   
}
