package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import src.AuthenticationException;
import src.Message;
import src.MessageType;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.User;
import src.UserAlreadyExistsException;

/*
 * 
 */

/**
 *
 * @author Imanol
 */
public class RegistrableImplementation implements Registrable{
    private final int PUERTO = 5000;
    private final String IP = "127.0.0.1";
    
    @Override
    public User SignIn(User user) throws ServerErrorException,AuthenticationException,TimeOutException{
        Socket client = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            //creates client connection
            client = new Socket(IP, PUERTO);
            //open writing and reading stream
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
            //make pdu with the user and the msg type
            Message msg = new Message(user, MessageType.SIGNIN_REQUEST);
            
            oos.writeObject(msg);
            
            ois.re
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (client != null)
                    client.close();
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Fin cliente");
        }
        
        return user;
    }

    @Override
    public User SignUp(User user) throws ServerErrorException,UserAlreadyExistsException,TimeOutException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
