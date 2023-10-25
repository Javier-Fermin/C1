package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.Pool;

import src.AuthenticationException;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.User;
import src.UserAlreadyExistsException;

/**
 *
 * @author javie
 */
public class RegistrableImplementation implements Registrable {

    // DB Connection
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rset;
    private Pool poolConnections;

    //Postgres sentences that we are going to use
    private final String signInStmt = "SELECT P.name, U.password, P.phone, U.login, P.zip ,P.city ,P.street "
            + "FROM public.res_partner P, public.res_users U "
            + "WHERE U.partner_id=P.id AND U.login=? AND U.password=?";

    private final String insertPartnerStmt = "INSERT INTO public.res_partner"
            + "(company_id, name, street, zip, city, email, phone, active) "
            + "VALUES ('1', ?, ?, ?, ?, ?, ?, true)";

    private final String getPartnerIdStmt = "SELECT id "
            + "FROM public.res_partner "
            + "WHERE email=?";

    private final String insertUserStmt = "INSERT INTO public.res_users "
            + "(company_id, partner_id, active, login, password) "
            + "VALUES ('1', ?, True, ?, ?)";

    private final String getUserIdStmt = "SELECT id "
            + "FROM public.res_users "
            + "WHERE partner_id=?";

    private final String insertRelUserCompanyStmt = "INSERT INTO public.res_company_users_rel "
            + "(cid, user_id) "
            + "VALUES ('1', ?)";

    public RegistrableImplementation(Pool poolConnections) {
        this.poolConnections = poolConnections;
    }

    @Override
    public User SignIn(User user) throws ServerErrorException, AuthenticationException, TimeOutException {
        //Instanciamos los objetos necesarios(Connection,PreparedStatement,User,Pool...)
        try {
            /**
             * DESCOMENTAR
             *
             * get connection from pool
             */
            //con = poolConnections.openConnection();
            //connection savepoint
            con.setSavepoint();
            con.setAutoCommit(false);
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.INFO, null, "Open connection");
            //Find user if already exist

            pstmt = con.prepareStatement(getPartnerIdStmt);
            pstmt.setString(1, user.getEmail());
            rset = pstmt.executeQuery();
            if (rset.next()) {
                //Connection rollback
                con.rollback();
                throw new ServerErrorException();
            } else {
                //Try to sign up into server
                pstmt = con.prepareStatement(signInStmt);
                pstmt.setString(1, user.getEmail());
                pstmt.setString(2, user.getPasswd());
                rset = pstmt.executeQuery();
                if (!rset.next()) {
                    //Connection rollback
                    con.rollback();
                    throw new AuthenticationException();
                } else {
                    //Insert data to the user
                    user = new User(rset.getString(1), rset.getString(2), rset.getString(3), rset.getString(4), rset.getString(5));
                }
            }
            //Return Connetion to pool
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.INFO, null, "Close connection and commit connection");
            con.commit();
            /**
             * DESCOMENTAR pool.returnConnection(con);
             */
        } catch (AuthenticationException ae) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, "Authentication error" + ae.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, "SQL error" + ex.getMessage());
        } catch (ServerErrorException se) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, "Server Error" + se.getMessage());
        }

        return user;
    }

    @Override
    public User SignUp(User user) throws ServerErrorException, UserAlreadyExistsException, TimeOutException {
        return null;
        //Se llama al pool y nos conectamos con la BD

    }

}
