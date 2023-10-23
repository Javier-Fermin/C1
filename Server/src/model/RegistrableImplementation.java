package model;

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
 * @author javie
 */
public class RegistrableImplementation implements Registrable{

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
                                            +"(company_id, name, street, zip, email, phone, active) " 
                                            +"VALUES ('1', ?, ?, ?, ?, ?, true)";
    
    private final String getPartnerIdStmt = "SELECT id "
                                           + "FROM public.res_partner "
                                           + "WHERE email=?";
    
    private final String insertUserStmt = "INSERT INTO public.res_users " 
                                         +"(company_id, partner_id, active, login, password) " 
                                        +"VALUES ('1', ?, True, ?, ?)";
    
    private final String getUserIdStmt = "SELECT id "
                                        + "FROM public.res_users "
                                        + "WHERE partner_id=?";
    
    private final String insertRelUserCompanyStmt = "INSERT INTO public.res_company_users_rel " 
                                                    +"(cid, user_id) " 
                                                    +"VALUES ('1', ?)";
    
    //User object
    private User us;
    
    @Override
    public User SignIn(User user) throws ServerErrorException,AuthenticationException,TimeOutException{
        //Instanciamos los objetos necesarios(Connection,PreparedStatement,User,Pool...)
        us=new User();
        
        //Se llama al pool y nos conectamos a la BD de odoo-postgresql
        
        //
        
        
        
        return us;
    }

    @Override
    public User SignUp(User user) throws ServerErrorException,UserAlreadyExistsException,TimeOutException{
        //Se llama al pool y nos conectamos con la BD usando a con
        try{
        //con=poolConnections.openConnection();
            con=DriverManager.getConnection("", "", "");
            
         //Check if the user isn't registered in the db
            if(!isUserRegistered(user,con)){
            //Execute all the needed methods to insert all the data inside of the required postgresql tables
                insertPartner(user,con);
            
                insertUser(user,getPartnerId(user,con),con);
            
                insertRelationUserCompany(user,getUserId(getPartnerId(user,con),con),con);
            
            }else{
                throw new UserAlreadyExistsException();
            }
            
        }catch(SQLException e){
        
        }finally{
            //If nothing went wrong, we close the connection with the DB
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return us;
    }
    
// --- SIGN UP METHODS ---
    
    
    private void insertPartner(User u, Connection c){
            String[] addressU=u.getAddress().split(",");
            String zipU=addressU[0];
            String streetU=addressU[1];
            
        try {
            pstmt = c.prepareStatement(insertPartnerStmt);
            pstmt.setString(0, u.getName());
            pstmt.setString(1, streetU);
            pstmt.setString(2, zipU);
            pstmt.setString(3, u.getEmail());
            pstmt.setString(4, u.getPhone());
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
        }    
    }
    private int getPartnerId(User u, Connection c){
        int idP=0;
        try {
            pstmt = c.prepareStatement(getPartnerIdStmt);
            pstmt.setString(0, u.getEmail());
            
            rset=pstmt.executeQuery();
            idP=rset.getInt("id");
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return idP;
    }

    private void insertUser(User user, int partnerId, Connection con) {
        try {
            pstmt = con.prepareStatement(insertPartnerStmt);
            
            pstmt.setInt(0, partnerId);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPasswd());
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    
    private int getUserId(int partnerId, Connection con){
        int userId=0;
        
        try {
            pstmt = con.prepareStatement(getUserIdStmt);
            pstmt.setInt(0, partnerId);
            
            rset=pstmt.executeQuery();
            userId=rset.getInt("id");
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return userId;
    }

    private void insertRelationUserCompany(User user, int userId, Connection con) {
        try {
            pstmt = con.prepareStatement(insertRelUserCompanyStmt);
            
            pstmt.setInt(0, userId);
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }

    private boolean isUserRegistered(User user, Connection con) {
        boolean userRegistered=false;
        try {
            pstmt = con.prepareStatement(getUserStmt);
            pstmt.setString(0, user.getEmail());
            
            rset=pstmt.executeQuery();
            if(!rset.getString("login").isEmpty()){
                userRegistered=true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                rset.close();
                pstmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return userRegistered;
    }
    
}
