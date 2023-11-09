/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.InterfaceClienteServidor;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 *
 * @author Iñigo
 *
 * /**
 * La clase Factoria es una fábrica que proporciona instancias de diferentes clases.
 * En este caso, ofrece métodos para obtener instancias de clases relacionadas con la
 * gestión de conexiones a servidores y bases de datos. Estos métodos estáticos permiten
 * la creación de objetos de las clases correspondientes para su uso en la aplicación.
 * La clase no contiene campos o lógica significativa, ya que se limita a proporcionar
 * instancias de otras clases.
 * 
 */
public class Factoria {

    private ExecutorService threadPool;
    private Logger LOGGER = Logger.getLogger(Factoria.class.getName());

    
    /**
     * Constructor de la clase Factoria. Inicializa el pool de hilos con un
     * número máximo predefinido de hilos.
     *
     * @return Una instancia de la clase "Servidor" configurada con el hilo de ejecución.
     */
    public static Servidor getServidor() {
        //return new Servidor(threadPool);
        return null;
    }
    
        
    /**
     * Crea y devuelve una instancia de la clase "DaoImplementation".
     *
     * @return Una instancia de la clase "DaoImplementation" que se utiliza para interactuar con la base de datos.
     */
      public static DaoImplementation crearDaoBD(){
        return new DaoImplementation();
    }
      
    public static InterfaceClienteServidor getDao(){
        return new DaoImplementation();
    } 
     
}
