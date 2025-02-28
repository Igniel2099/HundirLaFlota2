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

    // Este atributo es el pane que selecciono al disparar
    private Pane paneSelected;

    // botón con el que disparo o espero
    @FXML
    private ImageView bulletButton;

    // Grid Enemigo
    @FXML
    private GridPane yourGrid;

    // Mi Grid de mis barcos
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
            if (positionGang == null || positionGang.size() > 2) {
                throw new Exception("Que pex cabrón eres imbecil o no saber elegir por eso te dejaron mmhv");
            }

            client.sendMessageString(positionGang.getLast() + "," + positionGang.getFirst());

            int queToque = client.receiveMessageInt();
            System.out.println("Lo que toque fue: " + queToque);
            try {
                changeGridWithGang(positionGang.getLast() + "," + positionGang.getFirst(), queToque);
            } catch (Exception e) {
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }

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

    public int searchCoordinatesWithGang(String gang){
        String[] coords = gang.split(",");
        int[] coordinates = {Integer.parseInt(coords[0]), Integer.parseInt(coords[1]) };
        for (List<Integer[]> list : arraysShips) {
            for(Integer[] array : list){
                if (array[0] == coordinates[0] && array[1] == coordinates[1]){
                    System.out.println("Toco algo ñaño");
                    return 2; // Toco barco
                }
            }
        }
        System.out.println("Tocaste agua ñaño");
        return 3; // Toca agua
    }

    private void changeGridWithGang(String gang, int queToque) throws Exception {
        String[] coords = gang.split(",");
        int[] coordinates = {Integer.parseInt(coords[0]), Integer.parseInt(coords[1]) };

        if (paneSelected == null) {
            throw new Exception("No hay pane seleccionado");
        }

        yourGrid.getChildren().remove(paneSelected); // Eliminamos el Pane

        // Crear un nuevo ImageView con la imagen correspondiente
        ImageView imageView = new ImageView(
                queToque == 3
                ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/water.png")).toExternalForm())
                : queToque == 2
                ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/take.png")).toExternalForm())
                : null
        );

        if (imageView.getImage() == null)
        {
            throw new Exception("No tengo registrado esto que has tocado");
        }

        imageView.setFitWidth(37.6);
        imageView.setFitHeight(36.8);

        // Agregar el ImageView en la misma posición (x, y)
        yourGrid.add(imageView, coordinates[0], coordinates[1]);
    }

    public void iniciarEscucha() {

        Thread hiloEscuchando = new Thread(() -> {
            try{
                String disparoRecibido = client.receiveMessageString();
                System.out.println("El disparo es: " + disparoRecibido);
                // Mensaje del que toco provisional
                int loqQueToco = searchCoordinatesWithGang(disparoRecibido);
                client.sendMessageInt(loqQueToco);

                Platform.runLater(() -> {
                    // Actualizar el grid del enemigo


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
                                    ? new Image(Objects.requireNonNull(
                                            getClass().getResource(
                                                    "/org/example/hundirlaflota2/Images/buttonBang.png")
                                    ).toExternalForm())

                                    : new Image(Objects.requireNonNull(
                                            getClass().getResource(
                                                    "/org/example/hundirlaflota2/Images/button.png")
                                    ).toExternalForm())
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

    private void handleCellClick(MouseEvent event, int row, int col) {
        System.out.println("Clic en celda: (" + row + ", " + col + ")");

        positionGang = new ArrayList<>(Arrays.asList(row, col));

        paneSelected = (Pane) event.getSource();
        paneSelected.setStyle("-fx-background-color: red");
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

                // Agregar Pane al GridPane y también agrego las coordenadas donde está ubicado el pane
//                GridPane.setRowIndex(pane, finalRow);
//                GridPane.setColumnIndex(pane, finalCol);
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
