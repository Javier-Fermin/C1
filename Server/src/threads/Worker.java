/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.net.Socket;
import model.RegistrableFactory;
import src.Message;

/**
 *
 * @author javie
 */
public class Worker extends Thread{
    
    private final RegistrableFactory factory;
    private final Socket socket;
    private final Message message;

    public Worker(RegistrableFactory factory, Socket socket, Message message) {
        this.factory = factory;
        this.socket = socket;
        this.message = message;
    }
    
    @Override
    public void run(){
        switch (message.getMessageType()){
            case SIGNIN_REQUEST:
                break;
            case SIGNUP_REQUEST:
                break;
        }
    }
}
