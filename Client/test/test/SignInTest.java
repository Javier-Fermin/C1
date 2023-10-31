/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import client.Client;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeoutException;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author fdeys
 */
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class SignInTest extends ApplicationTest{
    
    @Override
    public void stop() {}
    
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
   }
    
    //Test 1: Inicializacion(Comprobar campos vacios y desabilitados).
    
    //Test 2: Comprobar que el SignIn boton este deshabilitado si alguno de los 
    //        campos o ambos estan vacios(usernameText y passwordText).
    
    //Test 3: Comprobar que el SignIn boton esta habilitado cuando los campos
    //        usernameText y passwordText no estan vacios.
    
    //Test 4: Comprobar que se cambia el icono del ToggleButton.
    
    //Test 5: Comprobar que al clickar el boton SignInButton la ventana 
    //        MainWindow es visible.
    
    //Test 6: Comprobar que se muestra un Alert cuando se introduce un username
    //        con formato de correo erroneo y pulsamos el boton SignInButton.
    
    //Test 7: Comprobar que se muestra un Alert cuando al hacer el metodo de 
    //        logica SignIn nos devuelva un objeto User nulo.
    
    //Test 8: Comprobar que se muestra un Alert cuando salte el TimeOutException
    //        por tardar demasiado en conectarse con el servidor.
    
    //Test 9: Comprobar que muestra un Alert cuando el servidor este apagado o
    //        inaccesible.
    
    //Test 10: Comprobar que muestra un Alert de confirmacion al cerrar la 
    //         ventana y y esta no se cierra cuando el usuario cancela la 
    //         operacion.
    
    //Test 11: Comprobar que muestra un Alert de confirmacion al cerrar la 
    //         ventana y esta se cierra cuando el usuario confirma la operacion.
    
}
