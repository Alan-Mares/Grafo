package com.example.grafos;

import java.util.*;

public class Grafo {
    private final List<Nodo> nodos = new ArrayList<>();    // Lista de nodos
    private final List<Arista> aristas = new ArrayList<>(); // Lista de aristas

    public void agregarNodo(Nodo n) { nodos.add(n); }       // Añade nodo
    public void agregarArista(Nodo a, Nodo b) { aristas.add(new Arista(a, b)); } // Añade arista

    public List<Nodo> getNodos() { return nodos; }
    public List<Arista> getAristas() { return aristas; }

    public Map<Nodo, Double> getVecinos(Nodo n) {
        Map<Nodo, Double> vecinos = new HashMap<>();
        for (Arista a : aristas) {
            if (a.getOrigen() == n) vecinos.put(a.getDestino(), a.getPeso());
            else if (a.getDestino() == n) vecinos.put(a.getOrigen(), a.getPeso());
        }
        return vecinos;
    }
}