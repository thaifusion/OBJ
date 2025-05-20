
package com.eksamen2025.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

//klasse som lager brukergrensesnitt for å sende inn sak
public class InsertView {
    public TextField tfTitle = new TextField();
    public TextArea taDescription = new TextArea();
    public ComboBox<String> cbPriority = new ComboBox<>();
    public ComboBox<String> cbCategory = new ComboBox<>();
    public Button btnSubmit = new Button("Send inn sak");

    public VBox getView() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Tittel:"), 0, 0);
        grid.add(tfTitle, 1, 0);
        grid.add(new Label("Beskrivelse:"), 0, 1);
        grid.add(taDescription, 1, 1);
        grid.add(new Label("Prioritet:"), 0, 2);
        grid.add(cbPriority, 1, 2);
        grid.add(new Label("Kategori:"), 0, 3);
        grid.add(cbCategory, 1, 3);
        grid.add(btnSubmit, 1, 4);

        VBox layout = new VBox(15, grid);
        layout.setPadding(new Insets(20));
        return layout;
    }

    //Disse metodene brukes for å sette verdier dynamisk (kalles etter data er hentet fra server)
    public void setPrioritetsvalg(List<String> prioriteter) {
        cbPriority.getItems().setAll(prioriteter);
    }

    public void setKategoriValg(List<String> kategorier) {
        cbCategory.getItems().setAll(kategorier);
    }

    // Metode for å hente prioritet og kategori fra serveren
    public void hentValgFraServer(String host, int port) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            //Hent prioriteter
            out.writeObject("GET_PRIORITIES");
            out.flush();
            List<String> prioriteter = (List<String>) in.readObject();
            setPrioritetsvalg(prioriteter);

            // Hent kategorier
            out.writeObject("GET_CATEGORIES");
            out.flush();
            List<String> kategorier = (List<String>) in.readObject();
            setKategoriValg(kategorier);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}