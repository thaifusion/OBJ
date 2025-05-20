package com.eksamen2025.server.nettverk;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;

import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.felles.Sak;
import com.eksamen2025.server.dao.BrukerDao;
import com.eksamen2025.server.dao.DatabaseUtil;
import com.eksamen2025.server.dao.KategoriDao;
import com.eksamen2025.server.dao.PrioritetDao;
import com.eksamen2025.server.dao.SakDao;

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

<<<<<<< HEAD
    @Override
    public void run() {
        try (
            ObjectOutputStream ut = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inn = new ObjectInputStream(socket.getInputStream())
        ) {
            while (true) {
                Object mottatt;

                try {
                    mottatt = inn.readObject();
                } catch (EOFException eof) {
                    System.out.println("Klient er koblet ifra!");
                    break;
                }

                System.out.println("Mottatt objekt: " + mottatt);

                if (mottatt instanceof SocketRequest) {
                    SocketRequest req = (SocketRequest) mottatt;

                    // Her kan du legge inn logikk basert på f.eks. req.getType()
                    SocketResponse svar = behandleRequest(req);
                    ut.writeObject(svar);
                    ut.flush();

                } else if (mottatt instanceof String && mottatt.equals("exit")) {
                    System.out.println("Klienten koblet fra.");
                    break;
                } else {
                    System.out.println("Ugyldig forespørsel mottatt.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Feil under behandling: " + e.getMessage());
            e.printStackTrace();
=======
public void run() {
    try (
        ObjectOutputStream ut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inn = new ObjectInputStream(socket.getInputStream());
        Connection conn = DatabaseUtil.getConnection();
    )   {
            Object mottatt;
            while ((mottatt = inn.readObject()) != null) {
                if (mottatt instanceof SocketRequest) {
                SocketRequest req = (SocketRequest) mottatt;
                switch (req.getType()) {
                    case "HENT_BRUKERE":
                        BrukerDao brukerDao = new BrukerDao(conn);
                        ut.writeObject(new SocketResponse(true, brukerDao.hentAlleBrukere()));
                        break;
                    case "GET_PRIORITIES":
                        PrioritetDao prioritetDao = new PrioritetDao(conn);
                        ut.writeObject(new SocketResponse(true, prioritetDao.hentAllePrioriteter()));
                        break;
                    case "GET_CATEGORIES":
                        KategoriDao kategoriDao = new KategoriDao(conn);
                        ut.writeObject(new SocketResponse(true, kategoriDao.hentAlleKategorier()));
                        break;
                    case "INSERT":
                        Sak sak = (Sak) req.getData();
                        SakDao sakDao = new SakDao(conn);
                        sakDao.leggTilSak(sak);
                        ut.writeObject(new SocketResponse(true, "Sak lagret"));
                        break;
                    default:
                        ut.writeObject(new SocketResponse(false, "Ukjent forespørsel"));
                        break;
                }
            } else {
                ut.writeObject(new SocketResponse(false, "Ugyldig forespørsel"));
            }
>>>>>>> main
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    }
<<<<<<< HEAD

    private SocketResponse behandleRequest(SocketRequest req) {
    String type = req.getType();
    Object data = req.getData();
    String brukernavn = req.getBrukernavn();

    switch (type) {
        case "hentBrukere":
            // Her kan du hente brukere fra database hvis du har det
            return new SocketResponse(true, "Brukerliste sendt");

        case "lagreSak":
            // Du kan bruke `data` til å få tak i selve saken
            // f.eks.: Sak sak = (Sak) data;
            return new SocketResponse(true, "Saken er lagret");

        default:
            return new SocketResponse(false, "Ukjent forespørsel: " + type);
    }
    }
=======
}
    
>>>>>>> main
}
