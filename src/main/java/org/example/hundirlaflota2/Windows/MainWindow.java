package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.MainController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class MainWindow extends FatherWindow {

    public Cliente cliente;

    @Override
    public void getController(FXMLLoader loader, Stage stage) {

        MainController mainController = loader.getController();
        mainController.setStage(stage);

        mainController.setClient(cliente);

    }

    public MainWindow(Cliente Client) {
        cliente = Client;
        pathView = "/org/example/hundirlaflota2/Views/mainWindow.fxml";
    }

}
