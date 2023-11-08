/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import excepciones.CheckSignUpException;
import clases.MessageEnum;
import clases.Usuario;
import excepciones.CheckSignInException;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * Esta interfaz define métodos para la autenticación y registro de usuarios en el sistema.
 * 
 * @author Iñigo
 */
public interface Signable {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param usuario El objeto Usuario que contiene los datos del usuario a registrar.
     * @return Un valor de la enumeración MessageEnum que indica el resultado del registro.
     * @throws CheckSignUpException Si ocurren errores durante el proceso de registro.
     */
    public MessageEnum insertUser(Usuario usuario) throws CheckSignUpException;

   
    /**
     * Comprueba si un correo electrónico ya está registrado en el sistema.
     *
     * @param usuario El objeto Usuario que contiene el correo electrónico a verificar.
     * @return Un entero que indica el resultado de la comprobación:
     *         - 0 si el correo electrónico no está registrado en el sistema.
     *         - 1 si el correo electrónico ya está registrado en el sistema.
     * @throws CheckSignUpException Si ocurren errores durante la comprobación.
     */
    public Integer checkSignUp(Usuario usuario) throws CheckSignUpException;

     /**
     * Autentica a un usuario en el sistema.
     *
     * @param usuario El objeto usuario que intenta iniciar sesión.
     * @return Un mensaje de estado (`MessageEnum`) que indica el resultado de
     * la autenticación.
     * @throws excepciones.CheckSignInException  Si se produce un error de base de datos durante la
     * verificación.
     */
    public MessageEnum checkSignIn(Usuario usuario)throws CheckSignInException;
}
