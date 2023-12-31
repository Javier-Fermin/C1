package model;

import exceptions.PoolErrorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.AuthenticationException;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.User;
import src.UserAlreadyExistsException;

/**
 * This is an implementation of the Registrable interface
 * 
 * @author Fran, Emil
 */
public class RegistrableImplementation implements Registrable {
// --- RegistrableImplementation Attributes ---

    // DB Connection
    /**
     * A connection to the database
     */
    private Connection con;
    
    /**
     * A PreparedStatement for the query execution
     */
    private PreparedStatement pstmt;
    
    /**
     * A ResultSet for the queries results
     */
    private ResultSet rset;
    
    /**
     * The connections pool
     */
    private static Poolable pool;
    
    /**
     * A Logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(RegistrableImplementation.class.getName());
    
    //Postgres sentences that we are going to use
    /**
     * A query to get a user's info
     */
    private final String signInStmt = "SELECT P.name, U.password, P.phone, U.login, P.zip ,P.city ,P.street "
            + "FROM public.res_partner P, public.res_users U "
            + "WHERE U.partner_id=P.id AND U.login=? AND U.password=?";

    /**
     * A query to search a user by the login
     */
    private final String getUserStmt = "SELECT login FROM public.res_users WHERE login=?";

    /**
     * A query to insert a partner
     */
    private final String insertPartnerStmt = "INSERT INTO public.res_partner"
            + "(company_id, name, street, zip, email, phone, active) "
            + "VALUES ('1', ?, ?, ?, ?, ?, true)";

    /**
     * A query to insert a user
     */
    private final String insertUserStmt = "INSERT INTO public.res_users "
            + "(company_id, partner_id, active, login, password) "
            + "VALUES ('1', ?, True, ?, ?)";

    /**
     * A query to insert the relation of the user and the company
     */
    private final String insertRelUserCompanyStmt = "INSERT INTO public.res_company_users_rel "
            + "(cid, user_id) "
            + "VALUES ('1', ?)";

    /**
     * A query to insert the relation of the user and the group
     */
    private final String insertRelUserGroupStmt = "INSERT INTO public.res_groups_users_rel "
            + "(gid, uid) "
            + "VALUES ('1', ?),('7', ?),('8', ?),('9', ?);";
    
    public RegistrableImplementation() {
        //If the pool is null it is created
        if(pool == null){
            pool = new Pool();
        }
    }
    
    /**
     * This method get a User object and insert it inside multiple tables inside
     * the PostgreSQL database, using various methods in the process and a Pool
     * object to make the connection with the database.
     *
     * @param user the usert to sign in
     * @return User the user that has been signed in
     * @throws ServerErrorException in case there is any problem during the sign in
     * @throws TimeOutException in case the sign in takes more than a specified time
     */
    @Override
    public User signIn(User user) throws ServerErrorException, AuthenticationException, TimeOutException {
        //Instanciamos los objetos necesarios(Connection,PreparedStatement,User,Pool...)
        LOGGER.info("Signing in a user.");
        try {
            LOGGER.info("Requesting a connection to the pool.");
            con = pool.openGetConnection();

            //Try to sign in into server
            LOGGER.info("Preparing statement signInStmt to be executed.");
            pstmt = con.prepareStatement(signInStmt);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPasswd());
            pstmt.setQueryTimeout(10);
            LOGGER.info("Executing query");
            rset = pstmt.executeQuery();
            if (!rset.next()) {
                LOGGER.info("Wrong credentials for the user or the user may not exist.");
                throw new AuthenticationException("The credentials for the user are wrong or the user may not exist.");
            } else {
                //Insert data to the user
                user = new User(rset.getString("name"), rset.getString("password"), rset.getString("phone"), rset.getString("login"), null);
                LOGGER.info("User found successfully.");
            }
        } catch (SQLTimeoutException ex) {
            LOGGER.severe(ex.getMessage());
            throw new TimeOutException();
        } catch (SQLException ex) {
            LOGGER.severe(ex.getMessage());
        } catch (PoolErrorException ex) {
            LOGGER.severe(ex.getMessage());
            throw new ServerErrorException();
        }finally{
            //Return Connetion to pool
            if(con!=null){
                LOGGER.info("Returning the connection to the pool.");
                pool.returnConnection(con);
            }
        }
        return user;
    }

    /**
     * Signs up a new user by sending a sign-up request to the server and
     * receiving a response.
     *
     * @param user The user to sign up.
     * @return The signed-up user.
     * @throws ServerErrorException If the server encounters an error during
     * sign-up.
     * @throws UserAlreadyExistsException If the user already exists.
     * @throws TimeOutException If a timeout occurs during the operation.
     */
    @Override
    public User signUp(User user) throws ServerErrorException, UserAlreadyExistsException, TimeOutException {
        LOGGER.info("Signing up a user.");
        try {
            //Connects to the database using the Pool to obtain an open 
            //connection
            LOGGER.info("Requesting a connection to the pool.");
            con=pool.openGetConnection();
            con.setAutoCommit(false);
            con.setSavepoint();
            //Check if the user isn't registered in the dbL
            LOGGER.info("Checking if the user exists.");
            if (!isUserRegistered(user, con)) {
                //Execute all the needed methods to insert all the data inside of the required postgresql tables
                LOGGER.info("Inserting the partner for the user.");
                int idP=insertPartner(user, con);
                LOGGER.info("Inserting the user.");
                int idU=insertUser(user, idP, con);
                LOGGER.info("Inserting the relations of the user and the company.");
                insertRelationUserCompany(user, idU, con);
                LOGGER.info("Inserting the relations of the user and the groups.");
                insertRelationUserGroups(idU, con);
                con.commit();
            } else {
                LOGGER.severe("User already exists.");
                throw new UserAlreadyExistsException();
            }
        } catch (PoolErrorException e) {
            LOGGER.severe(e.getMessage());
            throw new ServerErrorException();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                LOGGER.severe(ex1.getMessage());
            }
            LOGGER.severe(ex.getMessage());
        } finally {
            //Return Connetion to pool
            pool.returnConnection(con);
        }
        return user;
    }

