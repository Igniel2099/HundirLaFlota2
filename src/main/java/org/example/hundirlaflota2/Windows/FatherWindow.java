package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.FatherController;

import java.io.IOException;

public class FatherWindow  {

    protected FatherController controller;
    protected String pathView;

    /**
     * Este método te permite obtener el controlador de la ventana y mandarle a este mismo controlador
     * el Stage.
     * @param loader el loader de la escena
     * @param stage el stage que tengo que pasarle al controlador
     */
    public void getController(FXMLLoader loader, Stage stage) {
        controller = loader.getController();
        controller.setStage(stage);
    }

    /**
     * Este método te permite cargar el fxml como la escena de un stage y mandarle el stage al controlador
     * @param stage el primer escenario
     * @return me devuelve la escena
     * @throws IOException controlar las posibles excepciones del programa
     */
    public Scene fxmlLoader( Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(pathView));
        Parent root = loader.load();

        // Obtener el controlador y pasar el Stage principal
        getController(loader, stage);

        return new Scene(root);
    }

}
