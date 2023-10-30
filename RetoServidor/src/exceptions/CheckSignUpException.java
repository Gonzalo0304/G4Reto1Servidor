/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Iñigo
 */
public class CheckSignUpException extends Exception {
    
    public CheckSignUpException(String message) {
        super(message);
    }

    public CheckSignUpException() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
