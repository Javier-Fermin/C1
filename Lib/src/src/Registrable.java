package src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author javie
 */
public interface Registrable {
    public User SignIn(User user) throws ServerErrorException,AuthenticationException,TimeOutException;
    public User SignUp(User user) throws ServerErrorException,UserAlreadyExistsException,TimeOutException;
}