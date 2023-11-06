package test;

import client.Client;
import java.util.concurrent.TimeoutException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;


/**
 * This class tests all the exceptions that could be thrown 
 * 
 * @author Javier
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpControllerExceptionsTest extends ApplicationTest{
    
    /*@Override 
    public void start(Stage stage) throws Exception {
       new Client().start(stage);
    }*/
    
    /**
     * Launch only one JavaFX application
     * 
     * @throws TimeoutException
     */
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }
    
    /**
     * 
     */
    @Test
    public void test1_UserAlreadyExists(){
        clickOn("#signUpLink");
        clickOn("#userTextField");
        write("User");
        clickOn("#mailTextField");
        write("user@gmail.com");
        clickOn("#passwordTextField");
        write("Abcd*1234");
        clickOn("#confirmPasswordTextField");
        write("Abcd*1234");
        clickOn("#signUpButton");
        verifyThat("The user already exists.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * 
     */
    @Test
    public void test2_ServerError(){
        clickOn("#signUpButton");
        verifyThat("An unexpected error occurred, try again later.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * 
     */
    @Test
    public void test3_TimeOut() {
        clickOn("#signUpButton");
        verifyThat("Could not reach the server, try again later.", isVisible());
        clickOn("Aceptar");
    }
}