package com.eksamen2025.server.nettverk;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.database.DatabaseUtil;
import com.eksamen2025.felles.Sak;
import com.eksamen2025.server.dao.BrukerDao;
import com.eksamen2025.server.dao.KategoriDao;
import com.eksamen2025.server.dao.PrioritetDao;
import com.eksamen2025.server.dao.SakDao;

/**
 * SakBehandler-klassen: håndterer kommunikasjonen med en klient.
 * 
 * @author Vibeke
 */

public class SakBehandler implements Runnable {
    private Socket socket;

    public SakBehandler(Socket socket) {
        this.socket = socket;
    }

public void run() {
    try (
        ObjectOutputStream ut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream inn = new ObjectInputStream(socket.getInputStream());
        Connection conn = DatabaseUtil.getConnection();
    )   {
            Object mottatt;
            while ((mottatt = inn.readObject()) != null) {
                SocketResponse svar;

            if (!(mottatt instanceof SocketRequest)) {

                svar = new SocketResponse(false, "Ugyldig forespørsel");
            } else {
                SocketRequest req = (SocketRequest) mottatt;
                try {
                    svar = behandleRequest(req, conn);
                } catch (Exception e) {
                    e.printStackTrace();
                    svar = new SocketResponse(false, "SERVER ERROR: " + e.getMessage());
                }
            }
            ut.writeObject(svar);
            ut.flush();
        }
    } catch (EOFException eof) {
        System.out.println("Klient koblet fra: " + socket.getInetAddress());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

/** 
 * Behandler forespørselen fra klienten.
 * @param req forespørselen fra klienten
 * @param conn forbindelsen til databasen
 * @return svar til klienten
 * @throws Exception hvis det oppstår en feil
 */
private SocketResponse behandleRequest(SocketRequest req, Connection conn) throws Exception {
    switch (req.getType()) {
        case "HENT_BRUKERE":
            BrukerDao brukerDao = new BrukerDao(conn);
            return new SocketResponse(true, brukerDao.hentAlleBrukere());

        case "GET_PRIORITIES":
            PrioritetDao prioritetDao = new PrioritetDao(conn);
            return new SocketResponse(true, prioritetDao.hentAllePrioriteter());

        case "GET_CATEGORIES":
            KategoriDao kategoriDao = new KategoriDao(conn);
            return new SocketResponse(true, kategoriDao.hentAlleKategorier());

        case "INSERT":
            Sak sak = (Sak) req.getData();
            SakDao sakDao = new SakDao(conn);
            sakDao.leggTilSak(sak);
            return new SocketResponse(true, "Sak lagret");

        case "HENT_SAKER":
            SakDao sakDao2 = new SakDao(conn);
            List<Sak> alleSaker = sakDao2.hentAlleSaker(); 
            return new SocketResponse(true, alleSaker);

        case "OPPDATER_SAK":
            Map<String, String> data = (Map<String, String>) req.getData();
            int sakId = Integer.parseInt(data.get("sakId"));
            String status = data.get("status");
            String utviklerkommentar = data.get("utviklerkommentar");

            int oppdatert = new SakDao(conn).oppdaterStatusOgKommentar(sakId, status, utviklerkommentar);

            return new SocketResponse(
                oppdatert == 1,
                oppdatert == 1 ? "OK" : "Fant ikke sak"
            );
            
        default:
            return new SocketResponse(false, "Ukjent forespørsel");
    }
}
}