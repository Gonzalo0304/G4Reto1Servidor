/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * La clase `WorkerThread` es un hilo de trabajo que se encarga de enviar un
 * mensaje (`MessageEnum`) mediante un socket de usuario. Este hilo se utiliza
 * para enviar mensajes a los clientes conectados a través de sockets.
 *
 * @author Iñigo
 */
public class WorkerThread {

    private Socket socketUsuario;
    private Mensaje MESSAGE;

    /**
     * Constructor de la clase `WorkerThread`.
     *
     * @param socketUsuario El socket a través del cual se enviará el mensaje.
     * @param MESSAGE El mensaje a enviar (representado por `MessageEnum`).
     */
    WorkerThread(Socket socketUsuario, Mensaje MESSAGE) {
        this.socketUsuario = socketUsuario;
        this.MESSAGE = MESSAGE;
    }

    /**
     * Ejecuta el hilo. Envía el mensaje al cliente mediante el socket.
     */
    public void run() {
        ObjectOutputStream out;
        
        

        try {
            out = new ObjectOutputStream(socketUsuario.getOutputStream());
            out.writeObject(MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
