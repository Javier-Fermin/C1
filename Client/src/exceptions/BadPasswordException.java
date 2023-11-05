/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 * This is an exception for the treatment of the user interface validations
 * 
 * @author Javier, Imanol
 */
public class BadPasswordException extends Exception{
    /**
     * Creates a new instance of <code>BadPasswordException</code> without detail
     * message.
     */
    public BadPasswordException() {
    }

    /**
     * Constructs an instance of <code>BadPasswordException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BadPasswordException(String msg) {
        super(msg);
    }
}

