/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.TimeOutException;

/**
 *
 * @author javie
 */
public class TimeOutThread extends Thread {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(30);
            try {
                throw new TimeOutException();
            } catch (TimeOutException e) {
                Logger.getLogger(TimeOutThread.class.getName()).log(Level.FINE, null, e.getMessage());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(TimeOutThread.class.getName()).log(Level.FINE, null, "All fine");
        }
    }
}
