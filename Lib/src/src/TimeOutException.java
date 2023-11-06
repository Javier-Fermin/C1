package src;

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
    /**
     * Creates a new instance of <code>TimeOutException</code> without detail
     * message.
     */
    public TimeOutException() {
    }

    /**
     * Constructs an instance of <code>TimeOutException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TimeOutException(String msg) {
        super(msg);
    }
}
