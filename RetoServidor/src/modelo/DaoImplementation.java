/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import clases.InterfaceClienteServidor;
import clases.Mensaje;
import excepciones.CheckSignUpException;
import clases.MessageEnum;
import static clases.MessageEnum.ERRORSIGNUP;
import static clases.MessageEnum.SIGNIN;
import static clases.MessageEnum.SIGNUP;
import clases.Usuario;
import static com.sun.javafx.scene.control.skin.FXVK.Type.EMAIL;
import excepciones.CheckSignInException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * La clase DaoImplementation es una implementación de la interfaz `Signable`
 * que se utiliza para interactuar con una base de datos, gestionar la inserción
 * de usuarios y la autenticación de usuarios en un sistema.
 *
 * Esta clase proporciona métodos para insertar usuarios en la base de datos,
 * verificar el registro (signUp) y la autenticación (signIn) de usuarios, y
 * gestionar las conexiones a la base de datos
 *
 * @author Iñigo
 */
public class DaoImplementation implements InterfaceClienteServidor {

    final String INSETRESPARTNER = "INSERT INTO res_partner(create_date, name, create_uid, write_uid, street, zip, city, phone, active) VALUES(?,?,2,2,?,?,?,?,'true');";
    final String INSERTRESUSER = "INSERT INTO res_users(company_id, partner_id, create_date, login, password, create_uid, write_date, notification_type) VALUES(1,?,?,?,?,2,?,'email');";
    final String INSERTRESCOMP = "INSERT INTO res_company_users_rel (cid, user_id) VALUES (1,?);";
    final String INSERTRESGROUP = "INSERT INTO res_groups_users_rel (gid, uid) VALUES (16,?),(26,?),(28,?),(31,?);";
    final String SELECTRESPARTNERID = "SELECT MAX(id) AS id FROM res_partner;";
    final String SELECUSERID = "SELECT MAX(id) AS id FROM res_users;";
    final String SELECTEMAIL = "SELECT login FROM res_users WHERE login = ? GROUP BY login;";
    final String SELECTPASS = "SELECT password FROM res_users WHERE login = ? GROUP BY password;";

    private Logger LOGGER = Logger.getLogger(DaoImplementation.class.getName());
    private Pool connection = null;
    private Connection c;
    private PreparedStatement statement;
    private boolean errorCondition = false;

