/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import excepciones.CheckSignUpException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * La clase `Servidor` representa un servidor que se puede iniciar y apagar. Se
 * utiliza junto con un `ExecutorService` para gestionar la ejecución de hilos.
 * Permite iniciar y detener el servidor.
 *
 * @author Iñigo
 */
public class Servidor {

    private boolean isRunning = true;

    private static int numUsers = 0;

    final private Logger LOGGER = Logger.getLogger(ServerSocket.class.getName());

    final private int PORT = Integer.parseInt(ResourceBundle.getBundle("modelo.connection").getString("port"));
    final private int MAXUSERS = Integer.parseInt(ResourceBundle.getBundle("modelo.connection").getString("maxUser"));

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
                    workerThread.run();
                    
                    connectUser(workerThread);
                    
                } else {
                    oos = new ObjectOutputStream(skU.getOutputStream());
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMessageEnum(MessageEnum.MAXUSERS);
                    oos.writeObject(mensaje);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
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
