package com.eksamen2025.client;

import com.eksamen2025.model.Sak;

import javafx.scene.control.Alert;

public class InsertController {
    private final InsertView view;
    private final Rolle brukerRolle;

   public InsertController(InsertView view, Rolle brukerRolle) {
    this.view = view;
    this.brukerRolle = brukerRolle;

    if (brukerRolle != Rolle.TESTER && brukerRolle != Rolle.LEDER) {
        view.setDisable(true);
        showAlert("Du har ikke tilgang til å opprette saker.");
        return;
    }

    attachEvents();
}

    private void attachEvents() {
        view.btnSubmit.setOnAction(e -> handleLagre());
    }

    private void handleLagre() {
        String tittel = view.tfTitle.getText();
        String beskrivelse = view.taDescription.getText();
        String prioritet = view.cbPriority.getValue();
        String kategori = view.cbCategory.getValue();

        if (tittel.isEmpty() || beskrivelse.isEmpty() || prioritet == null || kategori == null) {
            showAlert("Alle felt må fylles ut.");
            return;
        }

        Sak sak = new Sak(tittel, beskrivelse, prioritet, kategori);
        boolean success = NetworkClient.sendSak(sak);

        if (success) {
            showAlert("Sak registrert!");
            clearForm();
        } else {
            showAlert("Noe gikk galt. Prøv igjen.");
        }
    }

    private void clearForm() {
        view.tfTitle.clear();
        view.taDescription.clear();
        view.cbPriority.getSelectionModel().clearSelection();
        view.cbCategory.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
