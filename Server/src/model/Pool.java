/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import exceptions.PoolErrorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * This is a pool of connections
 *
 * @author Javier
 */
public class Pool implements Poolable {

    /**
     * Parameter with the password to access the database
     */
    private final String PASSWORD_DB = ResourceBundle.getBundle("resources.Properties").getString("PASSWORD_DB");
    /**
     * Parameter with the user to access the database
     */
    private final String USER_DB = ResourceBundle.getBundle("resources.Properties").getString("USER_DB");
    /**
     * Parameter with the url to access the database
     */
    private final String URL_DB = ResourceBundle.getBundle("resources.Properties").getString("URL_DB");
    /**
     * The Logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(Pool.class.getName());

    public Pool() {
        connections = new Stack();
    }

    /**
     * The connections
     */
    private Stack<Connection> connections;

    /**
     * A method to open/get a connection
     *
     * @return the desired connection
     * @throws PoolErrorException in case there is any problem opening the
     * connection
     */
    @Override
    public synchronized Connection openGetConnection() throws PoolErrorException {
        try {
            if (connections.isEmpty()) {
                LOGGER.info("Creating a new connection.");
                connections.push(DriverManager.getConnection(URL_DB, USER_DB, PASSWORD_DB));
            }
            LOGGER.info("Connection requested.");
            return (Connection) connections.pop();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
            throw new PoolErrorException(e.getMessage());
        }
    }

    /**
     * This method saves a connection
     *
     * @param connection the connection to be saved in the pool
     */
    @Override
    public synchronized void returnConnection(Connection connection) {
        LOGGER.info("Connection returned.");
        connections.push(connection);
    }

    /**
     * This methods clears the stack removeing all the connections inside
     *
     * @return True if the method cleared the whole stack False otherwise
     */
    @Override
    public boolean closeConnections() {
        if (connections != null) {
            for (Connection connection : connections) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    LOGGER.severe(ex.getMessage());
                }
            }
        }
        return connections.isEmpty();
    }
}
