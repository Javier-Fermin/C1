package server;

import java.io.IOException;
import java.io.ObjectInputStream;
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

    private final String PUERTO = ResourceBundle.getBundle("resources.Properties").getString("PORT");
    private final String MAX_THREADS = ResourceBundle.getBundle("resources.Properties").getString("MAX_THREADS");
    private final RegistrableFactory factory = new RegistrableFactory();
    
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    
    private static Integer threads = 0;

    public void iniciar() {
        ServerSocket server = null;
        Socket client = null;
        try {
            // creates a server socket in the given port
            server = new ServerSocket(Integer.parseInt(PUERTO));
            LOGGER.info("Server started");
            Finisher finisher = new Finisher();
            finisher.start();
            while (true) {
                // waits until the client connects to the server
                client = server.accept();
                LOGGER.info("Client accepted");
                /* then it creates a worker with the parameters of the factory 
                 * to process the message
                 */
                if (threads < Integer.parseInt(MAX_THREADS)) {
                    Worker worker = new Worker(factory, client);
                    LOGGER.info("Worker started for the client");
                    worker.run();
                    threads++;
                }else{
                    LOGGER.severe("Max connections reached, responding the client");
                    ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
                    Message message = (Message) ois.readObject();
                    ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                    message.setMessageType(MessageType.SERVER_ERROR_EXCEPTION_RESPONSE);
                    oos.writeObject(message);
                    ois.close();
                    oos.close();
                    client.close();
                }
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }
    
    public static synchronized void minusThread() {
        LOGGER.info("A worker has ended.");
        threads--;
    }

    public static void main(String[] args) {
        Server s1 = new Server();
        // starts the server
        s1.iniciar();
    }
}
