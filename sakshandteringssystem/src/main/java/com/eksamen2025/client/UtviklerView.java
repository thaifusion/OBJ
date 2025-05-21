package com.eksamen2025.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    // Arver fra SakTabellView og oppretter en layout for utvikler
    public Parent getUtviklerView() {
        return root;
    }
    
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

    VBox vb = new VBox(10,
        new Label("Ny status:"),
        cbStatus,
        new Label("Utviklerkommentar:"),
        taUtviklerkommentar
    );

    vb.setPadding(new Insets(10));
    dialog.getDialogPane().setContent(vb);

    // 
    Optional<ButtonType> result = dialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        String nyStatus = cbStatus.getValue();
        String utviklerkommentar = taUtviklerkommentar.getText();

        try (Socket socket = new Socket("localhost", 3000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // sender forespørsel videre til server
            OppdaterSakPayload payload = new OppdaterSakPayload(String.valueOf(valgt.getId()), nyStatus, utviklerkommentar);
            System.out.println(payload.toString());

            SocketRequest forespørsel = new SocketRequest("OPPDATER_SAK", payload, super.getAktivBruker().getBrukernavn());
            out.writeObject(forespørsel);
            out.flush();

            // Får svar fra server
            SocketResponse respons = (SocketResponse) in.readObject();

            // Oppdaterer GUI-et lokalt 
            valgt.setStatus(nyStatus);
            valgt.setKommentar(utviklerkommentar);
            valgt.setOppdatert(new java.sql.Timestamp(System.currentTimeMillis())); // Oppdaterrer sakens oppdatert-tidspunkt
            super.getTabell().refresh();

            new Alert(Alert.AlertType.INFORMATION, "Sak oppdatert.").showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Feil ved oppdatering av sak: " + e.getMessage()).showAndWait();
        }
    }
    }

    // Hjelpemetoder for å hente felt fra superklassen SakTabellView
    @Override
    protected TableView<Sak> getTabell() {
        return super.getTabell();
    }

    @Override
    protected Bruker getAktivBruker() {
        return super.getAktivBruker();
    }

    // Klasse for payload?? 
    private static class OppdaterSakPayload implements java.io.Serializable {
        private static final long serialVersionUID = 1L; // SerialVersionUID for serialisering

        public final String sakId; 
        public final String status; 
        public final String utviklerkommentar;
        
        public OppdaterSakPayload(String sakId, String status, String utviklerkommentar) {
            this.sakId = sakId;
            this.status = status;
            this.utviklerkommentar = utviklerkommentar;
        }

        // Example usage method to avoid unused field warning
        @Override
        public String toString() {
            return "OppdaterSakPayload{" +
                    "sakId='" + sakId + '\'' +
                    ", status='" + status + '\'' +
                    ", utviklerkommentar='" + utviklerkommentar + '\'' +
                    '}';
        }
    }
}

