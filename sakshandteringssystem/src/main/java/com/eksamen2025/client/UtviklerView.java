package com.eksamen2025.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.felles.Bruker;
import com.eksamen2025.felles.Sak;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** 
 * @author Sara
 * UtviklerView arver fra SakTabellView og oppretter en egen layout for utvikler.
 * Inneholder metode for å oppdatere sak, via en knapp som legges til i bunnen av tabellen.
 */
public class UtviklerView extends SakTabellView {
    private final BorderPane root;

    public UtviklerView(Bruker bruker) {
        super(bruker);

        root = (BorderPane) super.getView();
        HBox bunn = (HBox) root.getBottom();

        Button btnOppdaterSak = new Button("Oppdater Sak");
        btnOppdaterSak.setOnAction(e -> { oppdaterSak(); });

        bunn.getChildren().add(bunn.getChildren().size() - 1, btnOppdaterSak);
    }

    /**
     * Arver fra SakTabellView og oppretter en egen layout for utvikler.
     * @return root
     */

    public Parent getUtviklerView() {
        return root;
    }
    
    /** 
     * Metode for å oppdatere sak.
     * Viser dialogboks for å velge ny status og legge til utviklerkommentar.
     * Sender forespørsel til server for oppdatering av sak.
     * @return void
     */
    private void oppdaterSak() {
        Sak valgt = super.getTabell().getSelectionModel().getSelectedItem();
        if (valgt == null) {
            new Alert(Alert.AlertType.WARNING, "Ingen sak valgt.").showAndWait();
            return;
    }

    // Input
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Oppdater sak " + valgt.getTittel());
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    // Valg for status i oppdatering
    // kun "Pågår" og "Rettet" er tilgjengelig for utvikler
    ComboBox<String> cbStatus = new ComboBox<>();
    cbStatus.getItems().addAll("Pågår", "Rettet"); // Må jeg ha kode verdiene her istedet for? 
    cbStatus.setValue("Pågår");

    // Utviklerkommentar
    TextArea taUtviklerkommentar = new TextArea();
    taUtviklerkommentar.setPromptText("Skriv utviklerkommentar her...");
    taUtviklerkommentar.setPrefRowCount(4); // ?? 

    // Legger inn i dialogboks
    VBox vb = new VBox(10,
        new Label("Ny status:"),
        cbStatus,
        new Label("Utviklerkommentar:"),
        taUtviklerkommentar
    );

    // Setter padding og marginer
    vb.setPadding(new Insets(10));
    dialog.getDialogPane().setContent(vb);

    // Viser dialogboks og venter på svar
    Optional<ButtonType> svar = dialog.showAndWait();
    if (svar.isEmpty() || svar.get() != ButtonType.OK) {
        return;
    }

    // Henter valgt status og utviklerkommentar
    String valgtTekst = cbStatus.getValue();

    // Setter ny status basert på valgt tekst
    String nyStatusId; 
    if ("Pågår".equals(valgtTekst)) {
        nyStatusId = "2"; // Pågår
    } else if ("Rettet".equals(valgtTekst)) {
        nyStatusId = "3"; // Rettet
    } else {
        nyStatusId= String.valueOf(valgt.getStatus());
    }
     String utviklerkommentar = taUtviklerkommentar.getText(); 

     // lager payload for oppdatering som Map
        Map<String, String> payload = new HashMap<>();
        payload.put("sakId", String.valueOf(valgt.getId()));
        payload.put("status", nyStatusId);
        payload.put("utviklerkommentar", utviklerkommentar);

        // Sender socket forespørsel til server
        try (Socket socket = new Socket("localhost", 3000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                // Sender forespørsel til server
            SocketRequest forespørsel = new SocketRequest("OPPDATER_SAK", payload, super.getAktivBruker().getBrukernavn());
            out.writeObject(forespørsel); // Skriver ut objektet
            out.flush(); // Tømmer utdata-strømmen

            // Får svar fra server
            SocketResponse respons = (SocketResponse) in.readObject();
            if (!respons.isSuccess()) { // Hvis ikke suksess
                new Alert(Alert.AlertType.ERROR, "Feil ved oppdatering av sak: " + respons.getResult()
                ).showAndWait();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Feil ved oppdatering av sak: " + e.getMessage()).showAndWait();
            return;
        }

            // Oppdaterer GUI-et lokalt 
            valgt.setStatus(nyStatusId);
            valgt.setKommentar(utviklerkommentar);
            valgt.setOppdatert(new java.sql.Timestamp(System.currentTimeMillis())); // Oppdaterer sakens oppdatert-tidspunkt
            super.getTabell().refresh();

            // Viser bekreftelse via alert
            new Alert(Alert.AlertType.INFORMATION, "Sak oppdatert.").showAndWait();
    }

    /**
     * Hjelpemetode for å hente felt fra superklassen SakTabellView
     * @return TableView<Sak>
     */
    @Override
    protected TableView<Sak> getTabell() {
        return super.getTabell();
    }

    /**
     * Hjelpemetode for å hente aktiv bruker fra superklassen SakTabellView
     * @return Bruker
     */
    @Override
    protected Bruker getAktivBruker() {
        return super.getAktivBruker();
    }
}

