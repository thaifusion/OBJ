package com.eksamen2025.client;

import com.eksamen2025.felles.Rolle;
import com.eksamen2025.felles.Sak;
import com.eksamen2025.felles.Prioritet;

import java.time.LocalDate;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/** @author Ranem
 * Kontrollerklasse håndterer sending av ny status
 * tilgangskontroll og visuell tilbakemelding
 */
public class InsertController {
    private final InsertView view;
 
    /**
     * konstruktør for klassen
     *
     * @param view        
     * @param brukerRolle
     */
    public InsertController(InsertView view, Rolle brukerRolle) {
        this.view= view;
        attachEvents();
        // tester og leder kan opprette ny sak
        if (brukerRolle != Rolle.TESTER && brukerRolle != Rolle.LEDER) {
            view.deaktiverSkjemaFelter();
            showAlert("Du har ikke tilgang til å opprette saker.");
            return;
        }
   
    }
    
    /**
     * Knytter event-handler til "Send inn"-knappen for innsending sak
     * og "Vis mine saker" knappen for å bytte til tabellvisning
     */
    private void attachEvents() {
        view.btnSubmit.setOnAction(e -> handleLagre());
        view.btnVisSaker.setOnAction(e ->{
            SakTabellView tabellView = new SakTabellView(NetworkClient.getAktivBruker());
            Scene tabellScene = new Scene(tabellView.getView(),800,600);
            Stage stage = (Stage) view.btnVisSaker.getScene().getWindow();
            stage.setScene(tabellScene);
        });
    }

    /**
     * Håndterer innsending av sak
     * Validerer input, bygger en Sak og sender den til serveren.
     * viser en dialog
     */
    private void handleLagre() {
        String tittel = view.tfTitle.getText();
        String beskrivelse = view.taDescription.getText();
        String prioritetStr = view.cbPriority.getValue();
        String kategori = view.cbCategory.getValue();

        if (tittel.isEmpty() || beskrivelse.isEmpty() || prioritetStr == null || kategori == null) {
            showAlert("Alle felt må fylles ut.");
            return;
        }

        Prioritet prioritet;
        try {
            prioritet = Prioritet.valueOf(prioritetStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            showAlert("Ugyldig prioritet valgt.");
            return;
        }

        Sak sak = new Sak.Oppsett()
                .tittel(tittel)
                .beskrivelse(beskrivelse)
                .prioritet(prioritet)
                .kategori(kategori)
                .status("SUBMITTED")
                .innsender(NetworkClient.getBrukernavn())
                .opprettet(LocalDate.now())
                .oppdatert(LocalDate.now())
                .kommentar("")        // tom
                .tilbakemelding("")   // tom
                .bygg();

        
        boolean success = NetworkClient.sendSak(sak);

        if (success) {
            showAlert("Sak registrert!");
            clearForm();
        } else {
            showAlert("Noe gikk galt. Prøv igjen.");
        }
    }
    
    /**
     * Tømmer alle feltene
     */
    private void clearForm() {
        view.tfTitle.clear();
        view.taDescription.clear();
        view.cbPriority.getSelectionModel().clearSelection();
        view.cbCategory.getSelectionModel().clearSelection();
    }
    
    /**
     * Melding til brukeren
     * @param msg 
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
        }
}