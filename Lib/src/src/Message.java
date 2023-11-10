package src;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class is used to communicate between the Client Application and the Server
 * Application
 * 
 * @author Javier, Imanol, Fran, Emil
 */
public class Message implements Serializable{
    /**
     * The User that is involved during the client and server transactions
     */
    private User user;
    
    /**
     * The MessageType of the message to comunicate different requests and responses 
     */
    private MessageType messageType;

    public Message(User user, MessageType messageType) {
        this.user = user;
        this.messageType = messageType;
    }

    public Message() {
    }
    
    /**
     * Getter for the user attribute
     * 
     * @return the value of the attribute user 
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the user attribute
     * 
     * @param user the value to set to the attribute user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Getter for the messageType attribute
     * 
     * @return the value of the attribute messageType 
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Setter for the messageType attribute
     * 
     * @param messageType the value to set to the attribute messageType
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    
    
}
