package org.example.hundirlaflota2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Windows.*;


public class MainApp extends Application {

    public FatherWindow fatherWindow = new LoginWindow();

    public FatherWindow getFatherWindow() {
        return fatherWindow;
    }

    public void setFatherWindow(FatherWindow fatherWindow) {
        this.fatherWindow = fatherWindow;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = fatherWindow.fxmlLoader(stage);

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
