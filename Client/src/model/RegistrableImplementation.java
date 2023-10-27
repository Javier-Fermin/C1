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
import src.AuthenticationException;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.User;
import src.UserAlreadyExistsException;

/*
 * 
 */

/**
 * The `RegistrableImplementation` class implements the `Registrable` interface
 * and provides methods for user registration and authentication in a client-server system.
 *
 * This class establishes a socket connection with a server and communicates with it to perform
 * user sign-in and sign-up operations. It handles exceptions related to the registration and
 * authentication process, including custom exceptions for server errors, user already exists,
 * authentication errors, and timeout issues.
 *
 * The class uses properties from a resource bundle to determine the server's IP address and port.
 *
 * @author Imanol
 * @version 1.0
 */
public class RegistrableImplementation implements Registrable{
    private final String PUERTO = ResourceBundle.getBundle("./resources/Properties.properties").getString("PORT");
    private final String IP = ResourceBundle.getBundle("./resources/Properties.properties").getString("IP");
    
     /**
     * Signs in a user by sending a sign-in request to the server and receiving a response.
     *
     * @param user The user to sign in.
     * @return The signed-in user.
     * @throws ServerErrorException If the server encounters an error during sign-in.
     * @throws AuthenticationException If authentication fails.
     * @throws TimeOutException If a timeout occurs during the operation.
     */
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
    
    /**
     * Signs up a new user by sending a sign-up request to the server and receiving a response.
     *
     * @param user The user to sign up.
     * @return The signed-up user.
     * @throws ServerErrorException If the server encounters an error during sign-up.
     * @throws UserAlreadyExistsException If the user already exists.
     * @throws TimeOutException If a timeout occurs during the operation.
     */
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
