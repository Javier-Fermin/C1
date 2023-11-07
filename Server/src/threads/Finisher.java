/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import static java.lang.System.exit;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Logger;
import model.PoolableFactory;
import model.RegistrableImplementation;
import model.Poolable;

/**
 * A thread used to end the server when certain key is pressed
 * 
 * @author Javier
 */
public class Finisher extends Thread{
    /**
     * The Logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(Finisher.class.getName());
    /**
     * The key to end the server application
     */
    private final String END_KEY = ResourceBundle.getBundle("resources.Properties").getString("END_KEY");

    
    @Override
    public void run() {
        Poolable closable = PoolableFactory.getClosable();
        Scanner scanner = new Scanner(System.in);
        LOGGER.info("To stop the server press "+END_KEY);
        String key = scanner.next();
        if (key.equals(END_KEY)) {
            LOGGER.info(END_KEY+" has been pressed, shutting down the server.");
            //Close the connections of the pool
            closable.closeConnections();
            exit(0);
        }
    }
}
