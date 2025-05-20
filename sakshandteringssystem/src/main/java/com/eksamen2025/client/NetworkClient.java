package com.eksamen2025.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.felles.Bruker;
import com.eksamen2025.felles.Sak;

/**  @author Ranem
 * NetworkClient håndterer kommunikasjonen mellom klient og server
 * via socket-basert overføring. Den støtter innsending av saker og
 * henting av brukere.
 */
public class NetworkClient {
    private static String brukernavn;
    private static final int PORT = 3000;
    private static Bruker aktivBruker;
    
    /**
     * Setter aktiv bruker for klienten.
     *
     * @param bruker Bruker-objektet som representerer den påloggede brukeren
     */
    public static void setAktivBruker(Bruker bruker) {
    aktivBruker = bruker;
  }

  /**
     * Henter brukernavnet til aktiv bruker. Hvis ingen bruker er aktiv,
     * returneres "Anonymous".
     *
     * @return brukernavn til aktiv bruker, eller "Anonymous" hvis ingen er logget inn
     */
public static String getBrukernavn() {
    return aktivBruker != null ? aktivBruker.getBrukernavn() : "Anonymous";
}
     /**
     * Setter brukernavn (kan brukes før innlogging for visning).
     *
     * @param navn brukernavn som skal lagres
     */
    public static void setBrukernavn(String navn) {
        brukernavn = navn;
    }
  
    /**
     * Sender en sak til serveren for innsending.
     *
     * @param sak Sak-objektet som skal sendes til serveren
     * @return true hvis innsendingen var vellykket, ellers false
     */
    public static boolean sendSak(Sak sak) {
        try (Socket socket = new Socket("localhost", PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            SocketRequest req = new SocketRequest("INSERT", sak, brukernavn);
            out.writeObject(req);

            SocketResponse res = (SocketResponse) in.readObject();
            return res.isSuccess();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Henter en liste over brukere fra serveren.
     *
     * @return en liste med Bruker-objekter, eller en tom liste hvis det oppstår feil
     */
    public static List<Bruker> hentBrukereFraServer() {
    try (Socket socket = new Socket("localhost", PORT);
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

        SocketRequest req = new SocketRequest("HENT_BRUKERE", null, null);
        out.writeObject(req);

        SocketResponse res = (SocketResponse) in.readObject();
        Object data = res.getResult();

        if (data instanceof List<?>) {
            List<?> list = (List<?>) data;
            List<Bruker> brukere = new ArrayList<>();

            for (Object obj : list) {
                if (obj instanceof Bruker) {
                    brukere.add((Bruker) obj);
                }
            }

            return brukere;
        }

    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }

    // Sørg for at du alltid returnerer noe!
    return new ArrayList<>();
}




}
