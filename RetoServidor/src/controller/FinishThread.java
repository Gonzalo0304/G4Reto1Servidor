/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import static java.lang.System.exit;
import java.net.ServerSocket;
import java.util.Scanner;


/**
 * Hilo para cerrar el servidor
 *
 * @author Gonzalo
 */
public class FinishThread extends Thread {

    private final ServerSocket servidor;

    public FinishThread(ServerSocket servidor) {
        this.servidor = servidor;
    }

    @Override
    public void run() {
        while (true) {            
            Scanner sc = new Scanner(System.in);
            String s = sc.next();
            if (s.equalsIgnoreCase("finish")) {
                Pool.deleteConexions();
                exit(0);
            }
        }
    }
}
