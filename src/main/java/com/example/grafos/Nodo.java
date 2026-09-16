package com.example.grafos;

public class Nodo {
    private final String id;                   // Identificador del nodo
    private double x, y;

    private double g, h, f;
    private Nodo padre;                        // Nodo anterior en la ruta

    public Nodo(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public double getG() { return g; }
    public void setG(double g) { this.g = g; }
    public double getH() { return h; }
    public void setH(double h) { this.h = h; }
    public double getF() { return f; }
    public void setF(double f) { this.f = f; }
    public Nodo getPadre() { return padre; }   // Devuelve padre
    public void setPadre(Nodo padre) { this.padre = padre; } // Cambia padre

    public double distanciaA(Nodo otro) {      // Distancia a otro nodo
        double dx = this.x - otro.x;           // Diferencia X
        double dy = this.y - otro.y;           // Diferencia Y
        return Math.sqrt(dx * dx + dy * dy);   // Pitágoras
    }

    @Override
    public String toString() { return id; }    // Al imprimir muestra id
}