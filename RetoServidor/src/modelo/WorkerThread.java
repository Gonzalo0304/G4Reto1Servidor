/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import clases.Usuario;
import exceptions.CheckSignUpException;
import exceptions.ServerException;
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
public class WorkerThread extends Thread {
    

    private Socket socketUsuario = null;
    private Usuario u = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Mensaje mensaje;
    private Signable s;

    /**
     * Constructor de la clase `WorkerThread`.
     *
     * @param socketUsuario El socket a través del cual se enviará el mensaje.
     * @param MESSAGE El mensaje a enviar (representado por `MessageEnum`).
     */
    
    public WorkerThread(){
        
    }
    
    
    public WorkerThread(Socket socketUsuario, Mensaje mensaje) {
        this.socketUsuario = socketUsuario;
        this.mensaje = mensaje;
    }
    
    final private Logger LOGGER = Logger.getLogger(WorkerThread.class.getName());
    
    
    /**
     * Ejecuta el hilo. Envía el mensaje al cliente mediante el socket.
     */
    @Override
    public void run() {

        try {
            ois = new ObjectInputStream(socketUsuario.getInputStream());
            Factoria factoria = new Factoria();
            s = factoria.getDao();
            
            mensaje = (Mensaje) ois.readObject();
            
            switch(mensaje.getMessageType()) {
                case SIGNIN:
                    LOGGER.info("El hilo ha recibido un sign in request");
                    u = s.checkSignIn(mensaje.getUser());
                    mensaje.setUser(u);
                    mensaje.setMessageType(MessageEnum.OK);
                    break;
                    
                case SIGNUP:
                    LOGGER.info("SignUp request");
                    
                    s.checkSignUp(mensaje.getUser());
                    mensaje.setUser(u);
                    mensaje.setMessageType(MessageEnum.OK);
                    break;
            }
            
   } catch (IOException e) {
       
            mensaje.setMessageType(MessageEnum.ERROR);
        } catch (ClassNotFoundException ex) {
            mensaje.setMessageType(MessageEnum.ERROR);
            mensaje.setMessageType(MessageEnum.ERRORSIGNIN);
        } catch (CheckSignUpException ex) {
            mensaje.setMessageType(MessageEnum.ERRORSIGNUP);
        } finally {
            try {
                oos = new ObjectOutputStream(socketUsuario.getOutputStream());
                oos.writeObject(mensaje);
                Servidor.disconnectUser(this);
                ois.close();
                oos.close();
                socketUsuario.close();
            } catch (IOException ex) {
                Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

}

}
