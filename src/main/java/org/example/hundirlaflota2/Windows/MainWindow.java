package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.MainController;
import org.example.hundirlaflota2.Service.Communication;
import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class MainWindow extends FatherWindow {

    public Cliente cliente;
    private final Communication communicationMw;
    
    @Override
    public void getController(FXMLLoader loader, Stage stage) {

        MainController mainController = loader.getController();
        mainController.setStage(stage);

        mainController.setClient(cliente);

        mainController.setClient(cliente);

        mainController.setCommunicationMw(communicationMw);
        
        
        mainController.sendMessageClint();
        try {
            System.out.println(mainController.getClient().receiveMessageString());
        }catch (Exception e){
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }

        mainController.threadListens(); // hilo que siempre escucha

    }

    public MainWindow(Cliente client) {
        cliente = client;
        pathView = "/org/example/hundirlaflota2/Views/mainWindow.fxml";
        communicationMw = new Communication(client);
    }

}
