package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.UploadController;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class UploadWindow extends FatherWindow {
    private String name;

    @Override
    public void getController(FXMLLoader loader, Stage stage) {

        UploadController controller = loader.getController();
        controller.setStage(stage);
        controller.setClient(new Cliente(name));
    }

    public UploadWindow(String nameClient) {
        name = nameClient;
        controller = new UploadController();
        pathView = "/org/example/hundirlaflota2/Views/uploadWindow.fxml";
    }
}
