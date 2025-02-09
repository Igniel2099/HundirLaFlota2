package org.example.hundirlaflota2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class StartController extends FatherController{

    public final Cliente client;

    public StartController(Cliente client) {
        this.client = client;
    }

    @FXML
    public void handleButtonClick(ActionEvent event){
        System.out.println("Click del disparo");
    }
}
