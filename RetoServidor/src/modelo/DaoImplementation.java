/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import exceptions.CheckSignUpException;
import clases.MessageEnum;
import static clases.MessageEnum.ERRORSIGNUP;
import static clases.MessageEnum.SIGNIN;
import static clases.MessageEnum.SIGNUP;
import clases.Usuario;
import static com.sun.javafx.scene.control.skin.FXVK.Type.EMAIL;
import exceptions.CheckSignInException;
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
 * Esta clase se encarga de insertar nuevos usuarios en la base de datos,
 * verificar la existencia de un correo electrónico en el proceso de registro y
 * comprobar las credenciales de inicio de sesión.
 *
 * @author Iñigo
 */
public class DaoImplementation implements Signable {

    final String INSETRESPARTNER = "INSERT INTO res_partner(create_date, name, create_uid, write_uid, street, zip, city, phone, active) VALUES(?,?,2,2,?,?,?,?,'true');";
    final String INSERTRESUSER = "INSERT INTO res_users(company_id, partner_id, create_date, login, password, create_uid, write_date, notification_type) VALUES(1,?,?,?,?,2,?,'email');";
    final String INSERTRESCOMP = "INSERT INTO res_company_users_rel (cid, user_id) VALUES (1,?);";
    final String INSERTRESGROUP = "INSERT INTO res_groups_users_rel (gid, uid) VALUES (16,?),(26,?),(28,?),(31,?);";
    final String SELECTRESPARTNERID = "SELECT MAX(id) AS id FROM res_partner;";
    final String SELECUSERID = "SELECT MAX(id) AS id FROM res_users;";
    final String SELECTEMAIL = "SELECT login FROM res_users WHERE login = ? GROUP BY login;";
    final String SELECTPASS = "SELECT password FROM res_users WHERE login = ? GROUP BY password;";

    private Pool pool = null;
    private Connection c;
    private PreparedStatement statement;
    private boolean errorCondition = false;

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param usuario El objeto "Usuario" que contiene los datos del usuario a
     * registrar.
     * @return Un valor de la enumeración "MessageEnum" que indica el resultado
     * de la operación.
     * @throws CheckSignUpException Si ocurre un error durante el proceso de
     * registro se lanza esta excepción.
     */
    @Override
    public MessageEnum insertUser(Usuario usuario) throws CheckSignUpException {

        Integer check;
        MessageEnum Order;

        try {
            c.setAutoCommit(false);
            c = Pool.getConnection();

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

                statement = c.prepareStatement(SELECUSERID);
                result = statement.executeQuery();
                result.next();
                Integer userId = result.getInt("id");

                statement = c.prepareStatement(INSERTRESCOMP);
                statement.setInt(1, userId);
                statement.executeUpdate();

                statement = c.prepareStatement(INSERTRESGROUP);
                statement.setInt(1, userId);
                statement.setInt(2, userId);
                statement.setInt(3, userId);
                statement.setInt(4, userId);

                statement.executeUpdate();

                c.commit();
                return MessageEnum.OK;

            } else if (check == 1) {
                c.rollback();
                throw new CheckSignUpException();
            }

        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Pool.saveConnection(c);
        }
        return null;
    }

    /**
     * Verifica si un correo electrónico ya existe en la base de datos durante
     * el proceso de registro.
     *
     * @param usuario El objeto "Usuario" que contiene el correo electrónico a
     * verificar.
     * @return Un entero que indica si el correo electrónico ya existe (1) o no
     * (0).
     * @throws CheckSignUpException Si ocurre un error durante la verificación,
     * se lanza esta excepción.
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

                return 1;
            }
            //Logger.getLogger(DaoImplementation.class.getEmail()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkE;
    }

    /**
     * Verifica las credenciales de inicio de sesión de un usuario.
     *
     * @param usuario El objeto "Usuario" que contiene las credenciales a
     * comprobar (correo electrónico y contraseña).
     * @return Un valor de la enumeración "MessageEnum" que indica el resultado
     * de la comprobación.
     * @throws CheckSignInException Si las credenciales no son válidas se lanza
     * esta excepción.
     */
    @Override
    public MessageEnum checkSignIn(Usuario usuario) throws CheckSignInException {
        ResultSet resultset;
        String i;
        Boolean login = false;
        Boolean pass = false;

        MessageEnum ORDER = null;

        try {
            c = Pool.getConnection();
            statement = c.prepareStatement(SELECTEMAIL);
            statement.setString(1, usuario.getEmail());
            resultset = statement.executeQuery();

            if (resultset.next()) {
                i = resultset.getString("login");
                if (i.equals(usuario.getEmail())) {
                    login = true;
                }
            }

            c = Pool.getConnection();
            statement = c.prepareStatement(SELECTPASS);
            statement.setString(1, usuario.getEmail());
            resultset = statement.executeQuery();

            if (resultset.next()) {
                i = resultset.getString("password");
                if (i.equals(usuario.getPass())) {
                    pass = true;
                }
            }

            if (login == true && pass == true) {
                ORDER = MessageEnum.OK;
            }

            if (login == false || pass == false) {
                throw new CheckSignInException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            Pool.saveConnection(c);
        }
        return ORDER;
    }

    /*private String getEmail(Usuario usuario) {
        ResultSet resultset;
        String i = null;
        try {
            statement = c.prepareStatement(SELECTEMAIL);
            statement.setString(1, usuario.getEmail());
            resultset = statement.executeQuery();

            if(resultset.next()) {
                i = statement.getString("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName().log(Level.SEVERE, null, ex));
        }
        return i;
    }*/
 /* private Integer countConnection(Integer id) {
        ResultSet resultset;
        Integer i = null;

        try{
            statement = c.prepareStatement(SELECTCOUNTID);
            statement.setInt(1, id);
            resultset = statement.executeQuery();

            if(resultset.next()){
                i = resultset.getInt("num");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName().log(Level.SEVERE, null, ex));
        }
        return i;
    } */
}
