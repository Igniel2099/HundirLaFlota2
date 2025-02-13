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
            e.printStackTrace();
        }
    }

    public void clientStart(){
        try {
            client.connectionToServer();
            String mensajeDelServidor = client.receiveMessageString();
            System.out.println("Este es el mensaje del servidor: " + mensajeDelServidor);
            client.sendMessageString("Soy client: " + client.getNombreCliente());
            System.out.println("Mensaje recibido del otro cliente" + client.receiveMessageString());
        }catch(Exception e){
            System.out.println("Error en el clienteStart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        new Thread(this::clientStart).start();
    }

}
