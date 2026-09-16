package com.example.grafos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        TextInputDialog dialogo = new TextInputDialog(); // Sin valor sugerido
        dialogo.setTitle("Configurar grafo");
        dialogo.setHeaderText("¿Cuántos nodos quieres en el grafo?");
        dialogo.setContentText("Número de nodos:");

        Optional<String> respuesta = dialogo.showAndWait(); // Espera al usuario

        int cantidadNodos = 6; //recomendado
        if (respuesta.isPresent()) {  // Si aceptó
            try {
                cantidadNodos = Integer.parseInt(respuesta.get().trim()); // Texto → número
                if (cantidadNodos < 2) cantidadNodos = 2; // Mínimo 2
            } catch (NumberFormatException ex) {
                cantidadNodos = 6;  // Si escribió letras, usa 6
            }
        }

        Grafo grafo = new Grafo();                   // Grafo vacío
        GrafoView vista = new GrafoView(grafo, 800, 500, cantidadNodos); // Vista con límite
        Scene scene = new Scene(vista, 800, 500);    // Vista como raíz

        stage.setTitle("Grafos con A* — Coloca " + cantidadNodos + " nodos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}