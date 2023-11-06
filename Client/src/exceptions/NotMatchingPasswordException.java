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
public class NotMatchingPasswordException extends Exception{
    /**
     * Creates a new instance of <code>NotMatchingPasswordException</code> without detail
     * message.
     */
    public NotMatchingPasswordException() {
    }

    /**
     * Constructs an instance of <code>NotMatchingPasswordException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotMatchingPasswordException(String msg) {
        super(msg);
    }
}


