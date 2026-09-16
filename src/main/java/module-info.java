module com.example.grafos {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.grafos to javafx.fxml;
    exports com.example.grafos;
}