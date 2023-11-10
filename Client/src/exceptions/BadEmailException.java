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
public class BadEmailException extends Exception{
    /**
     * Creates a new instance of <code>BadEmailException</code> without detail
     * message.
     */
    public BadEmailException() {
    }

    /**
     * Constructs an instance of <code>BadEmailException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public BadEmailException(String msg) {
        super(msg);
    }
}

