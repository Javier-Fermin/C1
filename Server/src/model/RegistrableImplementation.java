package model;

import src.AuthenticationException;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.User;
import src.UserAlreadyExistsException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
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
 * @author Fran
 */
public class RegistrableImplementation implements Registrable {
// --- RegistrableImplementation Attributes ---

    // DB Connection
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rset;
    private Pool poolConnections = new Pool();

    //Postgres sentences that we are going to use
    private final String signInStmt = "SELECT P.name, U.password, P.phone, U.login, P.zip ,P.city ,P.street "
            + "FROM public.res_partner P, public.res_users U "
            + "WHERE U.partner_id=P.id AND U.login=? AND U.password=?";

    private final String getUserStmt = "SELECT login "
            + "FROM public.res_users "
            + "WHERE login=?";

    private final String insertPartnerStmt = "INSERT INTO public.res_partner"
            + "(company_id, name, street, zip, email, phone, active) "
            + "VALUES ('1', ?, ?, ?, ?, ?, true)";

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

    //User object
    private User us;

// --- Main RegistrableImplementation Methods ---
    @Override
    public User SignIn(User user) throws ServerErrorException, AuthenticationException, TimeOutException {
        //Instanciamos los objetos necesarios(Connection,PreparedStatement,User,Pool...)
        us = new User();

        //Se llama al pool y nos conectamos a la BD de odoo-postgresql
        //
        return us;
    }

    /**
     * *
     * This method get a User object and insert it inside multiple tables inside
     * the PostgreSQL database, using various methods in the process and a Pool
     * object to make the connection with the database.
     *
     * @param user
     * @return User
     * @throws ServerErrorException
     * @throws UserAlreadyExistsException
     * @throws TimeOutException
     */
    @Override
    public User SignUp(User user) throws ServerErrorException, UserAlreadyExistsException, TimeOutException {
        //Se llama al pool y nos conectamos con la BD usando a con
        try {
            //con=poolConnections.openConnection();
            con = DriverManager.getConnection("", "", "");

            //Check if the user isn't registered in the db
            if (!isUserRegistered(user, con)) {
                //Execute all the needed methods to insert all the data inside of the required postgresql tables
                insertPartner(user, con);

                insertUser(user, getPartnerId(user, con), con);

                insertRelationUserCompany(user, getUserId(getPartnerId(user, con), con), con);

            } else {
                throw new UserAlreadyExistsException();
            }
        } catch (SQLException e) {
            throw new ServerErrorException();
        } finally {
            //If nothing went wrong, we close the connection with the DB
            try {
                con.close();
            } catch (SQLException ex) {
                throw new ServerErrorException();
            }
        }

        return us;
    }

// --- SIGN UP METHODS ---
    
    /***
     * This method used by the SignUp method inserts some attributes of the 
     * recieved User object inside the database's res_partner table.
     * @param u
     * @param c 
     */
    
    private void insertPartner(User u, Connection c) {
        /***
         * Split the users address in two: zip and street
        */
        String[] addressU = u.getAddress().split(",");
        String zipU = addressU[0];
        String streetU = addressU[1];

        try {
            /***
             * Set the PreparedStatement's statement, the values that it will 
             * use. Then we execute it, catching any possible exception. 
             * Finally, we close the object.
             */
            pstmt = c.prepareStatement(insertPartnerStmt);
            pstmt.setString(0, u.getName());
            pstmt.setString(1, streetU);
            pstmt.setString(2, zipU);
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPhone());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /***
     * This method used by the insertUser method to obtain the id of the partner
     * of the res_partner table that has the same email of our User to login.
     * If it is found, the method return the partner's id.
     * @param u
     * @param c
     * @return int
     * @throws ServerErrorException 
     */
    
    private int getPartnerId(User u, Connection c) throws ServerErrorException {
        int idP = 0;
        try {
            pstmt = c.prepareStatement(getPartnerIdStmt);
            pstmt.setString(0, u.getEmail());

            rset = pstmt.executeQuery();
            idP = rset.getInt("id");
        } catch (SQLException ex) {
            throw new ServerErrorException();
        } finally {
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServerErrorException();
            }
        }

        return idP;
    }

    /***
     * This method used by the SignUp method inserts some attributes of the 
     * recieved User object and the partner's id, that's sent by the 
     * getPartnerId method, inside the database's res_users table.
     * @param user
     * @param partnerId
     * @param con 
     * @throws ServerErrorException
     */
    
    private void insertUser(User user, int partnerId, Connection con) throws ServerErrorException {
        try {
            pstmt = con.prepareStatement(insertPartnerStmt);

            pstmt.setInt(0, partnerId);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPasswd());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServerErrorException();
            }
        }
    }

    /***
     * This method used by the insertRelationUserCompany method to obtain the id
     * of the user of the res_users table that has the same partner_id of our 
     * User. If it is found, the method return the user's id.
     * @param partnerId
     * @param con
     * @return
     * @throws ServerErrorException 
     */
    
    private int getUserId(int partnerId, Connection con) throws ServerErrorException {
        int userId = 0;

        try {
            pstmt = con.prepareStatement(getUserIdStmt);
            pstmt.setInt(0, partnerId);

            rset = pstmt.executeQuery();
            userId = rset.getInt("id");
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException();
        } finally {
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServerErrorException();
            }
        }

        return userId;
    }

    /***
     * This method used by the SignUp method inserts the company's id and the  
     * id of the User object and, that's sent by the getUserId method, 
     * inside the database's res_company_users_rel table.
     * @param user
     * @param userId
     * @param con
     * @throws ServerErrorException 
     */
    
    private void insertRelationUserCompany(User user, int userId, Connection con) throws ServerErrorException {
        try {
            pstmt = con.prepareStatement(insertRelUserCompanyStmt);

            pstmt.setInt(0, userId);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException();
        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServerErrorException();
            }
        }
    }

    /***
     * This method used by the SignUp method checks if the email attribute  
     * doesn't belongs to any user saved inside the database's res_users table.
     * If it does, the method returns a true boolean object, and if doesn't the
     * method returns it as false.
     * @param user
     * @param con
     * @return
     * @throws ServerErrorException 
     */
    
    private boolean isUserRegistered(User user, Connection con) throws ServerErrorException {
        boolean userRegistered = false;
        try {
            pstmt = con.prepareStatement(getUserStmt);
            pstmt.setString(0, user.getEmail());

            rset = pstmt.executeQuery();
            if (!rset.getString("login").isEmpty()) {
                userRegistered = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException();
        } finally {
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServerErrorException();
            }
        }
        return userRegistered;
    }

}
