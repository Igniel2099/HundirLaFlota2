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

        notifyAndAwaitsResponse(); // Hilo Secundario que no molesta al hilo de la UI



        changeStartWindow();
    }

    /**
     * Este método sirve para notificar al otro cliente que estoy listo y ademas
     * espero al respuesta de otro cliente.
     */
    private void notifyAndAwaitsResponse(){
        new Thread(() -> {
            communicationMw.mainCommunication();
        }).start();
    }

    /**
     * Este método sirve para cambiar de Scena a StarWindow
     */
    private synchronized void changeStartWindow() throws InterruptedException {

        while (labelOtherPlayer.getText().equals("El otro jugador esta configurando...")) {
            wait();
        }

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
    public void threadListens(){
        new Thread(() -> {
            String message = "";
            try{
                message = client.receiveMessageString();
                System.out.println("Mensaje del otro cliente" + message);
                Platform.runLater(() -> {
                    labelOtherPlayer.setText("El otro jugador ha terminado de configurar sus barcos.");
                });
                notify();
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

    }



}
