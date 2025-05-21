package com.eksamen2025.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.felles.Bruker;
import com.eksamen2025.felles.Rolle;
import com.eksamen2025.felles.Sak;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SakTabellView {
    protected final Bruker aktivBruker;
    protected TableView<Sak> tabell = new TableView<>();
    private ObservableList<Sak> saker = FXCollections.observableArrayList();
    private Label statusLabel = new Label("Ingen data lastet ennå.");
    
    // Setter opp tabellen med kolonner, Sara bruker i UtviklerView
    protected TableView<Sak> getTabell() {
        return tabell;
    }

    // Setter opp statusLabel, Sara bruker i UtviklerView
    protected Bruker getAktivBruker() {
        return aktivBruker;
    }

    private ComboBox<String> cbPrioritet = new ComboBox<>();
    private ComboBox<String> cbKategori = new ComboBox<>();
    private ComboBox<String> cbStatus = new ComboBox<>();
    private TextField tfTittelSok = new TextField();
    private TextField tfBeskrivelseSok = new TextField();
    private TextField tfStartÅr = new TextField();
    private TextField tfSluttÅr = new TextField();


    public SakTabellView(Bruker bruker) {
        this.aktivBruker = bruker;
        byggTabell();
        hentOgFiltrerSaker();
    }

    private void byggTabell() {
        TableColumn<Sak, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> cellData.getValue().saksIdProperty());

        TableColumn<Sak, String> colTittel = new TableColumn<>("Tittel");
        colTittel.setCellValueFactory(cellData -> cellData.getValue().tittelProperty());

        colTittel.setCellFactory(tc -> new TableCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null :
                item.length() > 20 ? item.substring(0, 18) + "..." : item);
        }
    });

        TableColumn<Sak, String> colPrioritet = new TableColumn<>("Prioritet");
        colPrioritet.setCellValueFactory(cellData -> cellData.getValue().prioritetProperty().asString());

        TableColumn<Sak, String> colKategori = new TableColumn<>("Kategori");
        colKategori.setCellValueFactory(cellData -> cellData.getValue().kategoriProperty());

        TableColumn<Sak, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        TableColumn<Sak, String> colRapportor = new TableColumn<>("Rapportør");
        colRapportor.setCellValueFactory(cellData -> cellData.getValue().rapportorProperty());

        TableColumn<Sak, String> colMottaker = new TableColumn<>("Mottaker");
        colMottaker.setCellValueFactory(cellData -> cellData.getValue().mottakerProperty());

        TableColumn<Sak, String> colOppdatert = new TableColumn<>("Oppdatert");
        colOppdatert.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getOppdatert().toString()));
        
        // Opprettet-dato
        TableColumn<Sak, String> colOpprettet = new TableColumn<>("Opprettet");
        colOpprettet.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getOpprettet().toString()));

       // Utviklerkommentar
       TableColumn<Sak, String> colKommentar = new TableColumn<>("Utviklerkommentar");
       colKommentar.setCellValueFactory(cellData ->
       new SimpleStringProperty(cellData.getValue().getKommentar()));

       // Tester-tilbakemelding
      TableColumn<Sak, String> colTilbakemelding = new TableColumn<>("Tester-tilbakemelding");
      colTilbakemelding.setCellValueFactory(cellData ->
      new SimpleStringProperty(cellData.getValue().getTilbakemelding()));


        tabell.getColumns().setAll(List.of(
    colId, colTittel, colPrioritet, colKategori, colStatus, colRapportor, colMottaker, colOppdatert, colOpprettet,
    colKommentar, colTilbakemelding
));

    }

    private void hentOgFiltrerSaker() {
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
                for (Object o : (List<?>) data) {
                    if (o instanceof Sak) {
                        alleSaker.add((Sak) o);
                    }
                }
            }

            Rolle rolle = aktivBruker.getRolle();
            List<Sak> filtrert = alleSaker;

            if (rolle == Rolle.TESTER) {
                filtrert = alleSaker.stream()
                        .filter(s -> s.getInnsender().equals(aktivBruker.getBrukernavn()))
                        .collect(Collectors.toList());
            } else if (rolle == Rolle.UTVIKLER) {
                filtrert = alleSaker.stream()
                        .filter(s -> aktivBruker.getBrukernavn().equals(s.getMottaker()))
                        .collect(Collectors.toList());
            }

            saker.setAll(filtrert);
            System.out.println("Antall saker mottatt: " + saker.size());
            tabell.setItems(saker);

            // Sett status i GUI
            if (filtrert.isEmpty()) {
              statusLabel.setText("Ingen saker å vise for din rolle: " + rolle);
            } else {
            statusLabel.setText("Du har " + filtrert.size() + " saker (rolle: " + rolle + ").");
          }

          cbPrioritet.getItems().setAll("LAV", "MIDDELS", "HØY");
          cbKategori.getItems().setAll("UI-feil", "Backend-feil", "Funksjonsforespørsel");
          cbStatus.getItems().setAll("SUBMITTED", "ASSIGNED", "IN_PROGRESS", "FIXED", "RESOLVED", "TEST_FAILED", "CLOSED");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void filtrerTabell() {
    List<Sak> filtrert = saker.stream()
        .filter(s -> cbPrioritet.getValue() == null || s.getPrioritet().name().equalsIgnoreCase(cbPrioritet.getValue()))
        .filter(s -> cbKategori.getValue() == null || s.getKategori().equalsIgnoreCase(cbKategori.getValue()))
        .filter(s -> cbStatus.getValue() == null || s.getStatus().equalsIgnoreCase(cbStatus.getValue()))
        .filter(s -> tfTittelSok.getText().isEmpty() || s.getTittel().toLowerCase().contains(tfTittelSok.getText().toLowerCase()))
        .filter(s -> tfBeskrivelseSok.getText().isEmpty() || s.getBeskrivelse().toLowerCase().contains(tfBeskrivelseSok.getText().toLowerCase()))
        .filter(s -> {
            if (tfStartÅr.getText().isEmpty()) return true;
            // Legger inn en sjekk på at bruker skriver inn et tall i år-feltet
            // Det vil bli ignorert hvis det er bokstaver eller ugyldig tall!
            try {
                int start = Integer.parseInt(tfStartÅr.getText());
                return s.getOpprettet().toLocalDateTime().getYear() >= start;
            } catch (NumberFormatException e) {
                return true; 
            }
        })
        .filter(s -> {
            if (tfSluttÅr.getText().isEmpty()) return true;
            try {
                int slutt = Integer.parseInt(tfSluttÅr.getText());
                return s.getOpprettet().toLocalDateTime().getYear() <= slutt;
            } catch (NumberFormatException e) {
                return true;
            }
        })
        .collect(Collectors.toList());

    tabell.setItems(FXCollections.observableArrayList(filtrert));
    statusLabel.setText("Viser " + filtrert.size() + " filtrerte saker.");
}

    public BorderPane getView() {
    BorderPane layout = new BorderPane();
    layout.setPadding(new Insets(15));

    // ✅ 1. Overskrift øverst
    Label overskrift = new Label("Liste over saker");
    overskrift.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    BorderPane.setMargin(overskrift, new Insets(0, 0, 10, 0));  // litt luft under
    layout.setTop(overskrift);

    // ✅ 2. Tabell midtstilt
    tabell.setItems(saker);
    tabell.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    VBox midt = new VBox(10, byggFilterpanel(), tabell);
    layout.setCenter(midt);

    // ✅ 3. Knapper nederst
    Button btnTilbake = new Button("Tilbake");
    btnTilbake.setOnAction(e -> {
        InsertView insertView = new InsertView();
        insertView.hentValgFraServer("localhost", 3000);
        new InsertController(insertView, aktivBruker.getRolle());

        Scene scene = new Scene(insertView.getView());
        Stage stage = (Stage) tabell.getScene().getWindow();
        stage.setScene(scene);
    });

    Button btnOppdater = new Button("Oppdater");
    btnOppdater.setOnAction(e -> hentOgFiltrerSaker());

    Button btnDetaljer = new Button("Vis detaljer");
    btnDetaljer.setOnAction(e -> {
        Sak valgt = tabell.getSelectionModel().getSelectedItem();
        if (valgt != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Saksdetaljer");
            alert.setHeaderText("Detaljer for valgt sak");
            alert.setContentText(valgt.toString());
            alert.showAndWait();
        }
    });

    HBox bunn = new HBox(10, btnTilbake, btnOppdater, btnDetaljer, statusLabel);
    bunn.setPadding(new Insets(10));
    layout.setBottom(bunn);

    return layout;
}

private HBox byggFilterpanel() {
    cbPrioritet.setPromptText("Prioritet");
    cbKategori.setPromptText("Kategori");
    cbStatus.setPromptText("Status");
    tfTittelSok.setPromptText("Søk tittel");
    tfBeskrivelseSok.setPromptText("Søk beskrivelse");
    tfStartÅr.setPromptText("Startår");
    tfSluttÅr.setPromptText("Sluttår");

    Button btnFiltrer = new Button("Søk");
    btnFiltrer.setOnAction(e -> filtrerTabell());

    return new HBox(10, cbPrioritet, cbKategori, cbStatus, tfTittelSok, tfBeskrivelseSok, tfStartÅr, tfSluttÅr ,btnFiltrer);
}


}
