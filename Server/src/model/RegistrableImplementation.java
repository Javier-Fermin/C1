package model;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author javie
 */
public class RegistrableImplementation implements Registrable{

    @Override
    public void SignIn(User user) throws ServerErrorException,AuthenticationException,TimeOutException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User SignUp(User user) throws ServerErrorException,UserAlreadyExistsException,TimeOutException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
