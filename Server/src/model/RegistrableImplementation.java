package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    private final String insertPartnerStmt = "INSERT INTO public.res_partner" 
                                            +"(company_id, name, street, zip, city, email, phone, active) " 
                                            +"VALUES ('1', ?, ?, ?, ?, ?, ?, true)";
    
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
    
    @Override
    public User SignIn(User user) throws ServerErrorException,AuthenticationException,TimeOutException{
        //Instanciamos los objetos necesarios(Connection,PreparedStatement,User,Pool...)
        User us=new User();
        
        //Se llama al pool y nos conectamos a la BD de odoo-postgresql
        
        //
        
        
        
        return us;
    }

    @Override
    public User SignUp(User user) throws ServerErrorException,UserAlreadyExistsException,TimeOutException{
        //Instanciar objectos necesarios
        
        //Se llama al pool y nos conectamos con la BD
        
        
    }
    
    public void setPool
    
}
