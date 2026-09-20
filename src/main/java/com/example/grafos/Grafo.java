package com.example.grafos;

import java.util.*;

public class Grafo {
    private final List<Nodo> nodos = new ArrayList<>(); //declara la lista de nodos
    private final List<Arista> aristas = new ArrayList<>(); //declara la lista de aristas

    public void agregarNodo(Nodo n) { nodos.add(n); } //añade nodo a la lista
    public void agregarArista(Nodo a, Nodo b) { aristas.add(new Arista(a, b)); } //añade y crea la arista a la lista

    public List<Nodo> getNodos() { return nodos; }
    public List<Arista> getAristas() { return aristas; }
    //crea el mapa de los nodos que conecto
    public Map<Nodo, Double> getVecinos(Nodo n) {
        Map<Nodo, Double> vecinos = new HashMap<>();
        for (Arista a : aristas) {
            if (a.getOrigen() == n) vecinos.put(a.getDestino(), a.getPeso()); //agrega los nodos destino
            else if (a.getDestino() == n) vecinos.put(a.getOrigen(), a.getPeso()); //agrega los nodos origen
        }
        return vecinos;
    }
}