/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package retoservidor;

import controller.Servidor;

/**
 *
 * @author Iñigo
 */
public class RetoServidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Servidor servidor = new Servidor();
        servidor.startServidor();
        
    }
    
}
