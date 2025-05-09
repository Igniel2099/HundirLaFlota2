package org.example.hundirlaflota2.Controllers;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.hundirlaflota2.MainApp;
import org.example.hundirlaflota2.ServidorCliente.Cliente;
import org.example.hundirlaflota2.Windows.MainWindow;

public class UploadController extends FatherController {

    public Cliente client;

    public void setClient(Cliente client) {
        this.client = client;
    }

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
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }

    public void changeToMainWindow(){
        MainApp mainApp = new MainApp();
        mainApp.setFatherWindow(new MainWindow(client));
        try{

            mainApp.start(getStage());

        }catch(Exception e){
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }

    public void initSecondThread(){
        clientStart();
        Platform.runLater(this::changeToMainWindow);
    }

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private StackPane fatherPane;

    @FXML
    public void initialize() {
        // Hacer que la imagen de detrás sea escalable
        backgroundImageView.fitWidthProperty().bind(fatherPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(fatherPane.heightProperty());
        backgroundImageView.setPreserveRatio(false);


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
                Thread.sleep(2000); // Tengo que cambiarlo a 3000 ms ósea 3 segundos
                initSecondThread();
            } catch (InterruptedException e) {
                System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
                for (StackTraceElement element : e.getStackTrace()) {
                    System.err.println("\tat " + element);
                }
            }
        }).start();

    }

}
