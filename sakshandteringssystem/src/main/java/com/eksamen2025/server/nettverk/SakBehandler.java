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
import com.eksamen2025.server.dao.StatusDao;

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
                    System.out.println("Mottatt objekt: " + mottatt.toString());
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
                        case "HENT_SAKER":
                            SakDao sakDao2 = new SakDao(conn);
                            List<Sak> alleSaker = sakDao2.hentAlleSaker(); 
                            ut.writeObject(new SocketResponse(true, alleSaker));
                            break;
                        case "HENT_STATUS":
                            StatusDao statusDao = new StatusDao(conn);
                            List<String> alleStatus = statusDao.hentAlleStatus();
                            ut.writeObject(new SocketResponse(true, alleStatus));
                            break;
                        case "OPPDATER_SAK":
                            Sak sakOppdatert = (Sak) req.getData();
                            SakDao sakDaoUpdate = new SakDao(conn);
                            boolean success = sakDaoUpdate.oppdaterSak(sakOppdatert);
                            ut.writeObject(new SocketResponse(success, success ? "Sak oppdatert" : "Ingen rad oppdatert"));
                            break;
                        default:
                            ut.writeObject(new SocketResponse(false, "Ukjent forespørsel"));
                            break;
                    }
                } else {
                    ut.writeObject(new SocketResponse(false, "Ugyldig forespørsel"));
                }
            }
            
        } catch (Exception e) {
            // Kaster EOFException når klienten kobler fra. Vi har ikke funnet noen annen måte å håndtere dette på.
            // Den har tilsynelatende ingen innvirkning på serverens drift.
            // Vi har ikke opplevd noen problemer med funksjonalitet som resultat av denne exceptionen.
        }
    }
    
}
