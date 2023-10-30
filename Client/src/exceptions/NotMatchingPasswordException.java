/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author imape
 */
public class NotMatchingPasswordException extends Exception{
     public NotMatchingPasswordException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NotMatchingPasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public NotMatchingPasswordException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public NotMatchingPasswordException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public NotMatchingPasswordException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
}


