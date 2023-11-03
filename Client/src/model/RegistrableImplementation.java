package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * and provides methods for user registration and authentication in a
 * client-server system.
 *
 * This class establishes a socket connection with a server and communicates
 * with it to perform user sign-in and sign-up operations. It handles exceptions
 * related to the registration and authentication process, including custom
 * exceptions for server errors, user already exists, authentication errors, and
 * timeout issues.
 *
 * The class uses properties from a resource bundle to determine the server's IP
 * address and port.
 *
 * @author Imanol
 * @version 1.0
 */
public class RegistrableImplementation implements Registrable {

    private final String PUERTO = ResourceBundle.getBundle("resources.Client").getString("PORT");
    private final String IP = ResourceBundle.getBundle("resources.Client").getString("IP");
    private SocketAddress socketAddress = new InetSocketAddress(IP, Integer.parseInt(PUERTO));

    private Socket client = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    /**
     * Signs in a user by sending a sign-in request to the server and receiving
     * a response.
     *
     * @param user The user to sign in.
     * @return The signed-in user.
     * @throws ServerErrorException If the server encounters an error during
     * sign-in.
     * @throws AuthenticationException If authentication fails.
     * @throws TimeOutException If a timeout occurs during the operation.
     */
    @Override
    public User signIn(User user) throws ServerErrorException, AuthenticationException, TimeOutException {
        try {
            Message msg = connectAndSendMessage(user, MessageType.SIGNIN_REQUEST);
            switch (msg.getMessageType()) {
                case SUCCESS_RESPONSE:
                    return msg.getUser();
                case SERVER_ERROR_EXCEPTION_RESPONSE:
                    throw new ServerErrorException();
                case AUTHENTICATION_EXCEPTION_RESPONSE:
                    throw new AuthenticationException();
                case TIMEOUT_EXCEPTION_RESPONSE:
                    throw new TimeOutException();
            }
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * Signs up a new user by sending a sign-up request to the server and
     * receiving a response.
     *
     * @param user The user to sign up.
     * @return The signed-up user.
     * @throws ServerErrorException If the server encounters an error during
     * sign-up.
     * @throws UserAlreadyExistsException If the user already exists.
     * @throws TimeOutException If a timeout occurs during the operation.
     */
    @Override
    public User signUp(User user) throws ServerErrorException, UserAlreadyExistsException, TimeOutException {
        try {
            Message msg = connectAndSendMessage(user, MessageType.SIGNUP_REQUEST);
            switch (msg.getMessageType()) {
                case SUCCESS_RESPONSE:
                    return msg.getUser();
                case USER_ALREADY_EXISTS_EXCEPTION_RESPONSE:
                    throw new UserAlreadyExistsException("The user already exists.");
                case SERVER_ERROR_EXCEPTION_RESPONSE:
                    throw new ServerErrorException("An unexpected error occur.");
                case TIMEOUT_EXCEPTION_RESPONSE:
                    throw new TimeOutException("Connection timmed out.");
            }
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private Message connectAndSendMessage(User user, MessageType type) {
        Message msg = null;
        try {
            //creates client connection
            client = new Socket();
            client.connect(socketAddress, 10000);
            //open writing and reading stream
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
            //make pdu with the user and the msg type
            msg = new Message(user, type);

            oos.writeObject(msg);

            msg = (Message) ois.readObject();
            return msg;
        } catch (SocketTimeoutException ex) {
            msg = new Message(user, MessageType.TIMEOUT_EXCEPTION_RESPONSE);
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RegistrableImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }
}
