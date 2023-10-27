package src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This exception is thrwon when there is a problem in the server side
 * 
 * @author Javier, Fran, Imanol, Emil
 */
public class ServerErrorException extends Exception{
    public ServerErrorException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public ServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ServerErrorException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ServerErrorException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
}
