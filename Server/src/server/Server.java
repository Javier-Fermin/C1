package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import model.RegistrableFactory;
import threads.Worker;

public class Server {

    private final int PUERTO = 5000;
    private final String clave = "abc";
    private final RegistrableFactory factory = new RegistrableFactory();

    public void iniciar() {
        ServerSocket servidor = null;
        Socket cliente = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        try {
            servidor = new ServerSocket(PUERTO);
            cliente = servidor.accept();
            Worker worker = new Worker(factory, cliente);
            
        } catch (IOException e) {
            
        } catch (Exception e) {
            
        } finally {
            try {
                if (servidor != null) {
                    servidor.close();
                }
                if (cliente != null) {
                    cliente.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
                if (salida != null) {
                    salida.close();
                }
            } catch (IOException e) {
                
            }
        }
    }

    public static void main(String[] args) {
        Server s1 = new Server();
        s1.iniciar();
    }
}