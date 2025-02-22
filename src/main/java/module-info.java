module org.example.hundirlaflota2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.example.hundirlaflota2 to javafx.fxml;
    opens org.example.hundirlaflota2.Windows to javafx.graphics; // ✅ SOLUCIÓN
    opens org.example.hundirlaflota2.Controllers to javafx.fxml;

    exports org.example.hundirlaflota2;
    exports org.example.hundirlaflota2.Windows;
    exports org.example.hundirlaflota2.Controllers;
    exports org.example.hundirlaflota2.ServidorCliente;
    exports org.example.hundirlaflota2.Communication;
}
