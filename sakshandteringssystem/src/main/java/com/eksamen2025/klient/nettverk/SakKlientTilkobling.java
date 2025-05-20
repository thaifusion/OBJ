package com.eksamen2025.klient.nettverk;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 
 * Klient-klassen: oppretter en socket for å kommunisere med serveren på localhost: 3000.
 * Klienten kan sende og motta objekter.
 * Klienten kan også lukke tilkoblingen når den ikke lenger er nødvendig.
 * 
 * Hjelpeklasse for å håndtere tilkoblingen til serveren.
 * Den oppretter en socket og sender forespørselen til serveren.
 * @author Vibeke
 */

public class SakKlientTilkobling {
    private Socket socket;
    private ObjectOutputStream objektUt;
    private ObjectInputStream objektInn;

    String hostnavn = "localhost";
    int port = 3000;

    

    public void svarForespørsel() {
        try {
            socket = new Socket(hostnavn, port);
            objektUt = new ObjectOutputStream(socket.getOutputStream());
            objektInn = new ObjectInputStream(socket.getInputStream());
            System.out.println("Tilkoblet til serveren på " + hostnavn + ":" + port);
        } catch (Exception e) {
            System.out.println("Feil under tilkobling: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mottaForespørsel() { 
    }

    public ObjectOutputStream getObjectUt() {
        return objektUt;
    }

    public ObjectInputStream getObjectInn() {
        return objektInn;
    }

    public void lukkTilkobling() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Tilkoblingen er lukket.");
            }
        } catch (Exception e) {
            System.out.println("Feil under lukking av tilkobling: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Lager en main - kun for å sjekke om det er connection!
    public static void main(String[] args) {
        SakKlientTilkobling klient = new SakKlientTilkobling();
        klient.svarForespørsel();
        
        // KlientSender sender = new KlientSender(klient.getObjectUt());
        KlientSender sender = new KlientSender(klient.getObjectUt());
        sender.sendeEnForespørsel();

        // KlientMottaker mottaker = new KlientMottaker(klient.getObjectInn());
        KlientMottaker mottaker = new KlientMottaker(klient.getObjectInn());
        mottaker.mottaSvar();

        // Lukker tilkoblingen etter at forespørselen er sendt og svaret er mottatt
        klient.lukkTilkobling();
    }
}
