package model;

import exceptions.PoolErrorException;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
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
// --- RegistrableImplementation Attributes ---

    // DB Connection
    private Connection con;
    private PreparedStatement pstmt;
    private ResultSet rset;
    private Pool pool;

    //Postgres sentences that we are going to use
    private final String signInStmt = "SELECT P.name, U.password, P.phone, U.login, P.zip ,P.city ,P.street "
            + "FROM public.res_partner P, public.res_users U "
            + "WHERE U.partner_id=P.id AND U.login=? AND U.password=?";

    private final String getUserStmt = "SELECT login FROM public.res_users WHERE login=?";

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

    private final String insertRelUserGroupStmt = "INSERT INTO public.res_groups_users_rel "
            + "(gid, uid) "
            + "VALUES ('1', ?),('7', ?),('8', ?),('9', ?);";
    
    private User us;

    public RegistrableImplementation(Pool poolConnections) {
        this.pool = poolConnections;
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
    public User signIn(User user) throws ServerErrorException, AuthenticationException, TimeOutException {
        //Instanciamos los objetos necesarios(Connection,PreparedStatement,User,Pool...)
        try {
            con = pool.openConnection();
            //Find user if already exist

            //Try to sign in into server
            pstmt = con.prepareStatement(signInStmt);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPasswd());
            rset = pstmt.executeQuery();
            if (!rset.next()) {
                throw new AuthenticationException();
            } else {
                //Insert data to the user
                user = new User(rset.getString("name"), rset.getString("password"), rset.getString("phone"), rset.getString("login"), null);
            }
            
            //Return Connetion to pool
            pool.closeConnection(con);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, "SQL error" + ex.getMessage());
        } catch (PoolErrorException ex) {
            ex.printStackTrace();
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
        return user;
    }

    @Override
    public User signUp(User user) throws ServerErrorException, UserAlreadyExistsException, TimeOutException {
        //Se llama al pool y nos conectamos con la BD usando a con
        try {
            con=pool.openConnection();
            con.setAutoCommit(false);
            con.setSavepoint();
            //Check if the user isn't registered in the db
            if (!isUserRegistered(user, con)) {
                //Execute all the needed methods to insert all the data inside of the required postgresql tables
                insertPartner(user, con);

                insertUser(user, getPartnerId(user, con), con);

                insertRelationUserCompany(user, getUserId(getPartnerId(user, con), con), con);
                
                insertRelationUserGroups(getUserId(getPartnerId(user, con), con), con);
                
                con.commit();
            } else {
                throw new UserAlreadyExistsException();
            }
        } catch (PoolErrorException e) {
            throw new ServerErrorException();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void insertPartner(User u, Connection c){
        /***
         * Split the users address in two: zip and street
        */
        String[] addressU = u.getAddress().split(" ");
        String zipU = addressU[0];
        String streetU = addressU[1];

        try {
            /***
             * Set the PreparedStatement's statement, the values that it will 
             * use. Then we execute it, catching any possible exception. 
             * Finally, we close the object.
             */
            pstmt = c.prepareStatement(insertPartnerStmt);
            pstmt.setString(1, u.getName());
            pstmt.setString(2, streetU);
            pstmt.setString(3, zipU);
            pstmt.setString(4, u.getEmail());
            pstmt.setString(5, u.getPhone());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
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
            pstmt.setString(1, u.getEmail());

            rset = pstmt.executeQuery();
            while(rset.next()){
                idP = rset.getInt("id");
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex1);
            }
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
            pstmt = con.prepareStatement(insertUserStmt);

            pstmt.setInt(1, partnerId);
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswd());

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex1);
            }
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
            pstmt.setInt(1, partnerId);
            
            rset = pstmt.executeQuery();
            while(rset.next()){
                userId = rset.getInt("id");
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException();
        } finally {
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
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

            pstmt.setInt(1, userId);

            pstmt.executeUpdate();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex1);
            }
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
     * This method used by the SignUp method inserts the company's id and the  
     * id of the User object and, that's sent by the getUserId method, 
     * inside the database's res_company_users_rel table.
     * @param user
     * @param userId
     * @param con
     * @throws ServerErrorException 
     */
    
    private void insertRelationUserGroups(int userId, Connection con) throws ServerErrorException {
        try {
            pstmt = con.prepareStatement(insertRelUserGroupStmt);
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            pstmt.setInt(4, userId);
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex1);
            }
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
            pstmt.setString(1, user.getEmail());

            rset = pstmt.executeQuery();
            while(rset.next()){
                if (!rset.getString("login").isEmpty()) {
                    userRegistered = true;
                }
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServerErrorException();
        } finally {
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServerErrorException();
            }
        }
        return userRegistered;
    }

}
