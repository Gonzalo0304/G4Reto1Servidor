/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.InterfaceClienteServidor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 *
 * @author Iñigo
 *
 * Esta es la clase Factoria que se utiliza para crear instancias de Servidor.
 */
public class Factoria {

    private ExecutorService threadPool;
    private Logger LOGGER = Logger.getLogger(Factoria.class.getName());

    
    /**
     * Constructor de la clase Factoria. Inicializa el pool de hilos con un
     * número máximo predefinido de hilos.
     *
     * @param maxThreads El número máximo de hilos en el pool.
     */


    /**
     * Obtiene una instancia de Servidor.
     *
     * @return Una instancia de Servidor configurada con el pool de hilos.
     */
    public static Servidor getServidor() {
        //return new Servidor(threadPool);
        return null;
    }
    
      public static DaoImplementation crearDaoBD(){
        return new DaoImplementation();
    }
      
    public static InterfaceClienteServidor getDao(){
        return new DaoImplementation();
    } 
     
}
