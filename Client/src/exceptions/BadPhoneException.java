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
public class BadPhoneException extends Exception{
     public BadPhoneException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public BadPhoneException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public BadPhoneException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public BadPhoneException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public BadPhoneException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
}

