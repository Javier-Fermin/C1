/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exceptions.PoolErrorException;
import java.sql.Connection;

/**
 * This interface contains all the methods needed in a pool class
 * 
 * @author Javier
 */
public interface Poolable {
    /**
     * This method should open a connection or get an existing one and return it
     * 
     * @return the desired connection
     * @throws PoolErrorException in case there is any problem openning a connection
     */
    public Connection openGetConnection() throws PoolErrorException;
    
    /**
     * This method is in charge of returning a connection to the pool
     * 
     * @param connection the connection to be stored
     */
    public void returnConnection(Connection connection);
    
    /**
     * This method closes the connections
     * 
     * @return true if the pool is empty and false otherwise
     */
    public boolean closeConnections();
}
