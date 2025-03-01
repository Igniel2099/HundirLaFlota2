package org.example.hundirlaflota2.Controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.Objects;

public class VictoryController extends FatherController {

    private boolean ganaste = true;
    private String name;
    @FXML
    private ImageView backgroundImageView;

    @FXML
    private StackPane fatherPane;

    @FXML
    private Label labelWinner;

    @FXML
    private ImageView imageWinner;

    public void setGanaste(boolean ganaste) {
        this.ganaste = ganaste;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void changeImageWinner(){
        if (!ganaste){
            imageWinner.setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(
                                    "/org/example/hundirlaflota2/Images/perdedor.png")
                    ).toExternalForm())
            );
        }
    }

    private void changeLabelWinner(){

        String text = ganaste ? "Eres un Ganador" : "Eres un perdedor";
        text += name;
        labelWinner.setText(text);

    }

    public void changeElementWindow(){
        changeImageWinner();
        changeLabelWinner();
    }

    @FXML
    public void initialize() {
        backgroundImageView.fitWidthProperty().bind(fatherPane.widthProperty());
        backgroundImageView.fitHeightProperty().bind(fatherPane.heightProperty());
        backgroundImageView.setPreserveRatio(false);
    }
}
