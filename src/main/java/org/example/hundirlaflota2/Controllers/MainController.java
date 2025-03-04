package org.example.hundirlaflota2.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.Service.ConvertMatrix;
import org.example.hundirlaflota2.ServidorCliente.Cliente;
import org.example.hundirlaflota2.Windows.StartWindow;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MainController extends FatherController {

    private Cliente client;
    private Communication communicationMw;

    private Integer shipSpace;
    private List<Integer[]> listCoordinates = new ArrayList<>();
    private final List<List<Integer[]>> listAllCoordinates = new ArrayList<>();
    private List<Pane> panesSelected = new ArrayList<>();

    @FXML
    private Label quantityShipFour;

    @FXML
    private Label quantityShipThree;

    @FXML
    private Label quantityShipTwo;

    @FXML
    private Label quantityShipOne;

    @FXML
    private ImageView shipOne;

    @FXML
    private ImageView shipTwo;

    @FXML
    private ImageView shipThree;

    @FXML
    private ImageView shipFour;

    @FXML
    private GridPane myGrid;

    @FXML
    private Label labelOtherPlayer;

    private boolean buttonActionPressed = false;

    private boolean labelChanged = false;

    public void setCommunicationMw(Communication communicationMw) {
        this.communicationMw = communicationMw;
    }

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    private void initMatrix() {
        ConvertMatrix convertMatrix = new ConvertMatrix();
        convertMatrix.buildGridPaneWithPaneWater(myGrid);

        setEventToPane();

    }

    private void initListenerLabel(){
        labelOtherPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("El texto cambió de: " + oldValue + " a: " + newValue);
            labelChanged = true;
            if(buttonActionPressed) {
                changeStartWindow(listAllCoordinates);
            }
        });
    }

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private StackPane fatherPane;

    @FXML
    public void initialize() {

        backgroundImageView.fitWidthProperty().bind(fatherPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(fatherPane.heightProperty());
        backgroundImageView.setPreserveRatio(false);

        initMatrix();
        // inicializo el listener del label :)
        initListenerLabel();
    }

    /**
     * Este método sirve para notificar al otro cliente que estoy listo
     */
    private void notifyReady(){
        new Thread(communicationMw::mainCommunication).start();
    }

    /**
     * Este método sirve para cambiar de Scene a StarWindow
     */
    private void changeStartWindow(List<List<Integer[]>> listCoordinates) {

        MainApp mainApp = new MainApp();
        mainApp.setFatherWindow(new StartWindow(client,listCoordinates));
        try{
            mainApp.start(getStage());

        }catch(Exception e){
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }

    /**
     * Este es un hilo terciario que siempre va a estar escuchando la respuesta del otro cliente
     * Utilizo CompletableFuture.runAsync junto con un While(true) para asegurarme de que este hilo
     * se ejecuta de manera asíncrona al hilo principal que es la UI.
     * Con el Thread No funciona bien, porque se ejecutaba de manera síncrona, o sea, se ejecutaba después de
     * la UI terminara su trabajo
     */
    public void threadListens() {
        CompletableFuture.runAsync(() -> {
            String message;
            try{
                System.out.println("Estoy Escuchando");
                do {
                    message = client.receiveMessageString();
                    Platform.runLater(() -> labelOtherPlayer.setText("El otro jugador termino"));

                } while (message == null);
            } catch (IOException e) {
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }
        });
    }

    /**
     * Este método deshabilita el evento del botón, le cambia la imagen y le quita sus estilos
     * @param imageButton Espera el ImageView que sirve de Botón en la Pantalla
     */
    private  void DisableButtonVisuals(ImageView imageButton) {
        imageButton.setImage(new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/buttonWaiting.png")).toExternalForm()));
        imageButton.setOnMouseClicked(null);
        imageButton.getStyleClass().clear();
    }

    @FXML
    private void pressedStart(MouseEvent event) {
        if (quantityShipFour.getText().equals("0") &&
                quantityShipThree.getText().equals("0") &&
                quantityShipTwo.getText().equals("0")) {

            DisableButtonVisuals((ImageView) event.getSource()); // Deshabilita eventos del botón

            buttonActionPressed = true;

            notifyReady(); // Hilo Secundario que no molesta al hilo de la UI

            // Se ve que está lista se la pasa al StartWindow para recolocar todos los barcos en el otro sitio
            if(labelChanged) {
                changeStartWindow(listAllCoordinates); // Aquí debería pasarle por parametro la lista de coordenadas
            }
        }
    }

    /**
     * Método para que client envie mensajes
     */
    public void sendMessageClint(){
        try{

            client.sendMessageString("He Cambiado de pantalla soy " + client.getNombreCliente() + "Estoy en MainWindow");

        }catch(Exception e){
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }

    @FXML
    private void pressedShip(MouseEvent event){
        ImageView shipImage = (ImageView) event.getSource();
        String idShip = shipImage.getId();

        Map<String,Integer> map = new HashMap<>();
        map.put(shipOne.getId(),1);
        map.put(shipTwo.getId(),2);
        map.put(shipThree.getId(),3);
        map.put(shipFour.getId(),4);

        shipSpace = map.get(idShip);
    }

    public List<Integer[]> sorterCords() throws Exception {
        if (listCoordinates.size() != 2) {
            throw new Exception("No puedo hacer nada con esta lista de arrays Integer ");
        }

        Integer[] firstArray = listCoordinates.getFirst();
        Integer[] lastArray = listCoordinates.getLast();


        return firstArray[0] > lastArray[0] || firstArray[1] > lastArray[1]
                ? new ArrayList<>(Arrays.asList(lastArray, firstArray))
                : firstArray[0] < lastArray[0] || firstArray[1] < lastArray[1]
                ? listCoordinates
                : new ArrayList<>(Arrays.asList(lastArray, firstArray));
    }

    public List<Integer[]> getCoordinatesBetween(Integer[] start, Integer[] end) throws Exception {
        List<Integer[]> coordinates = new ArrayList<>();

        // Asegurar que el movimiento es en línea recta
        if (!start[0].equals(end[0]) && !start[1].equals(end[1])) {
            throw new Exception("No puedes hacer movimientos en diagonal");
        }

        // Movimiento horizontal (misma fila, diferente columna)
        if (start[0].equals(end[0])) {
            int row = start[0];
            int minCol = Math.min(start[1], end[1]);
            int maxCol = Math.max(start[1], end[1]);

            for (int col = minCol; col <= maxCol; col++) {
                coordinates.add(new Integer[]{row, col});
            }
        }
        // Movimiento vertical (misma columna, diferente fila)
        else if (start[1].equals(end[1])) {
            int col = start[1];
            int minRow = Math.min(start[0], end[0]);
            int maxRow = Math.max(start[0], end[0]);

            for (int row = minRow; row <= maxRow; row++) {
                coordinates.add(new Integer[]{row, col});
            }
        }
        return coordinates;
    }

    private boolean comprobateCoords(List<Integer[]> coords) {
        List<Integer[]> list = new ArrayList<>();
        try {
            list = getCoordinatesBetween(coords.getFirst(),coords.getLast());
        } catch (Exception e) {
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }

        if (list.size() == shipSpace){
            listAllCoordinates.add(list);
        }
        return list.size() == shipSpace;
    }

    private boolean mergeCells(GridPane gridPane, List<Integer[]> coords) {

        if (!comprobateCoords(coords)){

            for (Integer[] coord : coords) {
                for (Node node : gridPane.getChildren()) {
                    Integer nodeRow = GridPane.getRowIndex(node);
                    Integer nodeCol = GridPane.getColumnIndex(node);

                    if (Objects.equals(nodeRow, coord[0]) && Objects.equals(nodeCol, coord[1])) {
                        node.setStyle("");
                    }
                }
            }
            return false;
        }

        if (coords.isEmpty()) return false;

        ConvertMatrix matrix = new ConvertMatrix();
        matrix.mergeCells(gridPane, coords);

        removeEvents(gridPane, coords);

        return true;
    }

    private void loopCoords(GridPane gridPane,int elementStart, int elementEnd, int row, int col, String electionElement){
        for (int i = elementStart; i <= elementEnd; i++) {
            for (Node node : gridPane.getChildren()) {
                Integer nodeRow = GridPane.getRowIndex(node);
                Integer nodeCol = GridPane.getColumnIndex(node);

                if (electionElement.equals("row")){
                    if (Objects.equals(nodeRow, row) && Objects.equals(nodeCol, i)) {
                        node.setOnMouseClicked(null);
                    }
                }else if (electionElement.equals("col")){
                    if (Objects.equals(nodeRow, i) && Objects.equals(nodeCol, col)) {
                        node.setOnMouseClicked(null);
                    }
                }
            }
        }
    }

    private void removeEvents(GridPane gridPane, List<Integer[]> coords) {
        Integer[] first = coords.getFirst();
        Integer[] last = coords.getLast();

        int startCol = first[1];
        int startRow = first[0];
        int endCol = last[1];
        int endRow = last[0];

        if (startRow == endRow) {
            loopCoords(gridPane,startCol,endCol,startRow,startCol,"row");

        } else if (startCol == endCol) {
            loopCoords(gridPane,startCol,endCol,startRow,startCol,"col");
        }
    }

    private void handleCellClick(MouseEvent event, int row, int col) {

        Map<Integer,Label> map = new HashMap<>();
        map.put(4,quantityShipFour);
        map.put(3,quantityShipThree);
        map.put(2,quantityShipTwo);
        map.put(1,quantityShipOne);
        if (shipSpace != null && !map.get(shipSpace).getText().equals("0")){
            Pane clickedPane = (Pane) event.getSource();
            clickedPane.setStyle("-fx-background-color: blue;"); // Cambia color al hacer clic

            listCoordinates.add(new Integer[]{row, col});
            panesSelected.add(clickedPane);
            if(listCoordinates.size() == 2){
                try{
                    sorterCords();
                    boolean action = mergeCells(myGrid, sorterCords());
                    if (action) {

                        Label electionLabel = map.get(shipSpace);
                        electionLabel.setText(String.valueOf(Integer.parseInt(electionLabel.getText()) - 1));
                        for (Pane pane : panesSelected) {
                            pane.setStyle("");
                        }

                    }
                }catch (Exception e){
                    System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                    for (StackTraceElement element : e.getStackTrace()) {
                        System.err.println("\tat " + element);
                    }
                }
                listCoordinates = new ArrayList<>();
                panesSelected = new ArrayList<>();
            }
        }
    }

    private void setEventToPane() {
        for (Node node : myGrid.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);

            // Si no se ha especificado el índice, por defecto será 0.
            rowIndex = (rowIndex == null) ? 0 : rowIndex;
            colIndex = (colIndex == null) ? 0 : colIndex;

            final Integer row = rowIndex;
            final Integer col = colIndex;
            node.setOnMouseClicked(event -> handleCellClick(event, row, col));
        }
    }
}
