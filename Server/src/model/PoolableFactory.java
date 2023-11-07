/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author javie
 */
public class PoolableFactory {
    private static Pool pool;
    
    public static Poolable getClosable(){
        if(pool == null){
            pool = new Pool();
        }
        return pool; 
    }
}
