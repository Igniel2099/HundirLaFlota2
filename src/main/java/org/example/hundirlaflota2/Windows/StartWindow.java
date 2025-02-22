package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.Controllers.StartController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class StartWindow  extends FatherWindow {

    public Cliente cliente;
    private final Communication communicationSw;

    @Override
    public void getController(FXMLLoader loader, Stage stage) {
        StartController startController = loader.getController();

        startController.setStage(stage);
        startController.setClient(cliente);

        startController.setCommunicationSw(communicationSw);

        startController.initCommunication();

    }

    public StartWindow(Cliente client) {
        cliente = client;
        pathView = "/org/example/hundirlaflota2/Views/startWindow.fxml";
        communicationSw = new Communication(client);
    }
}