    /**
     *
     * Inserta un nuevo usuario en el sistema.
     *
     * @param skUsuario
     * @param skUsuario El objeto `skUsuario` que representa al nuevo usuario a
     * insertar.
     * @return Un mensaje de estado (`MessageEnum`) que indica el resultado de
     * la inserción.
     * @throws excepciones.CheckSignUpException
     * @throws SQLException * @throws SQLException Si se produce un error de
     * base de datos durante la inserción.
     */
    @Override
    public MessageEnum insertUser(Usuario usuario) throws CheckSignUpException {

        connection = new Pool();
        Integer check;
        MessageEnum Order;

        try {
            c = connection.getConnection();

            check = checkSignUp(usuario);

            if (check == 0) {
                statement = c.prepareStatement(INSETRESPARTNER);
                statement.setDate(1, Date.valueOf(usuario.getCreacion()));
                statement.setString(2, usuario.getNombre());
                statement.setString(3, usuario.getDireccion());
                statement.setInt(4, usuario.getCodigoPostal());
                statement.setString(5, usuario.getDireccion());
                statement.setInt(6, usuario.getTelefono());
                statement.executeUpdate();

                LOGGER.info("Usuario introducido en la tabla res_partner");
                statement = c.prepareStatement(SELECTRESPARTNERID);
                ResultSet result = statement.executeQuery();
                result.next();
                Integer partnerId = result.getInt("id");

                statement = c.prepareStatement(INSERTRESUSER);
                statement.setInt(1, partnerId);
                statement.setDate(2, Date.valueOf(usuario.getCreacion()));
                statement.setString(3, usuario.getEmail());
                statement.setString(4, usuario.getPass());
                statement.setDate(5, Date.valueOf(usuario.getCreacion()));
                statement.executeUpdate();
                LOGGER.info("Usuario introducido en la tabla res_user");

                statement = c.prepareStatement(SELECUSERID);
                result = statement.executeQuery();
                result.next();
                Integer userId = result.getInt("id");

                statement = c.prepareStatement(INSERTRESCOMP);
                statement.setInt(1, userId);
                statement.executeUpdate();
                LOGGER.info("Usuario introducido en la tabla res_company_users_rel");

                statement = c.prepareStatement(INSERTRESGROUP);
                statement.setInt(1, userId);
                statement.setInt(2, userId);
                statement.setInt(3, userId);
                statement.setInt(4, userId);
                statement.executeUpdate();
                LOGGER.info("Usuario introducido en la tabla res_groups_users_rel");

                connection.saveConnection(c);
                return MessageEnum.OK;

            } else if (check == 1) {
                connection.saveConnection(c);
                throw new CheckSignUpException();
            }
            connection.saveConnection(c);
        } catch (SQLException ex) {
            connection.saveConnection(c);
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Verifica si un usuario ya está registrado en el sistema.
     *
     * @param usuario El objeto `Usuario` que se desea comprobar para ver si ya
     * está registrado.
     * @return Un entero que representa el estado de registro del usuario. 0 si
     * no está registrado, 1 si ya lo está.
     * @throws excepciones.CheckSignInException
     * @throws SQLException Si se produce un error de base de datos durante la
     * verificación.
     */
    @Override
    public Integer checkSignUp(Usuario usuario) throws CheckSignUpException {

        ResultSet resultset;
        Integer checkE = 0;

        try {
            statement = c.prepareStatement(SELECTEMAIL);
            statement.setString(1, usuario.getEmail());
            resultset = statement.executeQuery();
            if (resultset.next()) {
                LOGGER.info("El usuario introducido no existe se procedera a introducirlo en las tablas correspondientes");

                return 1;
            }
            //Logger.getLogger(DaoImplementation.class.getEmail()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        LOGGER.info("El usuario introducido ya existe se procedera a cancelar el registro");

        return checkE;
    }

    /**
     * Verifica si un usuario puede iniciar sesión en el sistema.
     *
     * @param usuario El objeto `Usuario` que intenta iniciar sesión.
     * @return Un mensaje de estado (`MessageEnum`) que indica el resultado de
     * la autenticación.
     * @throws excepciones.CheckSignInException
     */
    @Override
    public MessageEnum checkSignIn(Usuario usuario) throws CheckSignInException {
        ResultSet resultset;
        String i;
        Boolean login = false;
        Boolean pass = false;

        MessageEnum ORDER = null;

        connection = new Pool();

        try {
            c = connection.getConnection();
            statement = c.prepareStatement(SELECTEMAIL);
            statement.setString(1, usuario.getEmail());
            resultset = statement.executeQuery();

            if (resultset.next()) {
                i = resultset.getString("login");
                if (i.equals(usuario.getEmail())) {
                    login = true;
                    LOGGER.info("El usuario introducido coincide con uno existente");

                }
            }

            c = connection.getConnection();
            statement = c.prepareStatement(SELECTPASS);
            statement.setString(1, usuario.getEmail());
            resultset = statement.executeQuery();

            if (resultset.next()) {
                i = resultset.getString("password");
                if (i.equals(usuario.getPass())) {
                    pass = true;
                    LOGGER.info("La contraseña del usuario coincide con la del usuari introducido");
                }
            }

            if (login == true && pass == true) {
                ORDER = MessageEnum.OK;
            }

            if (login == false || pass == false) {
                throw new CheckSignInException();
            }
            connection.saveConnection(c);
        } catch (SQLException ex) {

            connection.saveConnection(c);
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ORDER;
    }

    @Override
    public Mensaje signIn(Mensaje respuesta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Mensaje signUp(Mensaje respuesta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void closeApli() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
