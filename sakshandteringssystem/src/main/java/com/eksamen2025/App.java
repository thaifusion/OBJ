package com.eksamen2025;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import com.eksamen2025.client.InsertView;
import com.eksamen2025.client.NetworkClient;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static BorderPane pane = new BorderPane();;

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Hent brukernavn fra server (via DAO på serversiden)
        List<String> usernames = NetworkClient.hentBrukernavnFraServer();


        // 2. Rullegardinvalg for brukernavn
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
            usernames.isEmpty() ? "Anonymous" : usernames.get(0), usernames);
        dialog.setTitle("Velkommen");
        dialog.setHeaderText("Velg brukernavn");
        dialog.setContentText("Brukernavn:");

        Optional<String> result = dialog.showAndWait();
        String valgtBruker = result.orElse("Anonymous");

        // 3. Send valgt brukernavn til NetworkClient for senere bruk
        NetworkClient.setBrukernavn(valgtBruker);

        // 4. Start GUI for innlegging av sak
        InsertView view = new InsertView();
        view.hentValgFraServer("localhost", 3000); 

        Scene scene = new Scene(view.getView());
        stage.setTitle("Sakshåndteringssystem - Innlogging som: " + valgtBruker);
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}