package model;

import src.Registrable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class is a factory used to return implementations of the Registrable
 * interface
 * 
 * @author Javier, Fran, Emil, Imanol
 */
public class RegistrableFactory {
    /**
     * This method returns a Registrable implementation
     * 
     * @return the desired implementation
     */
    public Registrable getRegistrable(){
        return new RegistrableImplementation();
    }
}
