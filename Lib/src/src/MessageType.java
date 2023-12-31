package src;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This enum is used to manage the requests and responses between Client and Server
 * applications
 * 
 * @author Javier, Emil, Fran, Imanol
 */
public enum MessageType implements Serializable{
    SIGNIN_REQUEST,
    SIGNUP_REQUEST,
    USER_ALREADY_EXISTS_EXCEPTION_RESPONSE,
    SERVER_ERROR_EXCEPTION_RESPONSE,
    AUTHENTICATION_EXCEPTION_RESPONSE,
    TIMEOUT_EXCEPTION_RESPONSE,
    SUCCESS_RESPONSE
}
