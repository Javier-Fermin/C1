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
public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UserAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public UserAlreadyExistsException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
}
