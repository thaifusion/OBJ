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

public class NetworkClient {
    private static String brukernavn;
    private static final int PORT = 3000;
    private static Bruker aktivBruker;
    

    public static void setAktivBruker(Bruker bruker) {
    aktivBruker = bruker;
}

 // Henter aktiv bruker (brukes i SakTabellView og andre steder)
    public static Bruker getAktivBruker() {
        return aktivBruker;
    }

public static String getBrukernavn() {
    return aktivBruker != null ? aktivBruker.getBrukernavn() : "Anonymous";
}

    public static void setBrukernavn(String navn) {
        brukernavn = navn;
    }

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
            List<Bruker> bruker = new ArrayList<>();

            for (Object obj : list) {
                if (obj instanceof Bruker) {
                    bruker.add((Bruker) obj);
                }
            }

            return bruker;
        }

    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }

    // Sørg for at du alltid returnerer noe!
    return new ArrayList<>();
}


public static boolean oppdaterSak(Sak sak) {
    try (Socket socket = new Socket("localhost", 3000);
         ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

        SocketRequest req = new SocketRequest("OPPDATER_SAK", sak, getBrukernavn());
        out.writeObject(req);
        out.flush();

        SocketResponse res = (SocketResponse) in.readObject();
        return res.isSuccess();
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


}
