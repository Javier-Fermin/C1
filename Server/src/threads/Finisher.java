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
import model.RegistrableImplementation;

/**
 *
 * @author javie
 */
public class Finisher extends Thread{
    private static final Logger LOGGER = Logger.getLogger(Finisher.class.getName());
    private final String END_KEY = ResourceBundle.getBundle("resources.Properties").getString("END_KEY");

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        LOGGER.info("To stop the server press "+END_KEY);
        String key = scanner.next();
        if (key.equals(END_KEY)) {
            LOGGER.info(END_KEY+" has been pressed, shutting down the server.");
            //Close the connections of the pool
            if(RegistrableImplementation.getPool()!=null){
                RegistrableImplementation.getPool().closeCOnnections();
            }
            exit(0);
        }
    }
}
