package com.example.grafos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Grafo grafo = new Grafo();
        GrafoView vista = new GrafoView(grafo, 800, 500);
        Scene scene = new Scene(vista, 800, 500);

        stage.setTitle("Grafos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}