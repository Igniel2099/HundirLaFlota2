package org.example.hundirlaflota2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class StartController extends FatherController{

    private Cliente client;

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
        }else {
            // El que espera
            client.receiveMessageString();
            // Mensaje del que toco provisional
            client.sendMessageInt(2);
        }
    }



    @FXML
    public void handleButtonClick(ActionEvent event){
        System.out.println("Click del disparo");

    }
}
