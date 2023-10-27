/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Iñigo 
 * 
 * Esta es la clase Factoria que se utiliza para crear instancias de Servidor.
 */
public class Factoria {
     private ExecutorService threadPool; // Pool de hilos para procesamiento concurrente

     /**
     * Constructor de la clase Factoria.
     * Inicializa el pool de hilos con un número máximo predefinido de hilos.
     * @param maxThreads El número máximo de hilos en el pool.
     */
     
    public Factoria() {
        // Inicializa el pool de hilos con un número máximo de hilos predefinido
        int maxThreads = 10;
        threadPool = Executors.newFixedThreadPool(maxThreads);
    }

    /**
     * Obtiene una instancia de Servidor.
     * @return Una instancia de Servidor configurada con el pool de hilos.
     */
    public Servidor getServidor() {
        // Crea una instancia de Servidor pasando el pool de hilos
        return new Servidor(threadPool);
    }
}