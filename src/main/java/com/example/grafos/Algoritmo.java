package com.example.grafos;

import java.util.*;

public class Algoritmo {

    public static List<Nodo> buscarRuta(Grafo grafo, Nodo inicio, Nodo destino) {
        List<Nodo> abierta = new ArrayList<>();
        List<Nodo> cerrada = new ArrayList<>();
        for (Nodo n : grafo.getNodos()) {
            n.setG(Double.POSITIVE_INFINITY); //g=infinito
            n.setH(0); //heuristica=0
            n.setF(Double.POSITIVE_INFINITY); //f=infinito
            n.setPadre(null); //sin padre
        }

        inicio.setG(0);//costo inicio = 0
        inicio.setH(inicio.distanciaA(destino)); //heurística al destino
        inicio.setF(inicio.getG() + inicio.getH()); //f=g+h
        abierta.add(inicio); //agrega inicio a abierta

        while (!abierta.isEmpty()) { //mientras haya nodos sigue buscando
            Nodo actual = abierta.get(0); //toma el primero
            for (Nodo n : abierta) { // Busca menor f
                if (n.getF() < actual.getF()) actual = n;
            }

            abierta.remove(actual); //saca de abierta
            cerrada.add(actual); //mete en cerrada

            if (actual == destino) return reconstruirRuta(actual); //verifica si es el destino

            for (Map.Entry<Nodo, Double> e : grafo.getVecinos(actual).entrySet()) { //busca los nodos vecinos
                Nodo vecino = e.getKey();
                double peso = e.getValue();

                if (!abierta.contains(vecino) && !cerrada.contains(vecino)) {
                    vecino.setPadre(actual); //marca nodo actual como padre
                    vecino.setG(actual.getG() + peso); //acumula actual mas vecino
                    vecino.setH(vecino.distanciaA(destino)); //calcula la heuristica del vecino
                    vecino.setF(vecino.getG() + vecino.getH()); //calcula f=g+h
                    abierta.add(vecino);//añade a la lista abierta
                }
                else if (abierta.contains(vecino)) { //si ya estaba en abierta
                    double nuevoG = actual.getG() + peso; //si llegara al vecino desde el actual
                    if (nuevoG < vecino.getG()) { //si mejora el coste con el anterior
                        vecino.setPadre(actual); //actualiza padre
                        vecino.setG(nuevoG);//actualiza g
                        vecino.setF(vecino.getG() + vecino.getH());//recalcula
                    }
                }
            }
        }
        return new ArrayList<>(); //si no enucentra el destino devuelve la lista vacia
    }

    private static List<Nodo> reconstruirRuta(Nodo actual) {
        List<Nodo> ruta = new ArrayList<>(); //crea la lista de la ruta
        while (actual != null) { //mientras el nodo acutal no sea null sigue bsucando padres
            ruta.add(0, actual); //inserta el nodo actual a la lista
            actual = actual.getPadre(); //avanza al padre del nodo actual
        }
        return ruta;//devuelve ruta
    }
}