/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.MessageEnum;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Iñigo
 */
public class WorkerThread {
    private Socket socketUsuario;
    MessageEnum MESSAGE;
    
    WorkerThread(Socket socketUsuario, MessageEnum MESSAGE) {
        this.socketUsuario = socketUsuario;
        this.MESSAGE = MESSAGE;
    }
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
