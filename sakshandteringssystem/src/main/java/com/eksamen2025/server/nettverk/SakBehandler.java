package com.eksamen2025.server.nettverk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * SakBehandler-klassen: håndterer kommunikasjonen med en klient.
 * Den leser meldinger fra klienten og sender svar tilbake.
 * @author Vibeke
 */

public class SakBehandler implements Runnable {
    private Socket socket;

    public SakBehandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

            ObjectOutputStream ut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inn = new ObjectInputStream(socket.getInputStream());


            Object mottatt = inn.readObject();
            System.out.println("Motatt objektet: " + mottatt);

            // For å snede et svar tilbake til klienten
            String svar = "Server har mottatt objektet";
            ut.writeObject(svar);
            

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Feil under behandling av melding: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
