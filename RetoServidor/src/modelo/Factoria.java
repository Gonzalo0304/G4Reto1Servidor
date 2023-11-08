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

     /**
     * Obtiene una instancia de la clase "Servidor".
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
    
        
    /**
     * Obtiene una instancia de la interfaz "Signable" que se relaciona con la clase "DaoImplementation".
     *
     * @return Una instancia de la interfaz "Signable" que permite interactuar con la base de datos y realizar operaciones de registro e inicio de sesión.
     */
    public static Signable getDao(){
        return new DaoImplementation();
    } 
     
}
