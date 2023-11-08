/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import exceptions.CheckSignInException;
import exceptions.CheckSignUpException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase representa un hilo de trabajo encargado de procesar solicitudes de
 * autenticación y registro de usuarios en el sistema.
 *
 * @author Iñigo
 */
public class WorkerThread extends Thread {

    final private Logger LOGGER = Logger.getLogger(WorkerThread.class.getName());

    private Socket socketUsuario;
    private MessageEnum orden = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Mensaje mensaje;
    private Signable s;

    public WorkerThread() {

    }

    /**
     * Constructor de la clase WorkerThread que recibe un socket de usuario.
     *
     * @param socketUsuario El socket que representa la conexión con el usuario.
     */
    public WorkerThread(Socket socketUsuario) {
        this.socketUsuario = socketUsuario;

    }

    /**
     * Método que se ejecuta cuando se inicia el hilo. Este método procesa las
     * solicitudes de autenticación y registro de usuarios en el sistema.
     */
    @Override
    public void start() {

        try {
            ois = new ObjectInputStream(socketUsuario.getInputStream());

            s = Factoria.getDao();

            mensaje = (Mensaje) ois.readObject();

            switch (mensaje.getMessageEnum()) {
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
