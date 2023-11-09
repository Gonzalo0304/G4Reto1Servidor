/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Logger;

/**
 *
 * @author Iñigo
 *
 * Esta clase representa un pool de conexiones a una base de datos. Se utiliza
 * para gestionar las conexiones y reutilizarlas en lugar de crear una nueva
 * conexión cada vez que se necesita interactuar con la base de datos.
 *
 * El pool de conexiones sigue un patrón Singleton, lo que significa que solo
 * puede haber una instancia de esta clase. El pool mantiene una pila de
 * conexiones disponibles y las asigna a las solicitudes según sea necesario.
 *
 */
public class Pool {

    private static Stack<Connection> connections = new Stack();

    //Singleton para que haya una sola instancia del pool
    private static Pool instance = null;
    
    private static Logger LOOGER = Logger.getLogger(Pool.class.getName());

    //Constructor privado para evitar instanciación directa
    /**
     * Obtiene la instancia única del pool de conexiones. Si no existe una
     * instancia, crea una nueva.
     *
     * @return La instancia única del pool de conexiones.
     */
//    public static synchronized Pool getInstance() {
//        if (instance == null) {
//            instance = new Pool();
//        }
//        return instance;
//    }
    /**
     * Obtiene una conexión de la base de datos. Si hay conexiones disponibles
     * en el pool, se reutiliza una de ellas; de lo contrario, se crea una nueva
     * conexión.
     *
     * @return Una conexión activa a la base de datos.
     * @throws SQLException Si se produce un error al establecer la conexión.
     */
    public static synchronized Connection getConnection() throws SQLException {

        Connection connection = null;

        if (connections.isEmpty()) {
            LOOGER.info("Creando conexion");
            String URL = ResourceBundle.getBundle("modelo.connection").getString("CONNECTION");
            String USER = ResourceBundle.getBundle("modelo.connection").getString("DBUSER");
            String PASSWORD = ResourceBundle.getBundle("modelo.connection").getString("DBPASS");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } else {
            LOOGER.info("Asignando conexion");
            connection = connections.pop();
        }
        return connection;
    }

    /**
     * Devuelve una conexión al pool de conexiones para su posterior
     * reutilización.
     *
     * @param connection La conexión que se desea guardar en el pool.
     */
    public static void saveConnection(Connection connection) {
        LOOGER.info("Guardando conexion");
        connections.add(connection);
    }

    /**
     * Cierra una conexión y la añade nuevamente al pool de conexiones.
     *
     * @param connection La conexión que se debe cerrar y añadir de nuevo al
     * pool.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                LOOGER.info("Cerrando conexions");
                connection.close();
                connections.add(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
