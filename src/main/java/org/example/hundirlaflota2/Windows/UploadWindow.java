package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXML;

import org.example.hundirlaflota2.Controllers.UploadController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class UploadWindow extends FatherWindow {

    public UploadWindow() {
        controller = new UploadController();
        pathView = "/org/example/hundirlaflota2/Views/uploadWindow.fxml";
    }
}
