package org.example.hundirlaflota2.Windows;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
            startController.setActivatedButton(
                    new SimpleBooleanProperty(
                            startController.getClient().receiveMessageString().equals("Atacas")
                    )
            );
            System.out.println("Turno " + startController.getActivatedButton());

            startController.getBulletButton().setText(
                    startController.getActivatedButton().get()
                    ? "Presiona"
                    : "Esperando..."
            );

            // Inicio con el botón habilitado o deshabilitado, dependiendo del
            // valor del BooleanProperty activateButton
            // El signo (!) al principio porque el true deshabilita
            // el botón mientras que el booleano que me devuelve el servidor
            // depende de si le toca atacar o no
            startController.getBulletButton().setDisable(
                    !startController.getActivatedButton().get()
            );

            // hago un addListener para que escuche el cambio de valor del nuevo valor
            startController.getActivatedButton().addListener(
                    (obs, oldValue, newValue) ->
                            startController.getBulletButton().setDisable(newValue)
            );

            startController.getBulletButton().setOnAction(
                    event -> startController.getBulletButton().setDisable(
                            !startController.getActivatedButton().get()
                    )
            );

            notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public StartWindow(Cliente client) {
        cliente = client;
        pathView = "/org/example/hundirlaflota2/Views/startWindow.fxml";
    }
}
