package com.example.grafos;

public class Arista {
    private final Nodo origen; //nodo de partida
    private final Nodo destino; //nodo de llegada

    public Arista(Nodo origen, Nodo destino) {
        this.origen = origen; // Guarda origen
        this.destino = destino; // Guarda destino
    }

    public Nodo getOrigen() { return origen; } //devuelve origen
    public Nodo getDestino() { return destino; } //devuelve destino

    public double getPeso() {                  // Peso = distancia actual
        return origen.distanciaA(destino); //se recalcula al mover nodos
    }
}