/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * Esta es la excepcion para cuando al comprobar si el correo introducido ya existe da algun error
 *
 * @author Iñigo
 */
public class CheckSignUpGmailException extends Exception {

    public CheckSignUpGmailException(String message) {
        super(message);
    }

}
