package src;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * The class User is where all the data related to the user is stored in order to 
 * signIn and signUp
 * 
 * @author Javier, Emil, Imanol, Fran
 */
public class User implements Serializable{
    /**
     * Attributes for the Odoo user
     */
    private String name,passwd,phone,email,address;

    public User() {
    }
    
    public User(String name, String passwd, String phone, String email, String address) {
        this.name = name;
        this.passwd = passwd;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }
    
    /**
     * Getter of the name attribute
     * 
     * @return the value of the name attribute
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of the name attribute
     * 
     * @param name the value to set to the name attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter of the passwd attribute
     * 
     * @return the value of the passwd attribute
     */
    public String getPasswd() {
        return passwd;
    }

    /**
     * Setter of the passwd attribute
     * 
     * @param passwd the value to set to the passwd attribute
     */
    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    /**
     * Getter of the phone attribute
     * 
     * @return the value of the phone attribute
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setter of the phone attribute
     * 
     * @param phone the value to set to the phone attribute
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Getter of the email attribute
     * 
     * @return the value of the email attribute
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter of the email attribute
     * 
     * @param email the value to set to the email attribute
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter of the address attribute
     * 
     * @return the value of the address attribute
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter of the address attribute
     * 
     * @param address the value to set to the address attribute
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
