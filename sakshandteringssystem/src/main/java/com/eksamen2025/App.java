package com.eksamen2025;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.eksamen2025.client.InsertController;
import com.eksamen2025.client.InsertView;
import com.eksamen2025.client.LederView;
import com.eksamen2025.client.NetworkClient;
import com.eksamen2025.felles.Bruker;
import com.eksamen2025.felles.Rolle;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Hent brukere (med navn + rolle) fra server
        List<Bruker> brukere = NetworkClient.hentBrukereFraServer();
        List<String> brukernavnListe = brukere.stream()
        .map(Bruker::getBrukernavn)
        .collect(Collectors.toList());

        // Vis dialog for valg av brukernavn
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                brukernavnListe.isEmpty() ? "Anonymous" : brukernavnListe.get(0),
                brukernavnListe);
        dialog.setTitle("Velkommen");
        dialog.setHeaderText("Velg brukernavn");
        dialog.setContentText("Brukernavn:");

        Optional<String> result = dialog.showAndWait();
        String valgtBrukernavn = result.orElse("Anonymous");

        // Finn valgt bruker og lagre i NetworkClient
        Bruker valgtBruker = brukere.stream()
                .filter(b -> b.getBrukernavn().equals(valgtBrukernavn))
                .findFirst()
                .orElse(new Bruker("Anonymous", Rolle.TESTER)); // fallback

        NetworkClient.setAktivBruker(valgtBruker);

        // Start GUI
        if (valgtBruker.getRolle() == Rolle.LEDER) {
            LederView lederView = new LederView(valgtBruker);
            Scene sceneLeder = new Scene(lederView.getLederView(), 1200, 800);
            stage.setTitle("Sakshåndteringssystem - Innlogget som: " + valgtBruker.getBrukernavn() + " (" + valgtBruker.getRolle() + ")");
            stage.setScene(sceneLeder);
            stage.show();
            return;
        }
        InsertView view = new InsertView();
        view.hentValgFraServer("localhost", 3000);

        new InsertController(view, valgtBruker.getRolle());

        Scene scene = new Scene(view.getView());
        stage.setTitle("Sakshåndteringssystem - Innlogget som: " + valgtBruker.getBrukernavn() + " (" + valgtBruker.getRolle() + ")");
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
