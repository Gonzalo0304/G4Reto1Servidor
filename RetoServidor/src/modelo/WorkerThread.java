/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.InterfaceClienteServidor;
import clases.Mensaje;
import clases.MessageEnum;
import clases.Usuario;
import excepciones.CheckSignInException;
import excepciones.CheckSignUpException;
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
    

    private Socket socketUsuario ;
    private MessageEnum orden = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Mensaje mensaje;
    private InterfaceClienteServidor s;

    /**
     * Constructor de la clase `WorkerThread`.
     *
     * @param socketUsuario El socket a través del cual se enviará el mensaje.
     * @param MESSAGE El mensaje a enviar (representado por `MessageEnum`).
     */
    
    public WorkerThread(){
        
    }
    
    
    public WorkerThread(Socket socketUsuario) {
        this.socketUsuario = socketUsuario;

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
            
            switch(mensaje.getMessageEnum()) {
                case SIGNIN:
                    LOGGER.info("El hilo ha recibido un sign in request");
                    orden = s.checkSignIn(mensaje.getUser());
                    mensaje.setMessageEnum(orden);
                    break;
                    
                case SIGNUP:
                    LOGGER.info("SignUp request");                    
                    orden = s.insertUser(mensaje.getUser());
                    mensaje.setMessageEnum(orden);
                    break;
            }
            
   } catch (IOException e) {
       
            mensaje.setMessageEnum(MessageEnum.ERROR);
        } catch (ClassNotFoundException ex) {
            mensaje.setMessageEnum(MessageEnum.ERROR);
            mensaje.setMessageEnum(MessageEnum.ERRORSIGNIN);
        } catch (CheckSignUpException ex) {
            mensaje.setMessageEnum(MessageEnum.ERRORSIGNUP);
        } catch (CheckSignInException ex) {
            mensaje.setMessageEnum(MessageEnum.ERRORSIGNIN);
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
