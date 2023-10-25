package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ResourceBundle;
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
    private final String PUERTO = ResourceBundle.getBundle("./resources/Properties.properties").getString("PORT");
    private final String IP = ResourceBundle.getBundle("./resources/Properties.properties").getString("IP");
    
    @Override
    public User SignIn(User user) throws ServerErrorException,AuthenticationException,TimeOutException{
        Socket client = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            //creates client connection
            client = new Socket(IP, Integer.parseInt(PUERTO));
            //open writing and reading stream
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
            //make pdu with the user and the msg type
            Message msg = new Message(user, MessageType.SIGNIN_REQUEST);
            
            oos.writeObject(msg);
            
            msg = (Message) ois.readObject();
            
            switch(msg.getMessageType()){
                
                case SUCCESS_RESPONSE:
                    
                case SERVER_ERROR_EXCEPTION_RESPONSE:
                    throw new ServerErrorException();   
            }
        } catch (IOException e) {
            
        } catch (Exception e) {
            
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
            
        }
        
        return user;
    }

    @Override
    public User SignUp(User user) throws ServerErrorException,UserAlreadyExistsException,TimeOutException{
        Socket client = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try {
            //creates client connection
            client = new Socket(IP, Integer.parseInt(PUERTO));
            //open writing and reading stream
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
            //make pdu with the user and the msg type
            Message msg = new Message(user, MessageType.SIGNUP_REQUEST);
            
            oos.writeObject(msg);
            
            msg = (Message) ois.readObject();
            
            switch(msg.getMessageType()){
                
                case USER_ALREADY_EXISTS_EXCEPTION_RESPONSE:
                    throw new UserAlreadyExistsException();
                case SUCCESS_RESPONSE:
                    
                case SERVER_ERROR_EXCEPTION_RESPONSE:
                    throw new ServerErrorException();   
            }
        } catch (IOException e) {
            
        } catch (Exception e) {
            
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
            
        }
        
        return user;
    }
    
    
}
