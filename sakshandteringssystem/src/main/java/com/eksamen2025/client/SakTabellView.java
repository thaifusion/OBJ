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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;


/** @author Ranem
 * 
 * Klasser som bygger opp en tabellvisning og filtrere saker
 * Tester og Utvikler seregne saker men Ledere ser alle saker
 */
public class SakTabellView {
    private static final int PORT = 3000;
    protected final Bruker aktivBruker;
    protected TableView<Sak> tabell = new TableView<>();
    protected ObservableList<Sak> saker = FXCollections.observableArrayList();
    protected Label statusLabel = new Label("Ingen data lastet ennå.");
    protected ComboBox<String> cbPrioritet = new ComboBox<>();
    protected ComboBox<String> cbKategori = new ComboBox<>();
    protected ComboBox<String> cbStatus = new ComboBox<>();
    protected TextField tfTittelSok = new TextField();
    protected TextField tfBeskrivelseSok = new TextField();
    protected TextField tfStartÅr = new TextField();
    protected TextField tfSluttÅr = new TextField();
    
     /**
     * Konstruktør
     * 
     * @param bruker som er logget inn.
     */
    public SakTabellView (Bruker bruker){
        this.aktivBruker = bruker;
        byggTabell();
        hentOgFiltrerSaker();
    }

    /**
     * Kolonnene for tabellen
     * Hver kolonne Knytte til egenskap i {@link Sak}-klassen ved hjelp av JavaFX bindings.
     * Resultat settes i {@code tabell}-objektet.
     */
    protected void byggTabell() {
        TableColumn<Sak, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> cellData.getValue().saksIdProperty());

        TableColumn<Sak, String> colTittel = new TableColumn<>("Tittel");
        colTittel.setCellValueFactory(cellData -> cellData.getValue().tittelProperty());

        // Kort visning av lang titler
        colTittel.setCellFactory(tc -> new TableCell<>(){
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                setText(empty || item ==null ? null : item.length() > 20 ? item.substring(0, 18) + "..." : item);
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
        colOppdatert.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOppdatert().toString()));

