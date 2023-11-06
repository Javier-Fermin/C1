/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import client.Client;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author fdeys
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UnitTest extends ApplicationTest {

    @Override
    public void stop() {
    }

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }

    //Test 1: Unit Test for the aplication
    @Test
    public void testUnitTest() {
        String user = "User" + new Random().nextInt() + "@gmail.com";
        
        clickOn("#signUpLink");
        clickOn("#userTextField");
        write("User");
        verifyThat("#signUpButton", isDisabled());

        clickOn("#mailTextField");
        verifyThat("#userErrorLabel", isInvisible());
        write(user);
        verifyThat("#signUpButton", isDisabled());

        clickOn("#passwordTextField");
        verifyThat("#mailErrorLabel", isInvisible());
        write("Abcd*1234");
        verifyThat("#signUpButton", isDisabled());
        verifyThat("#passwordErrorLabel", isInvisible());

        clickOn("#confirmPasswordTextField");
        write("Abcd*1234");
        verifyThat("#signUpButton", isEnabled());
        verifyThat("#confirmPasswordErrorLabel", isInvisible());
        clickOn("#signUpButton");
        
        clickOn("#usernameText");
        write(user);
        clickOn("#passwordText");
        write("Abcd*1234");

        verifyThat("#signInButton", isEnabled());
        clickOn("#signInButton");
        
        verifyThat("#logOutButton", isVisible());
        clickOn("#logOutButton");
        
        verifyThat("#signInWindow", isVisible());
        
    }
}
