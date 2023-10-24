/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * A TimeOutException should be thrown when there is no response after some time
 * from the server
 * 
 * @author Javier, Imanol, Emil, Fran
 */
public class TimeOutException extends Exception{
    public TimeOutException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public TimeOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public TimeOutException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public TimeOutException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
    
}
