/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import clases.MessageEnum;
import exceptions.CheckSignUpException;
import java.net.Socket;
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
    
    private Socket skUsuario;

    public Servidor(Socket skUsuario) {

        this.isRunning = false;
        this.skUsuario = skUsuario;
        
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            Logger.getLogger("Se ha iniciado el servidor");
            Mensaje mensaje = null;
            
            if(mensaje.getMessageEnum().equals(MessageEnum.SIGNUP)){
                try {
                    DaoImplementation imple = Factoria.crearDaoBD();
                    imple.insertUser(skUsuario);
                } catch (CheckSignUpException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } 
            
             if(mensaje.getMessageEnum().equals(MessageEnum.SIGNIN)){
                
            } 
            
        } else {
             Logger.getLogger("El servidor ya está en funcionamiento");
        }
        
    }

    public void stop() {

        if (isRunning) {

            Logger.getLogger("Se ha apagado el servidor");
        } else {
            Logger.getLogger("El servidor ya está apagado");
        }

    }

}
