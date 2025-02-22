package org.example.hundirlaflota2.Controllers;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.hundirlaflota2.Communication.CommunicationStartWindow;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

import java.io.IOException;

public class StartController extends FatherController{

    private Cliente client;
    private CommunicationStartWindow communicationSw;
    private BooleanProperty activatedButton;

    @FXML
    private Button bulletButton;

    public void setCommunicationSw(CommunicationStartWindow communicationSw) {
        this.communicationSw = communicationSw;
    }

    public Button getBulletButton() {
        return bulletButton;
    }

    public BooleanProperty getActivatedButton() {
        return activatedButton;
    }

    public void setActivatedButton(BooleanProperty activatedButton) {
        this.activatedButton = activatedButton;
    }

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    public void initCommunication(){
        try {
            setActivatedButton(
                    new SimpleBooleanProperty(
                            communicationSw.startCommunication()
                    )
            );

            getBulletButton().disableProperty().bind(getActivatedButton().not());

            System.out.println("Turno " + getActivatedButton());


            getBulletButton().setText(
                    getActivatedButton().get()
                            ? "Presiona"
                            : "Esperando..."
            );

            if (!getActivatedButton().get()) {
                iniciarEscucha();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void turno() throws Exception{

        if(getActivatedButton().get()){
            // El que ataca
            // -x-y-
            // disparo provisional
            client.sendMessageString("1,2");
            int queToque = client.receiveMessageInt();
            System.out.println("Lo que toque fue: " + queToque);

            getActivatedButton().set(
                client.receiveMessageString().equals("Atacas")
            );

            getBulletButton().setText(
                    getActivatedButton().get()
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

                        getActivatedButton().set(
                            client.receiveMessageString().equals("Atacas")
                        );
                    } catch (IOException ex){
                        ex.printStackTrace();
                    }

                    getBulletButton().setText(
                            getActivatedButton().get()
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
