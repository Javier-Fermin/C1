/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * This is a factory for the Poolable interface
 * 
 * @author Javier
 */
public class PoolableFactory {
    /**
     * The poolable that is used in the factory
     */
    private static Poolable pool;
    
    /**
     * This method returns an implementation of the interface Poolable
     * 
     * @return the desired implementation
     */
    public static Poolable getPoolable(){
        //If there is no poolable created it is created
        if(pool == null){
            pool = new Pool();
        }
        return pool; 
    }
}
