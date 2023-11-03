/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.Mensaje;
import exceptions.CheckSignUpException;
import clases.MessageEnum;
import clases.Usuario;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * Esta interfaz define operaciones relacionadas con la gestión de usuarios y la
 * autenticación en la App. Proporciona una estructura para la inserción de
 * usuarios, la comprobación de registro (signUp) y la comprobación de inicio de
 * sesión (signIn). Implementar esta interfaz permite a los desarrolladores
 * personalizar la lógica de autenticación de acuerdo a los requisitos
 * específicos del sistema.
 *
 * @author Iñigo
 */
public interface Signable {

    /**
     * Inserta un nuevo usuario en el sistema.
     *
     * @param usuario El objeto Usuario que representa un nuevo usuario.
     * @return Un mensaje de estado (MessageEnum) que indica el resultado de la
     * inserción.
     * @throws SQLException Si se produce un error de base de datos durante la
     * inserción.
     */
    public MessageEnum insertUser(Socket skUsuario) throws CheckSignUpException;

    /**
     * Verifica si un usuario ya está registrado en el sistema.
     *
     * @param usuario El objeto `Usuario` que se desea comprobar para ver si ya
     * está registrado.
     * @return Un valor entero que representa el estado de registro del usuario.
     * 0 si no está registrado, 1 si ya lo está.
     * @throws SQLException Si se produce un error de base de datos durante la
     * verificación.
     */
    public Integer checkSignUp(Usuario usuario) throws CheckSignUpException;

    /**
     * Verifica si un usuario puede iniciar sesión en el sistema.
     *
     * @param usuario El objeto usuario que intenta iniciar sesión.
     * @return Un mensaje de estado (`MessageEnum`) que indica el resultado de
     * la autenticación.
     */
    public Usuario checkSignIn(Usuario usuario);
}
