package com.eksamen2025.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.eksamen2025.SocketRequest;
import com.eksamen2025.SocketResponse;
import com.eksamen2025.felles.Prioritet;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** @author Ranem
 * Klassen bygger opp GUI for innsending ny saker
 * samt en knapp for innsending og vise saker
 */
public class InsertView {
    public TextField tfTitle = new TextField();
    public TextArea taDescription = new TextArea();
    public ComboBox<String> cbPriority= new ComboBox<>();
    public ComboBox<String> cbCategory = new ComboBox<>();
    public Button btnSubmit = new Button("Send inn sak");
    public Button btnVisSaker = new Button("Vis mine saker");
    
    /**
     * 
     * @return VBox
     */
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
        
        HBox knapper = new HBox(10, btnSubmit, btnVisSaker);
        VBox layout = new VBox(15, grid, knapper);
        
        layout.setPadding(new Insets(20));
        return layout;
        
    }

    /**
     *
     * @param prioriteter hente liste fra serveren.
     */
    public void setPrioritetsvalg(List<String> prioriteter) {
        cbPriority.getItems().setAll(prioriteter);
    }
    
    /**
     *
     * @param kategorier hente liste fra serveren.
     */
    public void setKategoriValg(List<String> kategorier) {
        cbCategory.getItems().setAll(kategorier);
    }

    /**
     *
     * @param host 
     * @param port 
     */
     public void hentValgFraServer(String host, int port) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Hente Prioriteter
            SocketRequest req1 = new SocketRequest("GET_PRIORITIES", null, null);
            out.writeObject(req1);
            out.flush();

            SocketResponse res1 = (SocketResponse) in.readObject();
            Object data1 = res1.getResult();
            if(data1 instanceof List<?>){
                List<String> prioriteter = new ArrayList<>();
                for (Object object : (List<?>) data1){
                    if (object instanceof Prioritet){
                    prioriteter.add(object.toString());
                }
            }
            setPrioritetsvalg(prioriteter);
             
            }

            // Hente Kategorier
            SocketRequest req2 = new SocketRequest("GET_CATEGORIES", null, null);
            out.writeObject(req2);
            out.flush();

            SocketResponse res2 = (SocketResponse) in.readObject();
            Object data2 = res2.getResult();
            if(data2 instanceof List<?>){
                List<String> kategorier = new ArrayList<>();
                for (Object o : (List<?>) data2){
                    if (o instanceof String){
                    kategorier.add((String) o);
                }
            }
            setKategoriValg(kategorier);
             
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param disabled
     */
    public void setDisable(boolean disabled){
        tfTitle.setDisable(disabled);
        taDescription.setDisable(disabled);
        cbPriority.setDisable(disabled);
        cbCategory.setDisable(disabled);
        btnSubmit.setDisable(disabled);
    }
   /**
     * Deaktiverer feltene for sende ny saker
     */ 
    public void deaktiverSkjemaFelter(){
        tfTitle.setDisable(true);
        taDescription.setDisable(true);
        cbPriority.setDisable(true);
        cbCategory.setDisable(true);
        btnSubmit.setDisable(true);
    }

}