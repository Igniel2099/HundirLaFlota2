package org.example.hundirlaflota2.Windows;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.example.hundirlaflota2.Controllers.VictoryController;

public class VictoryWindow extends FatherWindow{
    private final boolean gano;
    private final String name;
    @Override
    public void getController(FXMLLoader loader, Stage stage) {
        VictoryController victoryController = loader.getController();
        victoryController.setStage(stage);
        victoryController.setGanaste(gano);
        victoryController.setName(name);
        victoryController.changeElementWindow();
    }

    public VictoryWindow(boolean ganaste, String nameClient){
        gano = ganaste;
        name = nameClient;
        pathView ="/org/example/hundirlaflota2/Views/victoryView.fxml";
    }
}
