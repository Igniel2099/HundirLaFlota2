package org.example.hundirlaflota2.Controllers;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.Service.ConvertMatrix;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StartController extends FatherController{
    // cliente y comunicaciones
    private Cliente client;
    private Communication communicationSw;

    // booleano que me sirve para saber el estado del botón y de los turnos
    private BooleanProperty activatedButton;

    // posición de donde voy a disparar
    private List<Integer> positionGang;

    // lista de listas de todas las coordenadas de mis barcos
    private List<List<Integer[]>> arraysShips;

    // botón con el que disparo o espero
    @FXML
    private ImageView bulletButton;

    // Mi Grid de mis barcos
    @FXML
    private GridPane yourGrid;

    // Grid Enemigo
    @FXML
    private GridPane myGrid;

    public void setCommunicationSw(Communication communicationSw) {
        this.communicationSw = communicationSw;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    public void setArraysShips(List<List<Integer[]>> arraysShips) {
        this.arraysShips = arraysShips;
    }

    public void initCommunication(){
        try {
            activatedButton = new SimpleBooleanProperty(
                communicationSw.startCommunication()
            );

            System.out.println("Turno " + activatedButton);

            bulletButton.setImage(
                    activatedButton.get()
                    ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/buttonBang.png")).toExternalForm())
                    : new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/button.png")).toExternalForm())
            );

            if (!activatedButton.get()) {
                iniciarEscucha();
            }
        }catch (Exception e) {
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }

    public void turno() throws Exception{

        if(activatedButton.get()){
            // El que ataca
            // -x-y-
            // disparo provisional
            client.sendMessageString("1,2");
            int queToque = client.receiveMessageInt();
            System.out.println("Lo que toque fue: " + queToque);

            activatedButton.set(
                client.receiveMessageString().equals("Atacas")
            );

            bulletButton.setImage(
                    activatedButton.get()
                            ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/buttonBang.png")).toExternalForm())
                            : new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/button.png")).toExternalForm())
            );

            iniciarEscucha();
        }
    }

    @FXML
    public void pressedGang(MouseEvent event){
        ImageView imageView = (ImageView) event.getSource();
        System.out.println("Click del disparo " + imageView.getImage().getUrl());
        if (activatedButton.get()) {
            try{
                turno();
            }catch(Exception e){
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }
        }else{
            System.out.println("Turno " + activatedButton + " por lo que no puedes");
        }
    }
    
    public void iniciarEscucha() {

        Thread hiloEscuchando = new Thread(() -> {
            try{
                String disparoRecibido = client.receiveMessageString();

                System.out.println("El disparo es: " + disparoRecibido);
                // Mensaje del que toco provisional
                client.sendMessageInt(2);

                Platform.runLater(() -> {
                    try{

                        activatedButton.set(
                            client.receiveMessageString().equals("Atacas")
                        );
                    } catch (IOException e){
                        System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                        for (StackTraceElement element : e.getStackTrace()) {
                            System.err.println("\tat " + element);
                        }
                    }

                    bulletButton.setImage(
                            activatedButton.get()
                                    ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/buttonBang.png")).toExternalForm())
                                    : new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/button.png")).toExternalForm())
                    );

                });

            }catch (IOException e){
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }

        });

        hiloEscuchando.start();
    }

    public Pane getPaneFromGrid(GridPane grid, List<Integer> position) {
        int row = position.get(0); // Fila de getPosition
        int col = position.get(1); // Columna de getPosition

        for (Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                    return (Pane) node; // Retorna el Pane en esa posición
                }
            }
        }
        return null; // Si no encuentra el Pane
    }

    private void handleCellClick(MouseEvent event, int row, int col) {
        System.out.println("Clic en celda: (" + row + ", " + col + ")");

        if (positionGang!= null){
            Pane pane = getPaneFromGrid(yourGrid,positionGang);
            pane.setStyle("");
        }

        positionGang = new ArrayList<>(Arrays.asList(row, col));
        Pane pane = (Pane) event.getSource();
        pane.setStyle("-fx-background-color: red");
    }

    public void paneFillingYourGridPane(){
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Pane pane = new Pane();
                pane.setPrefSize(37.6, 36.8); // Tamaño de cada celda

                // Agregar evento de clic
                final int finalRow = row;
                final int finalCol = col;
                pane.setOnMouseClicked(event -> handleCellClick(event, finalRow, finalCol));
                pane.getStyleClass().add("pane-style");
                // Agregar Pane al GridPane
                yourGrid.add(pane, col, row);
            }
        }
    }

    public void gridPaneShipFilling(){
        ConvertMatrix convertMatrix = new ConvertMatrix();
        myGrid = convertMatrix.reBuildGridPane(arraysShips,myGrid);
    }
    @FXML
    public void initialize(){
        paneFillingYourGridPane();
    }
}
