/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * An AuthenticationException should be thrown by the signIn method whenever the
 * credentials of the user are wrong
 * 
 * @author Javier, Fran, Emil, Imanol
 */
public class AuthenticationException extends Exception{
    public AuthenticationException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public AuthenticationException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
}
