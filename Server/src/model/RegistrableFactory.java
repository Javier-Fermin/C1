package model;

import server.Pool;
import src.Registrable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author javie
 */
public class RegistrableFactory {
    public Registrable getRegistrable(Pool pool){
        return new RegistrableImplementation(pool);
    }
}
