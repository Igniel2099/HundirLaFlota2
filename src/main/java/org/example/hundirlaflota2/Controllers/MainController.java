package org.example.hundirlaflota2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.ServidorCliente.Cliente;
import org.example.hundirlaflota2.Windows.StartWindow;

public class MainController extends FatherController {

    private Cliente client;

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
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
     * Método para que client envie menasjes
     */
    public void sendMessageClint(){
        try{

            client.sendMessageString("He Cambiado de pantalla soy " + client.getNombreCliente() + "Estoy en WindowMain");

        }catch(Exception e){
            System.out.println("--Error en el sendMessageClient de MainController: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {

    }



}
