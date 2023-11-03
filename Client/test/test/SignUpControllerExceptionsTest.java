package test;

import client.Client;
import java.util.concurrent.TimeoutException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

/**
 * 
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
     * Set up Java FX fixture for tests. This is a general approach for using a 
     * unique instance of the application in the test.
     * @throws java.util.concurrent.TimeoutException
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
    }
    
    @Test
    public void test2_ServerError(){
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
    }
    
    @Test
    public void test3_TimeOut() {
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
    }
}