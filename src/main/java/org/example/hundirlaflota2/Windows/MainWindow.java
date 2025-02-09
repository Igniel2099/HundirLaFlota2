package org.example.hundirlaflota2.Windows;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.MainController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class MainWindow extends FatherWindow {

    public MainWindow(Cliente Client) {
        controller = new MainController(Client);
        pathView = "/org/example/hundirlaflota2/Views/mainWindow.fxml";
    }

}
