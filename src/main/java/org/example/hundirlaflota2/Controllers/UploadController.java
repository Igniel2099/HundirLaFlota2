package org.example.hundirlaflota2.Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.ServidorCliente.Cliente;
import org.example.hundirlaflota2.Windows.MainWindow;

public class UploadController extends FatherController {

    public Cliente client = new Cliente("Cliente1");


    @FXML
    public void handleButtonClick(ActionEvent event) {
        MainApp mainApp = new MainApp();
        mainApp.setFatherWindow(new MainWindow(client));
        try{

            mainApp.start(getStage());

        }catch(Exception e){
            System.out.println("Error en el UploadController: " + e.getMessage());
        }
    }

    public void clientStart(){
        try {
            client.connectionToServer();
            String mensajeDelOtroCliente = client.receiveMessage();
            System.out.println("Este es el mensaje del otro client que me envia el servidor: " + mensajeDelOtroCliente);
            client.sendMessage("Soy client: " + client.getNombreCliente());
        }catch(Exception e){
            System.out.println("Error en el clienteStart: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        clientStart();
    }

}
