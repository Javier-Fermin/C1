package model;

import src.Registrable;

/**
 * The RegistrableFactory class is responsible for creating instances of objects that implement the Registrable interface.
 * 
 * @author Imanol
 */
public class RegistrableFactory {
    
    private static Registrable registrable = new RegistrableImplementation();
    
    /**
     * Creates and returns a new instance of an object that implements the Registrable interface.
     *
     * @return a Registrable object that can be used for registration purposes.
     */
    public static Registrable getRegistrable(){
        return registrable;
    }
}
