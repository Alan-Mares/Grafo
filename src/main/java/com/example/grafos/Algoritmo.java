package com.example.grafos;

import java.util.*;

public class Algoritmo {

    public static List<Nodo> buscarRuta(Grafo grafo, Nodo inicio, Nodo destino) {
        List<Nodo> abierta = new ArrayList<>();
        List<Nodo> cerrada = new ArrayList<>();
        for (Nodo n : grafo.getNodos()) {       // Reinicia valores
            n.setG(Double.POSITIVE_INFINITY);   // g = infinito
            n.setH(0);                          // h = 0
            n.setF(Double.POSITIVE_INFINITY);   // f = infinito
            n.setPadre(null);                   // Sin padre
        }

        inicio.setG(0);                         // Costo inicio = 0
        inicio.setH(inicio.distanciaA(destino));// Heurística al destino
        inicio.setF(inicio.getG() + inicio.getH()); // f = g + h
        abierta.add(inicio);                    // Agrega inicio a abierta

        while (!abierta.isEmpty()) {            // Mientras haya nodos
            Nodo actual = abierta.get(0);       // Toma el primero
            for (Nodo n : abierta) {            // Busca menor f
                if (n.getF() < actual.getF()) actual = n;
            }

            abierta.remove(actual);             // Saca de abierta
            cerrada.add(actual);                // Mete en cerrada

            if (actual == destino) return reconstruirRuta(actual); // Llegó

            for (Map.Entry<Nodo, Double> e : grafo.getVecinos(actual).entrySet()) {
                Nodo vecino = e.getKey();
                double peso = e.getValue();

                if (!abierta.contains(vecino) && !cerrada.contains(vecino)) {
                    vecino.setPadre(actual);    // Marca padre
                    vecino.setG(actual.getG() + peso); // Acumula g
                    vecino.setH(vecino.distanciaA(destino)); // h
                    vecino.setF(vecino.getG() + vecino.getH()); // f
                    abierta.add(vecino);        // Añade a abierta
                }
                else if (abierta.contains(vecino)) {
                    double nuevoG = actual.getG() + peso; // Costo alternativo
                    if (nuevoG < vecino.getG()) { // Si mejora
                        vecino.setPadre(actual);
                        vecino.setG(nuevoG);
                        vecino.setF(vecino.getG() + vecino.getH());
                    }
                }
            }
        }
        return new ArrayList<>();               // Sin ruta
    }

    private static List<Nodo> reconstruirRuta(Nodo actual) {
        List<Nodo> ruta = new ArrayList<>();    // Lista resultado
        while (actual != null) {                // Mientras haya nodo
            ruta.add(0, actual);                // Inserta al inicio
            actual = actual.getPadre();         // Sube al padre
        }
        return ruta;                            // Devuelve ruta
    }
}