package com.eksamen2025.client;

import com.eksamen2025.felles.Rolle;
import com.eksamen2025.felles.Sak;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.eksamen2025.felles.Prioritet;
import javafx.scene.control.Alert;

/** @author Ranem
 * Kontrollerklasse for InsertView.
 * Håndterer innsending av sak og tilgangskontroll basert på brukerens rolle.
 */
public class InsertController {
    private final InsertView view;
    //private final Rolle brukerRolle;

    /**
     * Oppretter en ny InsertController og konfigurerer visningen basert på brukerens rolle.
     *
     * @param view        GUI-komponenten som inneholder skjemaet.
     * @param brukerRolle Brukerens rolle (f.eks. TESTER, LEDER).
     */
    public InsertController(InsertView view, Rolle brukerRolle) {
        this.view = view;
        //this.brukerRolle = brukerRolle;

        // Kun TESTER og LEDER har tilgang til å opprette saker
        if (brukerRolle != Rolle.TESTER && brukerRolle != Rolle.LEDER) {
            view.setDisable(true);
            showAlert("Du har ikke tilgang til å opprette saker.");
            return;
        }

        attachEvents();
    }
    
    /**
     * Knytter event-handler til "Send inn"-knappen i visningen.
     */
    private void attachEvents() {
        view.btnSubmit.setOnAction(e -> handleLagre());
    }

    /**
     * Håndterer lagring (innsending) av sak.
     * Validerer input, bygger en Sak og sender den til serveren.
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
                .opprettet(Timestamp.valueOf(LocalDateTime.now())) // Tidspunkt registrert er to timer bak vår tidssone, fordi
                .oppdatert(Timestamp.valueOf(LocalDateTime.now())) // LocalDateTime.now() returnerer en tid utenfor vår tidssone. 
                .kommentar("")              // tom ved innsending
                .tilbakemelding("")   // tom ved innsending
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
     * Tømmer alle feltene i skjemaet.
     */
    private void clearForm() {
        view.tfTitle.clear();
        view.taDescription.clear();
        view.cbPriority.getSelectionModel().clearSelection();
        view.cbCategory.getSelectionModel().clearSelection();
    }
    
    /**
     * Viser en informasjonsdialog til brukeren.
     *
     * @param msg Meldingen som skal vises i dialogen.
     */
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}