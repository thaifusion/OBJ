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
import com.eksamen2025.felles.Rolle;
import com.eksamen2025.felles.Sak;

/** @author Ranem
 * Klassen håndatere kommunikasjon mellom klient og server
 * informasjon over nettverlet som bruker og saker
 */
public class NetworkClient {
    private static String brukernavn;
    private static final int PORT = 3000;
    private static Bruker aktivBruker;

 /**
     *
     * @param bruker 
     */  
    public static void setAktivBruker(Bruker bruker){
        aktivBruker = bruker;
    }

    /**
     *
     * @return Bruker (aktive brukere) 
     */
    public static Bruker getAktivBruker(){
        return aktivBruker;
    }

    /**
     *
     * @return Brukernavn eller "Anonymous" 
     */
    public static String getBrukernavn(){
        return aktivBruker != null ? aktivBruker.getBrukernavn() : "Anonymous";
    }

     /**
     *
     * @param navn 
     */
    public static void setBrukernavn(String navn){
        brukernavn = navn;
    }

    /**
     *
     * @param sak sende ny sak til server for å lagring
     * @return true 
     */
    public static boolean sendSak(Sak sak){
        try (Socket socket = new Socket("localhost", PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

        SocketRequest req = new SocketRequest("INSERT", sak, brukernavn);
        out.writeObject(req);

        SocketResponse res = (SocketResponse) in.readObject();
        return res.isSuccess();
        } catch  (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static List<Sak> hentSakerFraServer() {
    try (
        Socket socket = new Socket("localhost", 3000);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
    ) {
        SocketRequest req = new SocketRequest("HENT_SAKER", null, aktivBruker != null ? aktivBruker.getBrukernavn() : null);
        out.writeObject(req);
        out.flush();
        SocketResponse res = (SocketResponse) in.readObject();
        Object data = res.getResult();
        if (data instanceof List<?>) {
            List<?> list = (List<?>) data;
            List<Sak> saker = new ArrayList<>();
            for (Object obj : list) {
                if (obj instanceof Sak) {
                    saker.add((Sak) obj);
                }
            }
            return saker;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return new ArrayList<>();
}
    /**
     *
     * @return Liste fra server med bruker eller tom liste
     */
    public static List<Bruker> hentBrukereFraServer(){
         try (Socket socket = new Socket("localhost", PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

        SocketRequest req = new SocketRequest("HENT_BRUKERE", null, null);
        out.writeObject(req);

        SocketResponse res = (SocketResponse) in.readObject();
        Object data = res.getResult();

        if (data instanceof List<?>){
            List<?> list = (List<?>) data;
            List<Bruker> brukere = new ArrayList<>();

            for (Object obj : list){
                if (obj instanceof Bruker){
                    brukere.add((Bruker) obj);
                }
            }
            return brukere;
        }
    } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param sak oppdatere en sak
     * @return true hvis oppdatering eller false.
     */ 
    public static boolean oppdaterSak(Sak sak){
        try (Socket socket = new Socket("localhost", PORT);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

        SocketRequest req = new SocketRequest("OPPDATER_SAK", sak, getBrukernavn());
        out.writeObject(req);
        out.flush();

        SocketResponse res = (SocketResponse) in.readObject();
        return res.isSuccess();
    }  catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return Liste for utviklere.
     */
    public static List<String> hentUtviklere() {
        List<String> utviklere = new ArrayList<>();
        for (Bruker b: hentBrukereFraServer()) {
            if (b.getRolle().name().equals("UTVIKLER")) {
                utviklere.add(b.getBrukernavn());
            }

        }
        return utviklere;
    }

}


