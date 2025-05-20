package com.eksamen2025.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.model.Sak;

public class NetworkClient {
    private static String brukernavn;

    public static void setBrukernavn(String navn) {
        brukernavn = navn;
    }

    public static boolean sendSak(Sak sak) {
        try (Socket socket = new Socket("localhost", 3000);
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

    public static List<String> hentBrukernavnFraServer() {
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            SocketRequest req = new SocketRequest("HENT_BRUKERNAVN", null, null);
            out.writeObject(req);

            SocketResponse res = (SocketResponse) in.readObject();
            return (List<String>) res.getResult();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
