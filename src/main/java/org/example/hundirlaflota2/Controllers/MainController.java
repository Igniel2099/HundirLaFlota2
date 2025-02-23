package org.example.hundirlaflota2.Controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.ServidorCliente.Cliente;
import org.example.hundirlaflota2.Windows.StartWindow;

import java.io.IOException;

public class MainController extends FatherController {

    private Cliente client;
    private Communication communicationMw;

    @FXML
    private Label labelOtherPlayer;

    @FXML
    private Button buttonAction;

    public void setCommunicationMw(Communication communicationMw) {
        this.communicationMw = communicationMw;
    }

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    @FXML
    public void handleButtonClick(ActionEvent event) throws InterruptedException {
        Button buttonReady = (Button) event.getSource();
        buttonReady.setText("Waiting...");

        notifyReady(); // Hilo Secundario que no molesta al hilo de la UI


        if(labelOtherPlayer.getText().equals("El otro jugador ha terminado de configurar sus barcos.")){
            changeStartWindow();
        }



        System.out.println("Click en el boton y ejecutada toda su funcionalidad");
    }

    /**
     * Este método sirve para notificar al otro cliente que estoy listo
     */
    private void notifyReady(){
        new Thread(() -> {
            communicationMw.mainCommunication();
        }).start();
    }

    /**
     * Este método sirve para cambiar de Scena a StarWindow
     */
    private synchronized void changeStartWindow() {

        MainApp mainApp = new MainApp();
        mainApp.setFatherWindow(new StartWindow(client));
        try{
            mainApp.start(getStage());

        }catch(Exception e){
            System.out.println("Error en el MainController, método handleButtonClick: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Este es un hilo terciario que siempre va a estar escuchando la respuesta del otro cliente
     */
    public synchronized void threadListens(){
        new Thread(() -> {
            String message = "";
            try{
                message = client.receiveMessageString();
                System.out.println("Mensaje del otro cliente" + message);
                Platform.runLater(() -> {
                    labelOtherPlayer.setText("El otro jugador ha terminado de configurar sus barcos.");
                });
           } catch (IOException e) {
               throw new RuntimeException(e);
           }
        }).start();
    }

    /**
     * Método para que client envie mensajes
     */
    public void sendMessageClint(){
        try{

            client.sendMessageString("He Cambiado de pantalla soy " + client.getNombreCliente() + "Estoy en MainWindow");

        }catch(Exception e){
            System.out.println("--Error en el sendMessageClient de MainController: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        labelOtherPlayer.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("El texto cambió de: " + oldValue + " a: " + newValue);
            if(labelOtherPlayer.getText().equals("El otro jugador ha terminado de configurar sus barcos.") && buttonAction.getText().equals("Waiting...")){
                changeStartWindow();
            }
        });
    }



}
