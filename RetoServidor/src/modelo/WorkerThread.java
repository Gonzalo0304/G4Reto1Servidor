/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import clases.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    

    private Socket socketUsuario = null;
    private Usuario u = null;
    private ObjectOutputStream ois = null;
    private ObjectInputStream oos = null;
    private Mensaje mensaje;

    /**
     * Constructor de la clase `WorkerThread`.
     *
     * @param socketUsuario El socket a través del cual se enviará el mensaje.
     * @param MESSAGE El mensaje a enviar (representado por `MessageEnum`).
     */
    WorkerThread(Socket socketUsuario, Mensaje mensaje) {
        this.socketUsuario = socketUsuario;
        this.mensaje = mensaje;
    }

    /**
     * Ejecuta el hilo. Envía el mensaje al cliente mediante el socket.
     */
    @Override
    public void run() {
        
        int c = 0;

        try {
            ois = new ObjectOutputStream(socketUsuario.getOutputStream());
            Factoria factoria = new Factoria();
             = factoria.getServidor();
            
            encap = (Encapsulator ois.readObject());
            
            switch(encap.getMensaje()) {
                case SIGN
            }
            
            
            
            
            
            
            
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
