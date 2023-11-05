package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import src.Message;
import src.MessageType;
import src.AuthenticationException;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.User;
import src.UserAlreadyExistsException;

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

    /**
     * A parameter with the port of the server
     */
    private final String PUERTO = ResourceBundle.getBundle("resources.Client").getString("PORT");
    /**
     * A parameter with the address of the server
     */
    private final String IP = ResourceBundle.getBundle("resources.Client").getString("IP");
    /**
     * A socket for the connection with the server
     */
    private SocketAddress socketAddress = new InetSocketAddress(IP, Integer.parseInt(PUERTO));
    /**
     * A Logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(RegistrableImplementation.class.getName());

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
        User answer = null;
        try {
            Message msg = connectAndSendMessage(user, MessageType.SIGNIN_REQUEST);
            switch (msg.getMessageType()) {
                case SUCCESS_RESPONSE:
                    LOGGER.info("SignIn executed successfully.");
                    answer = msg.getUser();
                case AUTHENTICATION_EXCEPTION_RESPONSE:
                    LOGGER.severe("The credentials given for this user are wrong or the user does not exists.");
                    throw new AuthenticationException("The credentials given for this user\n"
                            + "are wrong or the user does not exists.");
                case SERVER_ERROR_EXCEPTION_RESPONSE:
                    LOGGER.severe("An unexpected error occurred, try again later.");
                    throw new ServerErrorException("An unexpected error occurred, try again later.");
                case TIMEOUT_EXCEPTION_RESPONSE:
                    LOGGER.severe("Could not reach the server, try again later.");
                    throw new TimeOutException("Could not reach the server, try again later.");
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
                LOGGER.severe(e.getMessage());
            }
        }
        return answer;
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
        User answer = null;
        try {
            Message msg = connectAndSendMessage(user, MessageType.SIGNUP_REQUEST);
            switch (msg.getMessageType()) {
                case SUCCESS_RESPONSE:
                    LOGGER.info("SignUp executed successfully.");
                    answer = msg.getUser();
                case USER_ALREADY_EXISTS_EXCEPTION_RESPONSE:
                    LOGGER.severe("The user already exists.");
                    throw new UserAlreadyExistsException("The user already exists.");
                case SERVER_ERROR_EXCEPTION_RESPONSE:
                    LOGGER.severe("An unexpected error occurred, try again later.");
                    throw new ServerErrorException("An unexpected error occurred, try again later.");
                case TIMEOUT_EXCEPTION_RESPONSE:
                    LOGGER.severe("Could not reach the server, try again later.");
                    throw new TimeOutException("Could not reach the server, try again later.");
            }
        } finally {
            try {
                if (client != null) {
                    LOGGER.info("Closing the socket.");
                    client.close();
                }
                if (oos != null) {
                    LOGGER.info("Closing the output stream.");
                    oos.close();
                }
                if (ois != null) {
                    LOGGER.info("Closing the input stream.");
                    ois.close();
                }
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
            }
        }
        return answer;
    }

    private Message connectAndSendMessage(User user, MessageType type) {
        Message msg = null;
        try {
            LOGGER.info("Connecting with the server.");
            //creates client connection
            client = new Socket();
            client.connect(socketAddress, 1000);
            LOGGER.info("Connection established.");
            //open writing and reading stream
            LOGGER.info("Preparing the streams for communication.");
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
            //make pdu with the user and the msg type
            LOGGER.info("Creating the message.");
            msg = new Message(user, type);
            LOGGER.info("Sending the message.");
            oos.writeObject(msg);
            LOGGER.info("Waiting for a response.");
            msg = (Message) ois.readObject();
            LOGGER.info("Answer recieved.");
        } catch (SocketTimeoutException ex) {
            msg = new Message(user, MessageType.TIMEOUT_EXCEPTION_RESPONSE);
            LOGGER.severe(ex.getMessage());
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOGGER.severe(ex.getMessage());
        }
        return msg;
    }
}
