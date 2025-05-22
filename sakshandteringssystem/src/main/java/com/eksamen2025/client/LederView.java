package com.eksamen2025.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.felles.Bruker;
import com.eksamen2025.felles.Sak;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;


/** @author Jørgen og Sara
 * LederView-klassen: håndterer visningen for leder.
 * Den viser en tabell med saker og gir mulighet for å tildele utviklere og status.
 * 
 */
public class LederView extends SakTabellView { // Velger å arve fra SakTabellView for å gjenbruke kode/metoder
    private ComboBox<Bruker> utviklerComboBox = new ComboBox<>();
    private ComboBox<String> statusComboBox = new ComboBox<>();
    private Text melding = new Text();
    private Button btnTildelUtvikler = new Button("Tildel utvikler");
    private Button btnTilbake = new Button("Tilbake");
    private Label utviklerLabel = new Label("Utvikler:");
    private Label statusLabel = new Label("Status:");
    private VBox layout;

    public LederView(Bruker bruker) {
        super(bruker);
        byggGUI();
        utviklerComboBox.setPromptText("Velg utvikler");
        utviklerComboBox.getItems().setAll(NetworkClient.hentUtviklereFraServer());
        btnTildelUtvikler.setOnAction(e -> tildelSak());

        statusComboBox.setPromptText("Velg status");
        statusComboBox.getItems().setAll(NetworkClient.hentAlleStatusFraServer());
    }

    /**
     * Metode for å tildele en sak til en utvikler.
     * Den sjekker om en sak og utvikler er valgt, og oppdaterer sakens status og mottaker.
     */
    private void tildelSak() {
        Sak valgtSak = tabell.getSelectionModel().getSelectedItem();
        Bruker valgtUtvikler = utviklerComboBox.getValue();

        if (valgtSak == null) {                                         // Sjekker om bruker har valgt en sak
            melding.setText("Vennligst velg en sak.");
            return;
        }

        String valgtStatus = statusComboBox.getValue();                 // Henter status fra valgt sak

        if (valgtStatus == null) {                                      // Sjekker om bruker har valgt status
            melding.setText("Vennligst velg status.");
            return;
        }

        if (valgtUtvikler != null) {                                    // Sjekker om bruker har valgt utvikler, hvis ja, sett mottaker
            valgtSak.setMottaker(valgtUtvikler.getBrukernavn());
        }

        if (valgtStatus != null) {                                      // Sjekker om bruker har valgt status, hvis ja, sett status   
            valgtSak.setStatus(valgtStatus.toString());
        }

        
        if (LederController.oppdaterSak(valgtSak)) {                    // Sender sak til server, lagrer i basen og oppdaterer GUI
            melding.setText("Sak tildelt utvikler: " + valgtUtvikler.getBrukernavn() + " med status: " + valgtStatus);
            valgtSak.setStatus(valgtStatus);
            hentOgFiltrerSaker();
        } else {
            melding.setText("Feil ved tildeling av sak.");
        }

        melding.setText("Sak tildelt utvikler: " + valgtUtvikler.getBrukernavn());
        tabell.refresh();
    }

    @Override
    public void hentOgFiltrerSaker() {
        try (Socket socket = new Socket("localhost", 3000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            SocketRequest req = new SocketRequest("HENT_SAKER", null, aktivBruker.getBrukernavn());
            out.writeObject(req);
            out.flush();

            SocketResponse res = (SocketResponse) in.readObject();

            Object data = res.getResult();
            List<Sak> alleSaker = new ArrayList<>();
            if (data instanceof List<?>) {
                for (Object objekt : (List<?>) data) {
                    if (objekt instanceof Sak) {
                        alleSaker.add((Sak) objekt);
                    }
                }
            }
            
            saker.setAll(alleSaker);
            System.out.println("Antall saker mottatt: " + saker.size());
            tabell.setItems(saker);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Bygger GUI for LederView.
     * Inkluderer tabell for saker, knapper for tildeling av utvikler og tilbake til hovedmeny.
     */
    private void byggGUI() {
        byggFilterpanel();
        byggTabell();
        
        layout = new VBox(10);
        layout.setPadding(new Insets(15));

        HBox kontrollPanel = new HBox(10);
        kontrollPanel.getChildren().addAll(utviklerLabel, utviklerComboBox, statusLabel, statusComboBox, btnTildelUtvikler, btnTilbake);
        layout.getChildren().addAll(tabell, kontrollPanel, melding);
    }

    public VBox getLederView() {
        return layout;
    }
    
}
