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
import java.util.Stack;

/**
 *
 * @author Iñigo
 *
 * Esta clase se encarga de gestionar conexiones a la base de datos. Permite
 * reutilizar conexiones en lugar de crear una nueva cada vez que se necesita
 * una conexión a la base de datos.
 */
public class Pool {

    private static Stack<Connection> connections = null;

    //Singleton para que haya una sola instancia del pool
    private static Pool instance = null;

    // Constructor privado para evitar instanciación directa
    public Pool() {
        connections = new Stack();
    }

    public static synchronized Pool getInstance() {
        if (instance == null) {
            instance = new Pool();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        
        Connection connection = null;
        
        if (connections.isEmpty()) {
            
        String URL = "jdbc:postgresql://192.168.20.59:8069/Produccion";
        String USER = "tuUsuario";
        String PASSWORD = "tuPassword";
        
        connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } else {
            // Si la lista de connection no está vacía, obtiene una connection de la lista
            connection = connections.pop();
        }
        return connection;
    }

    public void saveConnection(Connection connection) {
        //Guatrda la connection en la lista para volver a usarla
        connections.add(connection);
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                connections.add(connection);
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de errores
            }
        }
    }

  

}
