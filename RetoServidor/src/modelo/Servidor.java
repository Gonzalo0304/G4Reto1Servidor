/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.concurrent.ExecutorService;

/**
 *
 * @author Iñigo
 */
public class Servidor {
    
    private ExecutorService threadPool;
    
    private boolean isRunning;
    
    public Servidor(ExecutorService threadPool){
        
        this.threadPool = threadPool;
        this.isRunning = false;
        
    }
    
    public void start(){
        if (!isRunning) {
            isRunning = true;
            System.out.println("El servidor ha iniciado");
        } else {
            System.out.println("El servidor ya está en unfionamiento");
        }
    }
    
    public void stop(){
        
        if(isRunning) {
            
            System.out.println("Se ha apagado el servidor");   
        } else {
            System.out.println("El servidor ya está apagado");
        }
        
    }
    
    
}
