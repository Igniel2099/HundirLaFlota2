package org.example.hundirlaflota2.Controllers;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

import java.io.IOException;
import java.util.Objects;

public class StartController extends FatherController{

    private Cliente client;
    private Communication communicationSw;
    private BooleanProperty activatedButton;

    @FXML
    private ImageView bulletButton;

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

            System.out.println("Turno " + activatedButton);

            bulletButton.setImage(
                    activatedButton.get()
                    ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/buttonBang.png")).toExternalForm())
                    : new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/button.png")).toExternalForm())
            );

            if (!activatedButton.get()) {
                iniciarEscucha();
            }
        }catch (Exception e) {
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
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

            bulletButton.setImage(
                    activatedButton.get()
                            ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/buttonBang.png")).toExternalForm())
                            : new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/button.png")).toExternalForm())
            );

            iniciarEscucha();
        }
    }

    @FXML
    public void pressedGang(MouseEvent event){
        ImageView imageView = (ImageView) event.getSource();
        System.out.println("Click del disparo " + imageView.getImage().getUrl());
        if (activatedButton.get()) {
            try{
                turno();
            }catch(Exception e){
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }
        }else{
            System.out.println("Turno " + activatedButton + " por lo que no puedes");
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
                    } catch (IOException e){
                        System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                        for (StackTraceElement element : e.getStackTrace()) {
                            System.err.println("\tat " + element);
                        }
                    }

                    bulletButton.setImage(
                            activatedButton.get()
                                    ? new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/buttonBang.png")).toExternalForm())
                                    : new Image(Objects.requireNonNull(getClass().getResource("/org/example/hundirlaflota2/Images/button.png")).toExternalForm())
                    );

                });

            }catch (IOException e){
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }

        });

        hiloEscuchando.start();
    }

    @FXML
    public void initialize(){
        // Nada de Momento
    }
}
