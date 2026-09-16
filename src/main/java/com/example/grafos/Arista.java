package com.example.grafos;

public class Arista {
    private final Nodo origen;                 // Nodo de salida
    private final Nodo destino;                // Nodo de llegada

    public Arista(Nodo origen, Nodo destino) {
        this.origen = origen;                  // Guarda origen
        this.destino = destino;                // Guarda destino
    }

    public Nodo getOrigen() { return origen; }   // Devuelve origen
    public Nodo getDestino() { return destino; } // Devuelve destino

    public double getPeso() {                  // Peso = distancia actual
        return origen.distanciaA(destino);     // Se recalcula al mover nodos
    }
}