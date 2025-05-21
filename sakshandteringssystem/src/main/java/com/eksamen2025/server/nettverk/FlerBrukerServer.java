package com.eksamen2025.server.nettverk;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eksamen2025.database.DatabaseUtil;

/**
 * Hovedserver-klassen: oppretter en server og åpner en ServerSocket på port 3000 og
 * for skal kunne håndtere flere forespørsler fra ulike
 * brukere samtidig. Dette gjøres med ExecutorService og newCatchedThreadPool, for å unngå
 * trådbelastning - og som oppretter tråder etter behov og kan gjenbrukes.
 * @author Vibeke
 *
 */

public class FlerBrukerServer {
    private static final int PORT = 3000;
    private static ExecutorService trådbehandler = Executors.newCachedThreadPool();

    public static void main(String[] args) {

        //Koble serveren til databasen 

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn != null) {
                System.out.println("Databasen er koblet til.");
            }
        } catch (SQLException | IOException e) {
            System.out.println("Feil ved tilkobling til databasen: " + e.getMessage());
            return;
        }
        
        
        // Åpner opp en server socket på port 3000 og lytter etter tilkoblinger
        try (ServerSocket lytter = new ServerSocket(PORT)) {
            System.out.println("Serveren lytter til port" + PORT);

            while (true) {
                Socket klient = lytter.accept();
                System.out.println("Ny bruker er tilkoblet");
                trådbehandler.execute(new SakBehandler(klient));
            }   
        } catch (IOException e) {
            System.out.println("Serveren kunne ikke opprettes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


