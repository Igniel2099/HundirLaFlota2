package org.example.hundirlaflota2.Service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.*;

public class ConvertMatrix {

    public Map<Integer,String> mapToImageShip(String orientation){
        if (orientation.equals("vertical")){
            return new HashMap<>(){{
                put(1,"/org/example/hundirlaflota2/Images/ship1Vertical.png");
                put(2,"/org/example/hundirlaflota2/Images/ship2Vertical.png");
                put(3,"/org/example/hundirlaflota2/Images/ship3Vertical.png");
                put(4,"/org/example/hundirlaflota2/Images/ship4Vertical.png");
            }};

        }
        if (orientation.equals("horizontal")){
            return new HashMap<>(){{
                put(1, "/org/example/hundirlaflota2/Images/ship1Horizontal.png");
                put(2,"/org/example/hundirlaflota2/Images/ship2Horizontal.png");
                put(3,"/org/example/hundirlaflota2/Images/ship3Horizontal.png");
                put(4,"/org/example/hundirlaflota2/Images/ship4Horizontal.png");
            }};
        }
        return null;
    }

    public String selectionShipImage(String orientation, Integer sizeShip){
        return mapToImageShip(orientation).get(sizeShip);
    }

    public void mergeCells(GridPane gridPane, List<Integer[]> coords) {

        System.out.println("Estos son las coordenadas que estoy buscando");
        for (Integer[] coord : coords) {
            System.out.println(Arrays.toString(coord));
        }
        // Obtener la primera y última coordenada
        Integer[] first = coords.getFirst();
        Integer[] last = coords.getLast();

        int startCol = first[1];
        int startRow = first[0];
        int endCol = last[1];
        int endRow = last[0];

        // Aplicar rowSpan o colSpan
        ImageView imageView = new ImageView();
        if (startRow == endRow) {
            imageView.setImage(
                new Image(
                    Objects.requireNonNull(getClass().getResource(
                        selectionShipImage("horizontal", endCol - startCol + 1)
                    )).toExternalForm()
                )
            );

            imageView.setFitHeight( 37.6);
            imageView.setFitWidth((endCol - startCol + 1) * 36.8);
            GridPane.setColumnSpan(imageView, endCol - startCol + 1);

        } else if (startCol == endCol) {
            imageView.setImage(
                new Image(
                    Objects.requireNonNull(getClass().getResource(
                        selectionShipImage("vertical", endRow - startRow + 1)
                    )).toExternalForm()
                )
            );

            imageView.setFitHeight((endRow - startRow + 1) * 37.6);
            imageView.setFitWidth( 36.8);
            GridPane.setRowSpan(imageView, endRow - startRow + 1);
        }
        gridPane.add(imageView, startCol, startRow);
    }

    public void buildGridPaneWithPaneWater(GridPane gridPane) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Pane pane = new Pane();
                pane.setPrefSize(37.6, 36.8); // Tamaño de cada celda

                // Agregar Pane al GridPane
                gridPane.add(pane, col, row);
            }
        }
    }

    public GridPane reBuildGridPane(List<List<Integer[]>> matrix, GridPane gridPane) {
        buildGridPaneWithPaneWater(gridPane);
        for (List<Integer[]> coords : matrix) {
            mergeCells(gridPane, coords);
        }
        return gridPane;
    }

}
