/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;
import model.RegistrableFactory;
import server.Server;
import src.AuthenticationException;
import src.Message;
import src.MessageType;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.UserAlreadyExistsException;

/**
 * A thread that answer the client requests
 * 
 * @author Javier
 */
public class Worker extends Thread{
    /**
     * The RegistrableFactory used by the worker
     */
    private final RegistrableFactory factory;
    
    /**
     * The Socket used to communicate with the client
     */
    private Socket socket;
    /**
     * A Logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(Worker.class.getName());

    /**
     * Constructor of a worker thread
     * 
     * @param factory the RegistrableFactory to be used
     * @param socket the socket to communicate with the client
     */
    public Worker(RegistrableFactory factory, Socket socket) {
        this.factory = factory;
        this.socket = socket;
    }
    
    @Override
    public void run(){
        Message message = null;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try{
            //Getting the streams to communicate with the user
            LOGGER.info("Getting the streams of the client.");
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            //Read the message from the user
            LOGGER.info("Reading the message from the client.");
            message = (Message) ois.readObject();
            //Get a Registrable from the factory
            Registrable reg = factory.getRegistrable();
            //Switch the user's request
            LOGGER.info("Analyzing the request.");
            switch (message.getMessageType()){
                //In case of a SIGNIN_REQUEST
                case SIGNIN_REQUEST:
                        LOGGER.info("SignIn request recieved.");
                        //Call the signIn method from the Registrable
                        message.setUser(reg.signIn(message.getUser()));
                        //If there was no error it will be send a SUCCESS_RESPONSE
                        message.setMessageType(MessageType.SUCCESS_RESPONSE);
                    break;
                //In case of a SIGNIN_REQUEST
                case SIGNUP_REQUEST:
                        LOGGER.info("SignUp request recieved.");
                        //Call the signUp method from the Registrable
                        message.setUser(reg.signUp(message.getUser()));
                        //If there was no error it will be send a SUCCESS_RESPONSE
                        message.setMessageType(MessageType.SUCCESS_RESPONSE);
                    break;
            }
        //If there was any kind of exception it would be catched and then the
        //corresponding response would be sent
        } catch (ServerErrorException ex) {
            LOGGER.severe(ex.getMessage());
            message.setMessageType(MessageType.SERVER_ERROR_EXCEPTION_RESPONSE);
        } catch (UserAlreadyExistsException ex) {
            LOGGER.severe(ex.getMessage());
            message.setMessageType(MessageType.USER_ALREADY_EXISTS_EXCEPTION_RESPONSE);
        } catch (TimeOutException ex) {
            LOGGER.severe(ex.getMessage());
            message.setMessageType(MessageType.TIMEOUT_EXCEPTION_RESPONSE);
        } catch (AuthenticationException ex) {
            LOGGER.severe(ex.getMessage());
            message.setMessageType(MessageType.AUTHENTICATION_EXCEPTION_RESPONSE);
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            message.setMessageType(MessageType.SERVER_ERROR_EXCEPTION_RESPONSE);
        } catch (ClassNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
            message.setMessageType(MessageType.SERVER_ERROR_EXCEPTION_RESPONSE);
        }finally{
            //Finally we would sent the message to the user
            try {
                oos.writeObject(message);
                ois.close();
                oos.close();
                socket.close();
                Server.minusThread();
            } catch (IOException ex) {
                LOGGER.severe(ex.getMessage());
            }
        }
    }
}
