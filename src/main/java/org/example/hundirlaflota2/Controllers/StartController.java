package org.example.hundirlaflota2.Controllers;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

import java.io.IOException;

public class StartController extends FatherController{

    private Cliente client;
    private Communication communicationSw;
    private BooleanProperty activatedButton;

    public boolean isActivatedButton() {
        return activatedButton.get();
    }

    @FXML
    private Button bulletButton;

    public void setCommunicationSw(Communication communicationSw) {
        this.communicationSw = communicationSw;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    public void initCommunication(){
        try {
            activatedButton = new SimpleBooleanProperty(
                communicationSw.startCommunication()
            );

            bulletButton.disableProperty().bind(activatedButton.not());

            System.out.println("Turno " + activatedButton);


            bulletButton.setText(
                    activatedButton.get()
                            ? "Presiona"
                            : "Esperando..."
            );

            if (!activatedButton.get()) {
                iniciarEscucha();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void turno() throws Exception{

        if(activatedButton.get()){
            // El que ataca
            // -x-y-
            // disparo provisional
            client.sendMessageString("1,2");
            int queToque = client.receiveMessageInt();
            System.out.println("Lo que toque fue: " + queToque);

            activatedButton.set(
                client.receiveMessageString().equals("Atacas")
            );

            bulletButton.setText(
                    activatedButton.get()
                            ? "Presiona"
                            : "Esperando..."
            );

            iniciarEscucha();
        }
    }

    @FXML
    public void handleButtonClick(ActionEvent event){
        System.out.println("Click del disparo " + bulletButton.getText());

        try{
            turno();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void iniciarEscucha() {

        Thread hiloEscuchando = new Thread(() -> {
            try{
                String recibioDisparo = client.receiveMessageString();

                System.out.println("El disparo es: " + recibioDisparo);
                // Mensaje del que toco provisional
                client.sendMessageInt(2);

                Platform.runLater(() -> {
                    try{

                        activatedButton.set(
                            client.receiveMessageString().equals("Atacas")
                        );
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }

                    bulletButton.setText(
                            activatedButton.get()
                                    ? "Presiona"
                                    : "Esperando..."
                    );

                });

            }catch (IOException ex){
                ex.printStackTrace();
            }

        });

        hiloEscuchando.start();
    }

    @FXML
    public void initialize(){
        // Nada de Momento
    }
}
