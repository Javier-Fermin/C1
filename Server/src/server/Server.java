package server;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RegistrableFactory;
//import threads.WaitToStopThread;
import threads.Worker;

/**
 * This class is the responsible of initialize the server with an open
 * connection and make aviable to exit the application
 *
 * @author Imanol
 */
public class Server {

    private final String PUERTO = ResourceBundle.getBundle("./resources/Properties.properties").getString("PORT");
    private final String MAX_THREADS = ResourceBundle.getBundle("./resources/Properties.properties").getString("MAX_THREADS");
    private final RegistrableFactory factory = new RegistrableFactory();
    
    private final Integer LOCAL_PORT_SSH_TUNNEL = Integer.parseInt(ResourceBundle.getBundle("./resources/Properties.properties").getString("LOCAL_PORT_SSH_TUNNEL"));
    private final Integer REMOTE_PORT_SSH_TUNNEL = Integer.parseInt(ResourceBundle.getBundle("./resources/Properties.properties").getString("REMOTE_PORT_SSH_TUNNEL"));
    private final String REMOTE_IP_SSH_TUNNEL = ResourceBundle.getBundle("./resources/Properties.properties").getString("REMOTE_IP_SSH_TUNNEL");
    private final Integer PORT_SSH_CONNECTION = Integer.parseInt(ResourceBundle.getBundle("./resources/Properties.properties").getString("PORT_SSH_CONNECTION"));
    private final String IP_SSH_CONNECTION = ResourceBundle.getBundle("./resources/Properties.properties").getString("IP_SSH_CONNECTION");
    private final String USER_SSH_CONNECTION = ResourceBundle.getBundle("./resources/Properties.properties").getString("USER_SSH_CONNECTION");
    private final String PASSWD_SSH_CONNECTION = ResourceBundle.getBundle("./resources/Properties.properties").getString("PASSWD_SSH_CONNECTION");

    
    private final Pool pool = new Pool();
    private static Integer threads = 0;

    public void iniciar() {
        ServerSocket server = null;
        Socket client = null;
        try {
            Session session = new JSch().getSession(
                    USER_SSH_CONNECTION,
                    IP_SSH_CONNECTION,
                    PORT_SSH_CONNECTION);
            session.setPassword(PASSWD_SSH_CONNECTION);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(
                    LOCAL_PORT_SSH_TUNNEL,
                    REMOTE_IP_SSH_TUNNEL,
                    REMOTE_PORT_SSH_TUNNEL);
            while (true) {
                // creates a server socken in the given port
                server = new ServerSocket(Integer.parseInt(PUERTO));
                System.out.println("Waiting the client ...");
                // waits until the client connects to the server
                client = server.accept();
                System.out.println("Client connected!");
                /* then it creates a worker with the parameters of the factory 
                 * to process the message
                 */
                if (threads < Integer.parseInt(MAX_THREADS)) {
                    Worker worker = new Worker(factory, client, pool);
                    worker.run();
                    threads++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (server != null) {
                    server.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Fin servidor");
        }
    }

    public static synchronized Integer getThreads() {
        return threads;
    }

    public static synchronized void setThreads(Integer threads) {
        Server.threads = threads;
    }

    public static void main(String[] args) {
        Server s1 = new Server();
        // starts the server
        s1.iniciar();
    }
}
