/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
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
public class ServidorSocket {

    private Integer PUERTO = Integer.parseInt(ResourceBundle.getBundle("clases.conexion").getString("puerto"));

    /**
     * Crea una instancia de `ServerSocket` y ejecuta un servidor de sockets.
     */
    public ServidorSocket() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        ServerSocket skServidor = null;
        Socket skUsuario = null;
        Mensaje msj = new Mensaje();
        
        try {
            skServidor = new ServerSocket(PUERTO);
            Logger.getLogger("Escucho el puerto " + PUERTO);
            while (true) {
                skUsuario = skServidor.accept();
                ois = new ObjectInputStream(skUsuario.getInputStream());
                msj = (Mensaje) ois.readObject();
                
                WorkerThread hilo = new WorkerThread(skUsuario, msj);
                hilo.run();
                
                oos = new ObjectOutputStream(skUsuario.getOutputStream());
                oos.writeObject(msj);
            }

        } catch (IOException ex) {
            Logger.getLogger(java.net.ServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                skServidor.close();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private ServidorSocket(Integer PUERTO) {
        this.PUERTO=PUERTO;
    }

}
