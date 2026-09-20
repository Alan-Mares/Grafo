package com.example.grafos;

public class Nodo {
    private final String id;
    private double x, y;
    private double g, h, f;
    private Nodo padre;
    //creacion de los nodos
    public Nodo(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    //nodo y su posicion
    public String getId() { return id; }
    public double getX() { return x; }
    public double getY() { return y; }
    //cambia la posicion
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public double getG() { return g; }
    public void setG(double g) { this.g = g; }
    public double getH() { return h; }
    public void setH(double h) { this.h = h; }
    public double getF() { return f; }
    public void setF(double f) { this.f = f; }
    public Nodo getPadre() { return padre; } //devuelve padre
    public void setPadre(Nodo padre) { this.padre = padre; } //cambia padre

    public double distanciaA(Nodo otro) { //distancia a otro nodo
        double dx = this.x - otro.x; //diferencia X
        double dy = this.y - otro.y; //diferencia Y
        return Math.sqrt(dx * dx + dy * dy); //pitágoras
    }

    @Override
    public String toString() { return id; } //id del nodo
}