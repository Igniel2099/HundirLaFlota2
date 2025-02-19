package org.example.hundirlaflota2.Controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class StartController extends FatherController{

    private Cliente client;
    private BooleanProperty activatedButton;

    @FXML
    private Button bulletButton;

    public Button getBulletButton() {
        return bulletButton;
    }

    public void setBulletButton(Button bulletButton) {
        this.bulletButton = bulletButton;
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
    public void sendMessageClint(){
        try{
            client.sendMessageString("He Cambiado de pantalla soy " + client.getNombreCliente() + "Estoy en StartWindow");

        }catch(Exception e){
            System.out.println("--Error en el sendMessageClient de MainController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void turno() throws Exception{

        boolean atacante = client.receiveMessageString().equals("Atacas");
        if(atacante){
            // El que ataca
            // -x-y-
            // disparo provisional
            client.sendMessageString("1,2");
            int queToque = client.receiveMessageInt();
            System.out.println("Lo que toque fue: " + queToque);
        }else {
            // El que espera
            String recibioDisparo = client.receiveMessageString();

            System.out.println("El disparo es: " + recibioDisparo);
            // Mensaje del que toco provisional
            client.sendMessageInt(2);

            setActivatedButton(
                    new SimpleBooleanProperty(true)
            );

            getBulletButton().setText(
                getActivatedButton().get()
                    ? "Presiona"
                    : "Esperando..."
            );
        }
    }

    @FXML
    public void handleButtonClick(ActionEvent event){
        System.out.println("Click del disparo" + bulletButton.getText());

        try{
            turno();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
