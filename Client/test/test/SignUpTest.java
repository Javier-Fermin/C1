/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import client.Client;
import java.util.concurrent.TimeoutException;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author imape
 */
public class SignUpTest extends ApplicationTest{

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
        
   }
    /**
     * Tests if the initalization of the SignUp window is correct
     */
    @Test
    public void test1_InitialState(){
        //Clicks on hyperlink
        clickOn("#signUpLink");
        //Checks textfields
        verifyThat("#userTextField", hasText(""));
        verifyThat("#phoneTextField", hasText(""));
        verifyThat("#mailTextField", hasText(""));
        verifyThat("#addressTextField", hasText(""));
        verifyThat("#passwordTextField", hasText(""));
        verifyThat("#confirmPasswordTextField", hasText(""));
        //Check SignUp button and hyperlink
        verifyThat("#signUpButton", isDisabled());
        verifyThat("#signInHyperlink", isEnabled());
        //Check if error labels are desabled
        verifyThat("#userErrorLabel", isInvisible());
        verifyThat("#phoneErrorLabel", isInvisible());
        verifyThat("#mailErrorLabel", isInvisible());
        verifyThat("#addressErrorLabel", isInvisible());
        verifyThat("#passwordErrorLabel", isInvisible());
        verifyThat("#confirmPasswordErrorLabel", isInvisible());
        
    }
    
    
    
}
