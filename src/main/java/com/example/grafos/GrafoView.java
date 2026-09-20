package com.example.grafos;

import com.example.grafos.Arista;
import com.example.grafos.Grafo;
import com.example.grafos.Nodo;
import javafx.animation.PathTransition;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
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

    private static final double RADIO = 20; //tamaño de los nodos
    private final Grafo grafo; //modelo del grafo

    public enum Modo { NODO, ARISTA, INICIO, FIN, MOVER } //declaracion de las cinco opciones

    private Modo modoActual = null;   // ningún modo activo al inicio
    //esta es la parte de los dos nodos a elegir y cual se conecto con cual
    private Nodo nodoPendiente = null;
    private Nodo nodoInicio = null;
    private Nodo nodoFin = null;

    private final Map<Nodo, Circle> circulos = new HashMap<>();
    private final Map<Nodo, Text> etiquetas = new HashMap<>();
    private final Map<Arista, Line> lineas = new HashMap<>();

    private final HBox barraBotones;    // Arista línea

    //nodos a agregar
    public GrafoView(Grafo grafo, double ancho, double alto) {
        this.grafo = grafo;
        //tamaño del panel
        setPrefSize(ancho, alto);
        setMinSize(ancho, alto);
        setStyle("-fx-background-color: #3b1f5e;");
        //barra de botones con tamaño
        barraBotones = new HBox(10);
        barraBotones.setPadding(new Insets(10));
        ToggleGroup grupo = new ToggleGroup(); //el toggle solo permite un boton seleccionado a la vez
        ToggleButton btnNodo = crearBotonModo("Nodo", Modo.NODO, grupo);
        ToggleButton btnArista = crearBotonModo("Arista", Modo.ARISTA, grupo);
        ToggleButton btnInicio = crearBotonModo("Inicio", Modo.INICIO, grupo);
        ToggleButton btnFin = crearBotonModo("Fin", Modo.FIN, grupo);
        ToggleButton btnMover = crearBotonModo("Mover", Modo.MOVER, grupo);
        barraBotones.getChildren().addAll(btnNodo, btnArista, btnInicio, btnFin, btnMover);
        getChildren().add(barraBotones);
        // cuando se da el click creara el nodo si es su caso
        setOnMouseClicked(e -> { //al hacer click crea nodo
            if (modoActual == null || modoActual != Modo.NODO) return; //si no es su opcion sale
            String id = String.valueOf((char) ('A' + grafo.getNodos().size())); //les da su nombre
            //hace la creacion de los grafos
            Nodo nuevo = new Nodo(id, e.getX(), e.getY());
            grafo.agregarNodo(nuevo);
            dibujarNodo(nuevo);
        });
    }

    private ToggleButton crearBotonModo(String texto, Modo modo, ToggleGroup grupo) {
        ToggleButton btn = new ToggleButton(texto); //crea los botones
        btn.setToggleGroup(grupo);
        btn.setOnAction(e -> { //revisa a que boton fue el click
            modoActual = modo;
            limpiarSeleccion();
            nodoPendiente = null;
        });
        return btn;
    }

    public void dibujarNodo(Nodo n) {
        //creal el nodo de color azul
        Circle c = new Circle(RADIO, Color.web("#0d1b3d")); //estandar universal
        c.setCenterX(n.getX());
        c.setCenterY(n.getY());
        //pone el nombre del nodo
        Text t = new Text(n.getId());
        t.setFill(Color.WHITE);
        t.setX(n.getX() - 5);
        t.setY(n.getY() + 4);
        //guarda y lo coloca
        circulos.put(n, c);
        etiquetas.put(n, t);
        //que click se hace
        c.setOnMouseClicked(e -> {
            if (modoActual == null) {//verifica que exista alguna opcion
                e.consume();
                return;
            }
            switch (modoActual) {
                case NODO:
                case MOVER:
                    break;
                case ARISTA:
                    //busca si se selecciono un nodo
                    if (nodoPendiente == null) { //pinta el borde
                        nodoPendiente = n;
                        c.setStroke(Color.YELLOW);
                        c.setStrokeWidth(3); //borde mas ancho
                    } else if (nodoPendiente == n) { //es el mismo
                        limpiarSeleccion();
                    } else {
                        grafo.agregarArista(nodoPendiente, n); //busca si se seleccina otro nodo y crea la arista
                        Arista a = grafo.getAristas().get(grafo.getAristas().size() - 1);
                        dibujarArista(a);
                        limpiarSeleccion();
                    }
                    break;
                case INICIO:
                    //si se selecciona un nodo lo pinta
                    if (nodoInicio != null) {
                        circulos.get(nodoInicio).setFill(Color.web("##0d1b3d"));
                    }
                    nodoInicio = n; //guarda que este nodo sera el inicial
                    c.setFill(Color.ORANGE);//cambia el color de antes por el nuevo que es naranja
                    break;
                case FIN:
                    //mismo caso que inicio
                    if (nodoFin != null) {
                        circulos.get(nodoFin).setFill(Color.web("#0d1b3d"));
                    }
                    nodoFin = n;
                    c.setFill(Color.web("#e74c3c"));
                    calcularYAnimarRuta();
                    break;
            }
            e.consume(); //para no hacer nada si no hay una opcion seleccionada
        });
        c.setOnMouseDragged(e -> {
            if (modoActual != Modo.MOVER) return;
            //el cirulo ira a donde se mueva el mouse
            c.setCenterX(e.getX());
            c.setCenterY(e.getY());
            t.setX(e.getX() - 5); //texto dentro del circulo
            t.setY(e.getY() + 4);
            //actualiza el modelo para recalcular la ruta
            n.setX(e.getX());
            n.setY(e.getY());
            //sigue el camino calculado
            for (Map.Entry<Arista, Line> entry : lineas.entrySet()) {
                Arista a = entry.getKey();
                if (a.getOrigen() == n || a.getDestino() == n) {
                    Line l = entry.getValue();
                    //actualiza las coordenadas
                    l.setStartX(a.getOrigen().getX());
                    l.setStartY(a.getOrigen().getY());
                    l.setEndX(a.getDestino().getX());
                    l.setEndY(a.getDestino().getY());
                }
            }
            e.consume();
        });
        c.setOnMouseReleased(e -> {
            if (nodoInicio != null && nodoFin != null) { //verifica si existe nuevo inicio y fin
                calcularYAnimarRuta();
            }
        });
        getChildren().addAll(c, t);
        barraBotones.toFront();
    }

    private void dibujarArista(Arista a) {
        Line linea = new Line(); //crea la linea
        linea.setStroke(Color.GRAY); //pinta la linea
        linea.setStrokeWidth(2); //grosor
        linea.setStartX(a.getOrigen().getX());
        linea.setStartY(a.getOrigen().getY());
        linea.setEndX(a.getDestino().getX());
        linea.setEndY(a.getDestino().getY());
        lineas.put(a, linea); // guarda en donde esta
        getChildren().add(0, linea); //los coloca en los nodos
        barraBotones.toFront();
    }

    private void limpiarSeleccion() {
        for (Circle circulo : circulos.values()) { //recorre punto por punto
            circulo.setStroke(Color.BLACK);
            circulo.setStrokeWidth(1);
        }
        nodoPendiente = null;
    }
    private void resetearLineas() {
        for (Line l : lineas.values()) { //recorre todas las lineas
            l.setStroke(Color.GRAY);
            l.setStrokeWidth(2);
        }
    }
    private void calcularYAnimarRuta() {
        resetearLineas(); //pinta todas las lineas
        if (nodoInicio == null || nodoFin == null) return; //si no hay sale

        List<Nodo> camino = Algoritmo.buscarRuta(grafo, nodoInicio, nodoFin); //busca que camino va a tomar
        if (camino.size() < 2) { //si no tiene minimo 2 no recorre
            System.out.println("No hay ruta entre " + nodoInicio.getId() + " y " + nodoFin.getId());
            return;
        }

        //pinta de verde las aristas de la ruta
        for (int i = 0; i < camino.size() - 1; i++) {
            Nodo a = camino.get(i); //nodo actual
            Nodo b = camino.get(i + 1); //nodo siguiente
            for (Arista ar : grafo.getAristas()) { //recorrido
                if ((ar.getOrigen() == a && ar.getDestino() == b) || (ar.getOrigen() == b && ar.getDestino() == a)) { //verifica que esten conectadas
                    lineas.get(ar).setStroke(Color.LIMEGREEN);
                    lineas.get(ar).setStrokeWidth(4);
                    break;
                }
            }
        }

        //crea el camino para la animacion
        Path path = new Path();
        path.getElements().add(new MoveTo(camino.get(0).getX(), camino.get(0).getY())); //busca el nodo inicial
        for (int i = 1; i < camino.size(); i++) { //empieza a recorrer el camino
            path.getElements().add(new LineTo(camino.get(i).getX(), camino.get(i).getY()));
        }

        //punto amarillo que viaja por la ruta
        Circle punto = new Circle(8, Color.YELLOW);
        getChildren().add(punto);
        //crea la animacion
        PathTransition anim = new PathTransition(Duration.seconds(2), path, punto);
        anim.setOnFinished(e -> getChildren().remove(punto)); //cuando termina se elimina el circulo amarillo
        anim.play(); //empieza la animacion
    }
}