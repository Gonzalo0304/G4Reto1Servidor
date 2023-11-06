/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 *
 * @author Iñigo
 *
 * Esta clase se encarga de gestionar conexiones a la base de datos. Permite
 * reutilizar conexiones en lugar de crear una nueva cada vez que se necesita
 * una conexión a la base de datos. Esta clase implementa el patrón Singleton
 * para garantizar una única instancia del pool de conexiones.
 *
 * Primero se debe obtener una instancia de `Pool` utilizando el método
 * getInstance()`. Luego, se puede llamar a los métodos `getConnection()` para
 * obtener una conexión a la base de datos, `saveConnection(Connection
 * connection)` para guardar la conexión en el pool y
 * `closeConnection(Connection connection)` para cerrar una conexión y
 * devolverla al pool.
 *
 */
public class Pool {

    private static Stack<Connection> connections = null;

    //Singleton para que haya una sola instancia del pool
    private static Pool instance = null;

    //Constructor privado para evitar instanciación directa
    public Pool() {
        connections = new Stack();
    }

    /**
     * Obtiene la única instancia del pool de conexiones.
     *
     * @return La instancia del pool de conexiones.
     */
    public static synchronized Pool getInstance() {
        if (instance == null) {
            instance = new Pool();
        }
        return instance;
    }

    /**
     * Obtiene una conexión a la base de datos desde el pool. Si el pool está
     * vacío, se crea una nueva conexión.
     *
     * @return Una conexión a la base de datos.
     * @throws SQLException Si se produce un error al obtener la conexión.
     */
    public synchronized Connection getConnection() throws SQLException {

        Connection connection = null;

        if (connections.isEmpty()) {

            String URL = ResourceBundle.getBundle("modelo.connection").getString("CONNECTION");
            String USER = ResourceBundle.getBundle("modelo.connection").getString("DBUSER");
            String PASSWORD = ResourceBundle.getBundle("modelo.connection").getString("DBPASS");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } else {
            connection = connections.pop();
        }
        return connection;
    }

    /**
     * Guarda una conexión en el pool para su reutilización.
     *
     * @param connection La conexión que se guarda en el pool.
     */
    public void saveConnection(Connection connection) {
        connections.add(connection);
    }

    /**
     * Cierra una conexión y la devuelve al pool para su reutilización.
     *
     * @param connection La conexión a cerrar y devolver al pool.
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                connections.add(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
