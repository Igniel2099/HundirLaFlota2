package org.example.hundirlaflota2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.Windows.UploadWindow;

public class LoginController extends FatherController{

    @FXML
    private TextField userField;

    @FXML
    private TextField passwordField;

    @FXML
    public void handleButtonClick(ActionEvent event) {

        if(userField.getText().isEmpty() || passwordField.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Campos Vacios no puede estar vacios");
            alert.showAndWait();
        } else if (userField.getText().equals(passwordField.getText())) {

            MainApp mainApp = new MainApp();
            mainApp.setFatherWindow(new UploadWindow());
            try{
                mainApp.start(getStage());

            }catch(Exception e){
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }
        }


    }
}
