/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * A PoolErrorException is thrown whenever there is a problem related to the pool
 * of connections
 * 
 * @author Javier
 */
public class PoolErrorException extends Exception {
    /**
     * Creates a new instance of <code>PoolErrorException</code> without detail
     * message.
     */
    public PoolErrorException() {
    }

    /**
     * Constructs an instance of <code>PoolErrorException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PoolErrorException(String msg) {
        super(msg);
    }
}