        TableColumn<Sak, String> colOpprettet = new TableColumn<>("Opprettet");
        colOpprettet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOpprettet().toString()));

        TableColumn<Sak, String> colKommentar = new TableColumn<>("Utviklerkommentar");
        colKommentar.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKommentar()));

        
        TableColumn<Sak, String> colTilbakemelding = new TableColumn<>("Tester-tilbakemelding");
        colTilbakemelding.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTilbakemelding()));

        // legg kolonner i tabell
        tabell.getColumns().setAll(List.of(
            colId, colTittel, colPrioritet, colKategori, colStatus,
            colRapportor, colMottaker, colOppdatert, colOpprettet,
            colKommentar, colTilbakemelding
        ));
    }

    /**
     * Hente alle saker fra server
     * Etter filtrering oppdatere tabell med relevant saker
     * 
     */
    private void hentOgFiltrerSaker() {
        try (Socket socket = new Socket("localhost", PORT);
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

        if (rolle == Rolle.TESTER){
            filtrert = alleSaker.stream().filter(s -> s.getInnsender().equals(aktivBruker.getBrukernavn())).collect(Collectors.toList());

        } else if (rolle == Rolle.UTVIKLER){
            filtrert = alleSaker.stream().filter(s -> aktivBruker.getBrukernavn().equals(s.getMottaker())).collect(Collectors.toList());
        }
        saker.setAll(filtrert);
        tabell.setItems(saker);
        System.out.println("Antall saker mottatt: " + saker.size());

        // Oppdater status
        if (filtrert.isEmpty()){
            statusLabel.setText("Ingen saker å vise for din rolle: " + rolle);
        } else {
            statusLabel.setText("Du har " + filtrert.size() + " saker (rolle: " + rolle + ").");
        }

        // Fyll nedtrekkslister
        cbPrioritet.getItems().setAll("Alle", "LAV", "MIDDELS", "HØY");
        cbPrioritet.setValue("Alle");

        cbKategori.getItems().setAll("Alle", "UI-feil", "Backend-feil", "Funksjonsforespørsel");
        cbKategori.setValue("Alle");

        cbStatus.getItems().setAll("Alle", "SUBMITTED", "ASSIGNED", "IN_PROGRESS", "FIXED", "RESOLVED", "TEST_FAILED", "CLOSED");
        cbStatus.setValue("Alle");



     }  catch (Exception e) {
        e.printStackTrace();
     }
    }

    /**
     * Filtrer saker i tabell basert på brukerens valgt
     * Hvis bruker velge "Alle" så ignoreres det kriteriet.
     * Resultat oppdaterer tabellen
     */

    protected void filtrerTabell() {
        List<Sak> filtrert = saker.stream()
        .filter(s -> cbPrioritet.getValue() == null 
             || cbPrioritet.getValue().equals("Alle") 
             || s.getPrioritet().name().equalsIgnoreCase(cbPrioritet.getValue()))
        .filter(s -> cbKategori.getValue() == null 
             || cbKategori.getValue().equals("Alle") 
             || s.getKategori().equalsIgnoreCase(cbKategori.getValue()))
        .filter(s -> cbStatus.getValue() == null 
             || cbStatus.getValue().equals("Alle") 
             || s.getStatus().equalsIgnoreCase(cbStatus.getValue()))  
        .filter(s -> tfTittelSok.getText().isEmpty() 
             ||  s.getTittel().toLowerCase().contains(tfTittelSok.getText().toLowerCase()))
        .filter(s -> tfBeskrivelseSok.getText().isEmpty() 
             ||  s.getBeskrivelse().toLowerCase().contains(tfBeskrivelseSok.getText().toLowerCase()))
        .filter(s -> {
            if (tfStartÅr.getText().isEmpty()) return true;
            try {
                int start = Integer.parseInt(tfStartÅr.getText());
                return s.getOpprettet().getYear() >= start;
            } catch (NumberFormatException e) {
                return true; 
            }
        })
        .filter(s -> {
            if (tfSluttÅr.getText().isEmpty()) return true;
            try {
                int slutt = Integer.parseInt(tfSluttÅr.getText());
                return s.getOpprettet().getYear() <= slutt;
            } catch (NumberFormatException e) {
                return true;
            }
        })
        .collect(Collectors.toList());

        tabell.setItems(FXCollections.observableArrayList(filtrert));
        statusLabel.setText("Viser " + filtrert.size() + " filtrerte saker.");
    }

    /**
     * 
     * Bygge og returner GUI visning for tabellen over saker
     * @return BorderPane
     */ 
    public BorderPane getView(){
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(15));

        // overskrift
        Label overskrift = new Label("Liste over saker");
        overskrift.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        BorderPane.setMargin(overskrift,new Insets(0, 0, 10, 0));
        layout.setTop(overskrift);

        // tabell midtstilt
        tabell.setItems(saker);
        tabell.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox midt = new VBox(10, byggFilterpanel(), tabell);
        layout.setCenter(midt);

        // Knapper nederst
        Button btnTilbake = new Button("Tilbake");
        btnTilbake.setOnAction(e -> {
            InsertView insertView = new InsertView();
            insertView.hentValgFraServer("localhost", PORT);
            new InsertController(insertView, aktivBruker.getRolle());

            Scene scene = new Scene(insertView.getView());
            Stage stage = (Stage) tabell.getScene().getWindow();
            stage.setScene(scene);
            
        });
        Button btnOppdater = new Button("Oppdater");
        btnOppdater.setOnAction(e -> oppdaterValgtSak());

        Button btnDetaljer = new Button("Vis detaljer");
        btnDetaljer.setOnAction(e -> {
            Sak valgt = tabell.getSelectionModel().getSelectedItem();
            if (valgt != null){
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

    /**
     * 
     * Bygge og returner et horisontalt panel
     * @return HBox- layout
     */ 
    protected HBox byggFilterpanel() {
        cbPrioritet.setPromptText("Prioritet");
        cbKategori.setPromptText("Kategori");
        cbStatus.setPromptText("Status");
        tfTittelSok.setPromptText("Søk tittel");
        tfBeskrivelseSok.setPromptText("Søk beskrivelse");
        tfStartÅr.setPromptText("Startår");
        tfSluttÅr.setPromptText("Sluttår");

        Button btnFiltrer = new Button("Søk");
        btnFiltrer.setOnAction(e -> filtrerTabell());

        return new HBox(10, cbPrioritet, cbKategori, cbStatus, tfTittelSok, tfBeskrivelseSok, tfStartÅr, tfSluttÅr, btnFiltrer);
    }

    /**
     * 
     * Åpen dialog for å oppdatere valgt sak i tabell
     * Hvis brukeren bekrefter så sende sak til server etter oppdatere via {@link NetworkClient#oppdaterSak(Sak)}.
     * Tabellen lastes inn på nytt etter oppdatering
     */ 
    protected void oppdaterValgtSak(){
        Sak valgt = tabell.getSelectionModel().getSelectedItem();
        if (valgt == null){
            new Alert(Alert.AlertType.WARNING, "Du må velge en sak først.").showAndWait();
            return;
        }

        Rolle rolle =aktivBruker.getRolle();
        // Tester kan tilgang til egen saker
        if (rolle == Rolle.TESTER && !valgt.getInnsender().equals(aktivBruker.getBrukernavn())) {
            new Alert(Alert.AlertType.WARNING,  "Testere kan bare oppdatere egne saker.").showAndWait();
            return;

        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Oppdater sak");
        dialog.setHeaderText("Endre status og legg inn kommentar");

        ComboBox<String> cbNyStatus = new ComboBox<>();
        if (rolle == Rolle.TESTER){
            cbNyStatus.getItems().addAll("RESOLVED", "TEST_FAILED");
        } else if (rolle == Rolle.UTVIKLER){
            cbNyStatus.getItems().addAll("IN_PROGRESS", "FIXED");
        } else if (rolle == Rolle.LEDER){
            cbNyStatus.getItems().setAll("ASSIGNED", "CLOSED", "SUBMITTED");
        }
        cbNyStatus.setValue(valgt.getStatus());

        TextArea kommentarFelt = new TextArea();
        kommentarFelt.setPromptText("Skriv " + (rolle == Rolle.TESTER ? "tilbakemelding" : "kommentar"));
        kommentarFelt.setPrefRowCount(4);

        VBox vbox = new VBox(10, 
                             new Label("Ny status:"), cbNyStatus,
                             new Label (rolle == Rolle.TESTER ? "Tilbakemelding:" : "Kommentar:"), kommentarFelt);
        ComboBox<String> cbMottaker = null;
        if (rolle == Rolle.LEDER){
            cbMottaker = new ComboBox<>();
            cbMottaker.getItems().setAll(NetworkClient.hentUtviklere());
            vbox.getChildren().addAll(new Label("Velg mottaker:"), cbMottaker);
        }
        
        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        final ComboBox<String> finalcbMottaker = cbMottaker;

        dialog.showAndWait().ifPresent(type -> {
            if (type == ButtonType.OK) {
                valgt.setStatus(cbNyStatus.getValue());
                valgt.setOppdatert(LocalDateTime.now());

                switch (rolle) {
                    case TESTER:
                        valgt.setTilbakemelding(kommentarFelt.getText());
                        break;
                    case UTVIKLER:
                        valgt.setKommentar(kommentarFelt.getText());
                        break;
                    case LEDER:
                        valgt.setKommentar(kommentarFelt.getText());
                        if (finalcbMottaker != null) {
                            valgt.setMottaker(finalcbMottaker.getValue());
                        }
                        break;
                
                }

                boolean ok = NetworkClient.oppdaterSak(valgt);
                if (ok) {
                    new Alert(Alert.AlertType.INFORMATION, "Sak oppdatert.").showAndWait();
                    hentOgFiltrerSaker();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Feil ved oppdatering.").showAndWait();
                }
            }
        });
    
    }

        /**
     * Hjelpemetode for å hente felt fra superklassen SakTabellView
     * @return TableView<Sak>
     */
    protected TableView<Sak> getTabell() {
        return tabell;
    }

    /**
     * Hjelpemetode for å hente aktiv bruker fra superklassen SakTabellView
     * @return Bruker
     */
    protected Bruker getAktivBruker() {
        return aktivBruker;
    }
}