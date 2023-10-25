/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import exceptions.PoolErrorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

/**
 * This is a pool of connections
 * 
 * @author Javier
 */
public class Pool {
    /**
     * The connections
     */
    private Stack connections;
    
    /**
     * A method to open/get a connection
     * 
     * @return the desired connection
     * @throws PoolErrorException in case there is any problem opening the connection
     */
    public synchronized Connection openConnection() throws PoolErrorException{
        try{
            if(connections.isEmpty()){
                connections.push(DriverManager.getConnection(""));
            }
            return (Connection) connections.pop();
        }catch(SQLException e){
            throw new PoolErrorException(e.getMessage());
        }
    }
    
    /**
     * This method saves a connection
     * 
     * @param connection the connection to be saved in the pool
     */
    public synchronized void closeConnection(Connection connection){
        connections.push(connection);
    }
    
    /**
     * This methods clears the stack removeing all the connections inside
     * 
     * @return True if the method cleared the whole stack False otherwise
     */
    public boolean closeAll(){
        connections.clear();
        return connections.isEmpty();
    }
}
