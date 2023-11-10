package src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * An AuthenticationException should be thrown by the signIn method whenever the
 * credentials of the user are wrong
 * 
 * @author Javier, Fran, Emil, Imanol
 */
public class AuthenticationException extends Exception{
    /**
     * Creates a new instance of <code>AuthenticationException</code> without detail
     * message.
     */
    public AuthenticationException() {
    }

    /**
     * Constructs an instance of <code>AuthenticationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public AuthenticationException(String msg) {
        super(msg);
    }
}
