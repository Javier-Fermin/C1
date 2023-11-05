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
        verifyThat("Aceptar", isVisible());
    }
    
    //@Test
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
        verifyThat("Aceptar", isVisible());
    }
    
    //@Test
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