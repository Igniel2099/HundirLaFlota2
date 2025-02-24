package org.example.hundirlaflota2.Controllers;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.ServidorCliente.Cliente;
import org.example.hundirlaflota2.Windows.MainWindow;

public class UploadController extends FatherController {

    public Cliente client = new Cliente("Cliente1");

    @FXML
    private ImageView imageViewRotate;

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

    public void changeToMainWindow(){
        MainApp mainApp = new MainApp();
        mainApp.setFatherWindow(new MainWindow(client));
        try{

            mainApp.start(getStage());

        }catch(Exception e){
            System.out.println("Error en el UploadController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initSecondThread(){
        clientStart();
        Platform.runLater(this::changeToMainWindow);
    }

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline();

        // Crear un KeyFrame que haga girar la imagen
        KeyFrame keyFrame = new KeyFrame(Duration.millis(50), e -> {
            // Incrementar la rotación en 1 grado cada vez
            imageViewRotate.setRotate(imageViewRotate.getRotate() + 15);
        });

        // Añadir el KeyFrame al Timeline
        timeline.getKeyFrames().add(keyFrame);

        // Configurar el Timeline para que se ejecute indefinidamente
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true); // Hace que la animación se invierta automáticamente

        // Iniciar la animación
        timeline.play();

        new Thread(() -> {
            try {
                Thread.sleep(10); // Tengo que cambiarlo a 3000 ms osea 3 segundos
                initSecondThread();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
