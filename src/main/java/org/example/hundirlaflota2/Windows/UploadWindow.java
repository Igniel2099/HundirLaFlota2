package org.example.hundirlaflota2.Windows;

import org.example.hundirlaflota2.Controllers.UploadController;

public class UploadWindow extends FatherWindow {

    public UploadWindow() {
        controller = new UploadController();
        pathView = "/org/example/hundirlaflota2/Views/uploadWindow.fxml";
    }
}
