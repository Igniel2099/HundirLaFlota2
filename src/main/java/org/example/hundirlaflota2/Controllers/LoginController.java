package org.example.hundirlaflota2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.Windows.UploadWindow;

public class LoginController extends FatherController{

    @FXML
    public void handleButtonClick(ActionEvent event) {
        MainApp mainApp = new MainApp();
        mainApp.setFatherWindow(new UploadWindow());
        try{
            mainApp.start(getStage());

        }catch(Exception e){
            System.out.println("Error en el LoginController: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
