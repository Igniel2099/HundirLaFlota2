package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.StartController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class StartWindow  extends FatherWindow {

    public Cliente cliente;

    @Override
    public void getController(FXMLLoader loader, Stage stage) {
        StartController startController = loader.getController();

        startController.setStage(stage);
        startController.setClient(cliente);
        startController.sendMessageClint();
        try {
            System.out.println(startController.getClient().receiveMessageString());

            startController.turno();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public StartWindow(Cliente client) {
        cliente = client;
        pathView = "/org/example/hundirlaflota2/Views/startWindow.fxml";
    }
}
