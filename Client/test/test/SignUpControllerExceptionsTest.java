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
     * Tests a sign up server error, it only works when the server is wrong in some way that generates
     * a ServerErrorException
     */
    @Test
    public void test1_ServerError(){
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
        verifyThat("An unexpected error occurred, try again later.", isVisible());
        clickOn("Aceptar");
    }
    
    /**
     * Tests a sign up timeout, it only works when the parameters in the client
     * properties are wrong
     */
    @Test
    public void test2_TimeOut() {
        clickOn("#signUpButton");
        verifyThat("Could not reach the server, try again later.", isVisible());
        clickOn("Aceptar");
    }
}