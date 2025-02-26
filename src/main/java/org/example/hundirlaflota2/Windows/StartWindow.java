package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.Controllers.StartController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

import java.util.List;

public class StartWindow extends FatherWindow {

    public Cliente cliente;
    private final Communication communicationSw;
    private final List<List<Integer[]>> listAllCoordinates;

    @Override
    public void getController(FXMLLoader loader, Stage stage) {
        StartController startController = loader.getController();

        startController.setStage(stage);
        startController.setClient(cliente);

        startController.setCommunicationSw(communicationSw);

        startController.initCommunication();

        startController.setArraysShips(listAllCoordinates);
        startController.gridPaneShipFilling();

    }



    public StartWindow(Cliente client, List<List<Integer[]>> listCoordinates) {
        cliente = client;
        pathView = "/org/example/hundirlaflota2/Views/startWindow.fxml";
        communicationSw = new Communication(client);
        listAllCoordinates = listCoordinates;
    }
}
