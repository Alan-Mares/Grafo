package com.example.grafos;

import javafx.animation.PathTransition;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafoView extends Pane {

    private static final double RADIO = 20;
    private final Grafo grafo;
    private final int cantidadNodos;

    private Nodo nodoPendiente = null;        // Para crear aristas con clic izquierdo
    private Nodo nodoInicio = null;           // Origen de la ruta
    private Nodo nodoFin = null;              // Destino de la ruta

    private final Map<Nodo, Circle> circulos = new HashMap<>();   // Nodo  círculo
    private final Map<Nodo, Text> etiquetas = new HashMap<>();    // Nodo texto
    private final Map<Arista, Line> lineas = new HashMap<>();     // Arista línea

    public GrafoView(Grafo grafo, double ancho, double alto, int cantidadNodos) {
        this.grafo = grafo;
        this.cantidadNodos = cantidadNodos;
        setPrefSize(ancho, alto);
        setMinSize(ancho, alto);
        setStyle("-fx-background-color: #1e1e2e;");

        setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;
            if (grafo.getNodos().size() >= cantidadNodos) return;

            String id = String.valueOf((char) ('A' + grafo.getNodos().size()));
            Nodo nuevo = new Nodo(id, e.getX(), e.getY());
            grafo.agregarNodo(nuevo);
            dibujarNodo(nuevo);
        });
    }

    private void dibujarNodo(Nodo n) {
        Circle c = new Circle(RADIO, Color.web("#4a90d9"));
        c.setCenterX(n.getX());
        c.setCenterY(n.getY());

        Text t = new Text(n.getId());
        t.setFill(Color.WHITE);
        t.setX(n.getX() - 5);
        t.setY(n.getY() + 4);

        circulos.put(n, c);
        etiquetas.put(n, t);

        // Crear aristas
        c.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;

            if (nodoPendiente == null) {
                nodoPendiente = n;
                c.setStroke(Color.YELLOW);
                c.setStrokeWidth(3);
            } else if (nodoPendiente == n) {
                limpiarSeleccion();
            } else {
                grafo.agregarArista(nodoPendiente, n);
                Arista a = grafo.getAristas().get(grafo.getAristas().size() - 1);
                dibujarArista(a);
                limpiarSeleccion();
            }
            e.consume();
        });

        // Marcar inicio y fin
        c.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                if (nodoInicio == null) {
                    nodoInicio = n;
                    c.setFill(Color.ORANGE);
                } else if (nodoFin == null && n != nodoInicio) {
                    nodoFin = n;
                    c.setFill(Color.CRIMSON);
                    calcularYAnimarRuta();
                } else {
                    nodoInicio = null;
                    nodoFin = null;
                    repintarNodos();
                    resetearLineas();
                }
                e.consume();
            }
        });

        // Mover el nodo
        c.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY) return;

            // Mover el círculo y el texto
            c.setCenterX(e.getX());
            c.setCenterY(e.getY());
            t.setX(e.getX() - 5);
            t.setY(e.getY() + 4);

            // Actualizar el modelo
            n.setX(e.getX());
            n.setY(e.getY());

            // Actualizar las líneas que tocan este nodo
            for (Map.Entry<Arista, Line> entry : lineas.entrySet()) {
                Arista a = entry.getKey();
                if (a.getOrigen() == n || a.getDestino() == n) {
                    Line l = entry.getValue();
                    l.setStartX(a.getOrigen().getX());
                    l.setStartY(a.getOrigen().getY());
                    l.setEndX(a.getDestino().getX());
                    l.setEndY(a.getDestino().getY());
                }
            }
            e.consume();
        });

        // Recalcular ruta
        c.setOnMouseReleased(e -> {
            if (nodoInicio != null && nodoFin != null) {
                calcularYAnimarRuta();
            }
        });

        getChildren().addAll(c, t);
    }

    private void dibujarArista(Arista a) {
        Line linea = new Line();
        linea.setStroke(Color.GRAY);
        linea.setStrokeWidth(2);
        linea.setStartX(a.getOrigen().getX());
        linea.setStartY(a.getOrigen().getY());
        linea.setEndX(a.getDestino().getX());
        linea.setEndY(a.getDestino().getY());
        lineas.put(a, linea);
        getChildren().add(0, linea);
    }

    private void limpiarSeleccion() {
        for (Circle circulo : circulos.values()) {
            circulo.setStroke(Color.BLACK);
            circulo.setStrokeWidth(1);
        }
        nodoPendiente = null;
    }

    private void repintarNodos() {
        for (Map.Entry<Nodo, Circle> entry : circulos.entrySet()) {
            Nodo n = entry.getKey();
            Circle c = entry.getValue();
            if (n == nodoInicio) c.setFill(Color.ORANGE);
            else if (n == nodoFin) c.setFill(Color.CRIMSON);
            else c.setFill(Color.web("#4a90d9"));
        }
    }

    private void resetearLineas() {
        for (Line l : lineas.values()) {
            l.setStroke(Color.GRAY);
            l.setStrokeWidth(2);
        }
    }

    private void calcularYAnimarRuta() {
        resetearLineas();
        if (nodoInicio == null || nodoFin == null) return;

        List<Nodo> camino = Algoritmo.buscarRuta(grafo, nodoInicio, nodoFin);
        if (camino.size() < 2) {
            System.out.println("No hay ruta entre " + nodoInicio.getId() + " y " + nodoFin.getId());
            return;
        }

        for (int i = 0; i < camino.size() - 1; i++) {
            Nodo a = camino.get(i);
            Nodo b = camino.get(i + 1);
            for (Arista ar : grafo.getAristas()) {
                if ((ar.getOrigen() == a && ar.getDestino() == b) ||
                        (ar.getOrigen() == b && ar.getDestino() == a)) {
                    lineas.get(ar).setStroke(Color.LIMEGREEN);
                    lineas.get(ar).setStrokeWidth(4);
                    break;
                }
            }
        }

        Path path = new Path();
        path.getElements().add(new MoveTo(camino.get(0).getX(), camino.get(0).getY()));
        for (int i = 1; i < camino.size(); i++) {
            path.getElements().add(new LineTo(camino.get(i).getX(), camino.get(i).getY()));
        }

        Circle punto = new Circle(8, Color.YELLOW);
        getChildren().add(punto);

        PathTransition anim = new PathTransition(Duration.seconds(2), path, punto);
        anim.setOnFinished(e -> getChildren().remove(punto));
        anim.play();
    }
}