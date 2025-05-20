package com.eksamen2025.server.nettverk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SakBehandler implements Runnable {
    private Socket socket;

    public SakBehandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            InputStream inn = socket.getInputStream();
            BufferedReader leser = new BufferedReader(new InputStreamReader(inn));

            OutputStream ut = socket.getOutputStream();
            PrintWriter skriver = new PrintWriter(ut, true);

            String melding = leser.readLine();
            System.out.println("Mottatt melding: " + melding);

            socket.close();
        } catch (IOException e) {
            System.out.println("Feil under behandling av melding: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
