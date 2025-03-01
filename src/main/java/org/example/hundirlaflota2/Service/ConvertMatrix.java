package org.example.hundirlaflota2.Service;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;

public class ConvertMatrix {



    public void mergeCells(GridPane gridPane, List<Integer[]> coords) {

        for (Integer[] coord : coords) {
            System.out.println(Arrays.toString(coord));
        }
        // Obtener la primera y última coordenada
        Integer[] first = coords.getFirst();
        Integer[] last = coords.getLast();

        int startCol = first[1]; // Columna inicial
        int startRow = first[0]; // Fila inicial
        int endCol = last[1]; // Última columna
        int endRow = last[0]; // Última fila

        // Crear un Pane unificado
        Pane mergedPane = new Pane();
        mergedPane.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
        mergedPane.setPrefSize((endCol - startCol + 1) * 37.6, (endRow - startRow + 1) * 36.8);

        // Remover las celdas individuales
        coords.forEach(coord -> {
            gridPane.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) == coord[0] && GridPane.getColumnIndex(node) == coord[1]
            );
        });

        // Agregar el Pane grande en la posición inicial
        gridPane.add(mergedPane, startCol, startRow);

        // Aplicar rowSpan o colSpan
        if (startRow == endRow) {
            GridPane.setColumnSpan(mergedPane, endCol - startCol + 1);
        } else if (startCol == endCol) {
            GridPane.setRowSpan(mergedPane, endRow - startRow + 1);
        }

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
