package org.example.hundirlaflota2.Windows;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.StartController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class StartWindow  extends FatherWindow {

    public StartWindow(Cliente client) {
        controller = new StartController(client);
        pathView = "/org/example/hundirlaflota2/Views/startWindow.fxml";
    }
}
