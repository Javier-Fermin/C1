/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This class is a factory used to return implementations of the Poolable
 * interface
 * 
 * @author Javier
 */
public class PoolableFactory {
    private static Pool pool;
    
    /**
     * This method returns a Registrable implementation
     * 
     * @return the desired implementation
     */
    
    public static Poolable getClosable(){
        if(pool == null){
            pool = new Pool();
        }
        return pool; 
    }
}
