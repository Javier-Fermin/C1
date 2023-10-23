package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RegistrableFactory;
import threads.WaitToStopThread;
import threads.Worker;

/**
 * This class is the responsible of initialize the server with an open connection
 * and make aviable to exit the application
 * @author Imanol
 */
public class Server {
    private final int PUERTO = 5000;
    private final String clave = "abc";
    private final RegistrableFactory factory = new RegistrableFactory();
   
    public void iniciar() {
        ServerSocket server = null;
        Socket client = null;
       
        try {
            // creates a server socken in the given port
            server = new ServerSocket(PUERTO);
            System.out.println("Waiting the client ...");
            // waits until the client connects to the server
            client = server.accept();
            System.out.println("Client connected!");
            /* then it creates a worker with the parameters of the factory 
            * to process the message
            */
            Woker worker = new Worker(factory, client);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (server != null)
                    server.close();
                if (client != null)
                    client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Fin servidor");
        }
    }

    public static void main(String[] args) {
        Server s1 = new Server();
        // starts the server
        s1.iniciar();
    }
}