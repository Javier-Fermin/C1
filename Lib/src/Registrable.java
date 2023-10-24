/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This interface encapsules all the methods for the treatment of user data for
 * signUp and singIn
 * 
 * @author Javier, Fran, Imanol, Emil
 */
public interface Registrable {
    /**
     * This method should sign in a given user in the database
     * 
     * @param user the user to sign in
     * @return the user that has been signed in 
     * @throws ServerErrorException in case there is any problem with the server
     * @throws AuthenticationException in case the given user credentials are wrong
     * @throws TimeOutException in case there is no response from the server after certain time
     */
    public User signIn(User user) throws ServerErrorException,AuthenticationException,TimeOutException;
    
    /**
     * This method should sign up a user in the database
     * 
     * @param user the user to sign up
     * @return the user that has been signed up
     * @throws ServerErrorException in case there is any problem with the server
     * @throws UserAlreadyExistsException in case the given user already exists in the database
     * @throws TimeOutException in case there is no response from the server after certain time
     */
    public User signUp(User user) throws ServerErrorException,UserAlreadyExistsException,TimeOutException;
}
