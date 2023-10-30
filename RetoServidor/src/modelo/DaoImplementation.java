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
public class DaoImplementation implements Signable {

    final String INSETRESPARTNER = "INSERT INTO res_partner(create_date, name, create_uid, write_uid, street, zip, city, phone, active) VALUES(?,?,2,2,?,?,?,?,'true');";
    final String INSERTRESUSER = "INSERT INTO res_users(company_id, partner_id, create_date, login, password, create_uid, write_date, notification_type) VALUES(1,?,?,?,?,2,?,'email');";
    final String INSERTRESCOMP = "INSERT INTO res_company_user_rel (cid, user_id) VALUES (1,?);";
    final String INSERTRESGROUP = "INSERT INTO res_groups_users_rel {gid, uid} VALUES (16,?),(26,?),(28,?),(31,?);";
    final String SELECTRESPARTNERID = "SELECT MAX(id) AS id FROM res_partner;";
    final String SELECUSERID = "SELECT MAX(id) AS id FROM res_users;";
    final String SELECTEMAIL = "SELECT email FROM usuario WHERE email = ? GROUP BY email;";
    final String SELECTPASS = "SELECT pass FROM usuario WHERE login = ? GROUP BY pass;";

    private Pool connection = null;
    private Connection c;
    private PreparedStatement statement;
    private boolean errorCondition = false;


    /**
     *
     * Inserta un nuevo usuario en el sistema.
     *
     * @param skUsuario
     * @param usuario El objeto `Usuario` que representa al nuevo usuario a
     * insertar.
     * @return Un mensaje de estado (`MessageEnum`) que indica el resultado de
     * la inserción.
     * @throws exceptions.CheckSignUpException
     * @throws SQLException * @throws SQLException Si se produce un error de
     * base de datos durante la inserción.
     */
    @Override
    public MessageEnum insertUser(Socket skUsuario) throws CheckSignUpException {

        connection = new Pool();
        Integer check;
        MessageEnum Order;

        try {
            c = connection.getConnection();
            Usuario usuario = null;
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

                ResultSet result = statement.executeQuery(SELECTRESPARTNERID);
                int partnerId = result.getInt("id");

                statement = c.prepareStatement(INSERTRESUSER);
                statement.setInt(1, partnerId);
                statement.setDate(2, Date.valueOf(usuario.getCreacion()));
                statement.setString(3, usuario.getEmail());
                statement.setString(4, usuario.getPass());
                statement.setDate(5, Date.valueOf(usuario.getCreacion()));
                statement.executeUpdate();

                result = statement.executeQuery(SELECUSERID);
                int userId = result.getInt("id");

                statement = c.prepareStatement(INSERTRESCOMP);
                statement.setInt(1, userId);
                statement.executeUpdate();

                statement = c.prepareStatement(INSERTRESGROUP);
                statement.setInt(1, userId);
                statement.setInt(2, userId);
                statement.setInt(3, userId);
                statement.setInt(4, userId);

                statement.executeUpdate();

                connection.saveConnection(c);
                return Order = SIGNUP;
                
                
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
     * @throws exceptions.CheckSignInException
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
                
                
                return 1;
            }
            //Logger.getLogger(DaoImplementation.class.getEmail()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return checkE;
    }

    /**
     * Verifica si un usuario puede iniciar sesión en el sistema.
     *
     * @param usuario El objeto `Usuario` que intenta iniciar sesión.
     * @return Un mensaje de estado (`MessageEnum`) que indica el resultado de
     * la autenticación.
     */
    public MessageEnum checkSignIn(Usuario usuario) {
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
                i = resultset.getString("email");
                if (i.equals(usuario.getEmail())) {
                    login = true;
                }
            }

            c = connection.getConnection();
            statement = c.prepareStatement(SELECTPASS);
            statement.setString(1, usuario.getPass());
            resultset = statement.executeQuery();

            if (resultset.next()) {
                i = resultset.getString("pass");
                if (i.equals(usuario.getPass())) {
                    pass = true;
                }
            }

            if (login == true && pass == true) {
                ORDER = MessageEnum.OK;
            }

            if (login == false || pass == false) {
                ORDER = MessageEnum.ERRORSIGNIN;
            }
            connection.saveConnection(c);
        } catch (SQLException ex) {

            connection.saveConnection(c);
            Logger.getLogger(DaoImplementation.class.getName()).log(Level.SEVERE, null, ex);
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
