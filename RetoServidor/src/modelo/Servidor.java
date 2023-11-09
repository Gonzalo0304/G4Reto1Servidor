/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * La clase `Servidor` representa un servidor que escucha y gestiona conexiones de clientes a través de sockets.
 * Los clientes se conectan al servidor y pueden interactuar con él mediante mensajes.
 *
 * @author Iñigo
 */
public class Servidor {

    private boolean isRunning = true;

    private static int numUsers = 0;

    final private Logger LOGGER = Logger.getLogger(ServerSocket.class.getName());

    final private int PORT = Integer.parseInt(ResourceBundle.getBundle("modelo.connection").getString("port"));
    final private int MAXUSERS = Integer.parseInt(ResourceBundle.getBundle("modelo.connection").getString("maxUser"));

     /**
     * Inicia el servidor, permitiendo que los clientes se conecten y establezcan conexiones.
     * Escucha en el puerto especificado y crea un hilo de trabajo para cada cliente que se conecta.
     *
     * Este método es responsable de iniciar el servidor y esperar a que los clientes se conecten.
     * Cuando un cliente se conecta, se crea un hilo de trabajo (WorkerThread) dedicado para ese cliente,
     * lo que permite gestionar múltiples conexiones de manera simultánea.
     * El número máximo de clientes conectados simultáneamente está limitado por la constante MAXUSERS.
     *
     */
    public void startServidor() {
        Socket skU = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            LOGGER.info("El servidor empieza a funcionar");
            ServerSocket sk = new ServerSocket(PORT);

            while (isRunning) {
                if (numUsers < MAXUSERS) {
                    skU = sk.accept();
                    LOGGER.info("Se ha conectado un usuario");
                    
                    WorkerThread workerThread = new WorkerThread(skU);
                    connectUser(workerThread);
                    workerThread.start();
                                        
                } else {
                    oos = new ObjectOutputStream(skU.getOutputStream());
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMessageEnum(MessageEnum.MAXUSERS);
                    oos.writeObject(mensaje);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                oos.flush();
                oos.close();
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stop() {
        if (isRunning) {
            Logger.getLogger("Se ha apagado el servidor");
        } else {
            Logger.getLogger("El servidor ya está apagado");
        }
    }

    public static synchronized void connectUser(WorkerThread workerThread) {
        numUsers++;
    }

    public static synchronized void disconnectUser(WorkerThread workerThread) {
        numUsers--;
    }    

}
