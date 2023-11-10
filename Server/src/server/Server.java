package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import model.RegistrableFactory;
import src.Message;
import src.MessageType;
import threads.Worker;
import java.util.logging.Logger;
import threads.Finisher;

/**
 * This class is the responsible of initialize the server with an open
 * connection and make aviable to exit the application
 *
 * @author Imanol
 */
public class Server {

    /***
     * Parameter with the port the server uses.
     */
    private final String PUERTO = ResourceBundle.getBundle("resources.Properties").getString("PORT");
    /***
     * Parameter with the server's maximum number of open connections.
     */
    private final String MAX_THREADS = ResourceBundle.getBundle("resources.Properties").getString("MAX_THREADS");
    
    /***
     * The factory used to obtain a Registrable object. 
     */
    private final RegistrableFactory factory = new RegistrableFactory();

    /***
     * The logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    /***
     * Parameter with the number of current open connections
     */
    private static Integer threads = 0;

    /***
     * Server's initialization method
     */
    public void iniciar() {
        ServerSocket server = null;
        Socket client = null;
        try {
            // creates a server socket in the given port
            server = new ServerSocket(Integer.parseInt(PUERTO));
            LOGGER.info("Server started, it has a capacity for " + MAX_THREADS + " users");
            Finisher finisher = new Finisher();
            finisher.start();
            while (true) {
                try {
                    // waits until the client connects to the server
                    client = server.accept();
                    LOGGER.info("Client accepted");
                    /* then it creates a worker with the parameters of the factory 
                 * to process the message
                     */
                    if (threads < Integer.parseInt(MAX_THREADS)) {
                        Worker worker = new Worker(factory, client);
                        LOGGER.info("Worker started for the client");
                        threads++;
                        worker.start();
                    } else {
                        LOGGER.severe("Max connections reached, responding the client");
                        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                        Message message= new Message();
                        message.setMessageType(MessageType.SERVER_ERROR_EXCEPTION_RESPONSE);
                        LOGGER.info("Message sent.");
                        oos.writeObject(message);
                    }
                } catch (IOException e) {
                    LOGGER.severe(e.getMessage());
                } catch (Exception e) {
                    LOGGER.severe(e.getMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    /**
     * This method substracts one from the thread variable
     */
    public static synchronized void minusThread() {
        LOGGER.info("A worker has ended.");
        threads--;
    }

    /**
     * Entry point for the Java application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server s1 = new Server();
        // starts the server
        s1.iniciar();
    }
}
