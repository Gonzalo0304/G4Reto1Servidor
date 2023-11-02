/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import exceptions.CheckSignUpException;
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

    private boolean isRunning;

    private int numUsers = 0;

    final private Logger LOGGER = Logger.getLogger(ServerSocket.class.getName());

    final private int PORT = Integer.parseInt(ResourceBundle.getBundle("connection.properties").getString("port"));
    final private int MAXUSERS = Integer.parseInt(ResourceBundle.getBundle("connection.properties").getString("maxUsers"));

    public void start() {
        if (!isRunning) {
            isRunning = true;
            Logger.getLogger("Se ha iniciado el servidor");
            Mensaje mensaje = null;

            if (mensaje.getMessageType().equals(MessageEnum.SIGNUP)) {
                try {
                    DaoImplementation imple = Factoria.crearDaoBD();
                    imple.insertUser(sk);
                } catch (CheckSignUpException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            if (mensaje.getMessageType().equals(MessageEnum.SIGNIN)) {

            }

        } else {
            Logger.getLogger("El servidor ya está en funcionamiento");
        }

    }

    public void openServer() {
        Socket skU = null;
        Servidor skS = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            LOGGER.info("El servidor empieza a funcionar");
            ServerSocket sk = new ServerSocket(PORT);

            while (isRunning) {
                if (numUsers < MAXUSERS) {
                    skU = sk.accept();
                    LOGGER.info("Se ha conectado un usuario");
                    ois = new ObjectInputStream(skU.getInputStream());

                    Mensaje m = (Mensaje) ois.readObject();
                    conectUser();

                    WorkerThread workerThread = new WorkerThread(sk, m);
                    workerThread.run();

                    oos = new ObjectOutputStream(skU.getOutputStream());
                    Mensaje mensaje = new Mensaje();
                    disconnectUser();
                } else {
                    oos = new ObjectOutputStream(skU.getOutputStream());
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMessageType(MessageEnum.MAXUSERS);
                    oos.writeObject(mensaje);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
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

    public synchronized void conectUser() {
        numUsers++;
    }

    public synchronized void disconnectUser() {
        numUsers--;
    }

}