// --- SIGN UP METHODS ---
    
    /**
     * This method used by the SignUp method inserts some attributes of the 
     * recieved User object inside the database's res_partner table.
     * 
     * @param u the user with all the data for the partner
     * @param c the connection to the database
     */
    private int insertPartner(User u, Connection c) throws SQLException{
        int id=0;
        /*
         * Split the users address in two: zip and street
         */
        String zipU = null;
        String streetU = null;
        if(!u.getAddress().isEmpty()){
            String[] addressU = u.getAddress().split(" ");
            zipU = addressU[0];
            streetU = addressU[1];
        }
        /*
         * Set the PreparedStatement's statement, the values that it will 
         * use. Then we execute it, catching any possible exception. 
         * Finally, we close the object.
         */
        LOGGER.info("Preparing statement insertPartnerStmt to be executed.");
        pstmt = c.prepareStatement(insertPartnerStmt,RETURN_GENERATED_KEYS);
        pstmt.setString(1, u.getName());
        pstmt.setString(2, streetU);
        pstmt.setString(3, zipU);
        pstmt.setString(4, u.getEmail());
        pstmt.setString(5, u.getPhone());
        pstmt.setQueryTimeout(10);
        LOGGER.info("Executing query.");
        pstmt.executeUpdate();
        
        rset=pstmt.getGeneratedKeys();
        if(rset.next()){
            id=rset.getInt(1);
        }
        rset.close();
        pstmt.close();     
        
        return id;
        
    }

    /**
     * This method used by the SignUp method inserts some attributes of the 
     * recieved User object and the partner's id, that's sent by the 
     * getPartnerId method, inside the database's res_users table.
     * 
     * @param user the user with all the data needed
     * @param partnerId the partner id of the user
     * @param con the connection to the database
     * @throws ServerErrorException in case there is any problem during the query
     */
    private int insertUser(User user, int partnerId, Connection con) throws ServerErrorException, SQLException {
         int id=0;
        
        LOGGER.info("Preparing statement insertUserStmt to be executed.");
        pstmt = con.prepareStatement(insertUserStmt,RETURN_GENERATED_KEYS);
        pstmt.setInt(1, partnerId);
        pstmt.setString(2, user.getEmail());
        pstmt.setString(3, user.getPasswd());
        pstmt.setQueryTimeout(10);
        LOGGER.info("Executing query.");
        pstmt.executeUpdate();
        
        rset=pstmt.getGeneratedKeys();    
        if(rset.next()){
            id=rset.getInt(1);
        }
        
        rset.close();
        pstmt.close();
        
        return id;
    }

    /**
     * This method used by the SignUp method inserts the company's id and the  
     * id of the User object and, that's sent by the getUserId method, 
     * inside the database's res_company_users_rel table.
     * 
     * @param user
     * @param userId
     * @param con the connection to the database
     * @throws ServerErrorException in case there is any problem during the query
     */
    private void insertRelationUserCompany(User user, int userId, Connection con) throws ServerErrorException, SQLException {
        
        LOGGER.info("Preparing statement insertRelUserCompanyStmt to be executed.");
        pstmt = con.prepareStatement(insertRelUserCompanyStmt);
        pstmt.setInt(1, userId);
        pstmt.setQueryTimeout(10);
        LOGGER.info("Executing query.");
        pstmt.executeUpdate();
        pstmt.close();
           
    }
    
    /**
     * This method used by the SignUp method inserts the company's id and the  
     * id of the User object and, that's sent by the getUserId method, 
     * inside the database's res_company_users_rel table.
     * 
     * @param user
     * @param userId
     * @param con
     * @throws ServerErrorException in case there is any problem during the query
     */    
    private void insertRelationUserGroups(int userId, Connection con) throws ServerErrorException, SQLException {
        LOGGER.info("Preparing statement insertRelUserGroupStmt to be executed.");
        pstmt = con.prepareStatement(insertRelUserGroupStmt);
        pstmt.setInt(1, userId);
        pstmt.setInt(2, userId);
        pstmt.setInt(3, userId);
        pstmt.setInt(4, userId);
        pstmt.setQueryTimeout(10);
        LOGGER.info("Executing query.");
        pstmt.executeUpdate();
        pstmt.close();
    }

    /**
     * This method used by the SignUp method checks if the email attribute  
     * doesn't belongs to any user saved inside the database's res_users table.
     * If it does, the method returns a true boolean object, and if doesn't the
     * method returns it as false.
     * @param user
     * @param con
     * @return
     * @throws ServerErrorException 
     */
    private boolean isUserRegistered(User user, Connection con) throws ServerErrorException, SQLException {
        boolean userRegistered = false;
        
        LOGGER.info("Preparing statement getUsetStmt to be executed.");
        pstmt = con.prepareStatement(getUserStmt);
        pstmt.setString(1, user.getEmail());
        pstmt.setQueryTimeout(10);
        LOGGER.info("Executing query.");
        rset = pstmt.executeQuery();
        while(rset.next()){
            LOGGER.info("User faound.");
            if (!rset.getString("login").isEmpty()) {
                userRegistered = true;
            }
        }
        rset.close();
        pstmt.close();
           
        return userRegistered;
    }

}
