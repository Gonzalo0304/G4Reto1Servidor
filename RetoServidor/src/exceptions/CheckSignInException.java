/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * La clase "CheckSignInException" es una excepción personalizada que se lanza
 * cuando ocurre un error al comprobar una operación de inicio de sesión. Lleva
 * un mensaje de error descriptivo para proporcionar información sobre la causa
 * de la excepción.
 *
 *
 * @author Iñigo
 */
public class CheckSignInException extends Exception {

    public CheckSignInException(String message) {
        super(message);
    }

    public CheckSignInException() {
    }

}
