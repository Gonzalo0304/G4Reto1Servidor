/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.io.IOException;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La clase `ServerSocket` representa un servidor de sockets que escucha
 * conexiones entrantes en un puerto específico y crea hilos (`Servidor`) para
 * manejar las conexiones de los clientes.
 *
 * Esta clase utiliza un puerto definido en un archivo de propiedades para
 * establecer la conexión y gestionar las solicitudes entrantes.
 *
 * @author Iñigo
 */
public class ServerSocket {

    static final Integer PUERTO = Integer.parseInt(ResourceBundle.getBundle("clases.connection").getString("puerto"));

    /**
     * Crea una instancia de `ServerSocket` y ejecuta un servidor de sockets.
     */
    public ServerSocket() {
        java.net.ServerSocket skServidor = null;
        Socket skUsuario = null;
        
        try {

            skServidor = new java.net.ServerSocket(PUERTO);
            Logger.getLogger("Escucho el puerto " + PUERTO);
            while (true) {
                skUsuario = skServidor.accept();

                Servidor hilo = new Servidor(skUsuario);
                hilo.start();
            }

        } catch (IOException ex) {
            Logger.getLogger(java.net.ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                skServidor.close();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
