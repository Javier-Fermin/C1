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

    public void sleepTimeOut() throws TimeOutException{
        try {
            TimeUnit.SECONDS.sleep(30);
            throw new TimeOutException();
        } catch (InterruptedException ex) {
            Logger.getLogger(TimeOutThread.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    @Override
    public void run() {   
    }
}
