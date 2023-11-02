/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import client.Client;
import java.util.concurrent.TimeoutException;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author imape
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpTest extends ApplicationTest{
    
    @Override 
    public void start(Stage stage) throws Exception {
       new Client().start(stage);
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
    
    @Test
    public void test2_mandatoryFieldButtonEnable(){
       
        clickOn("#signUpLink");
        clickOn("#userTextField");
        write("Ann");
        verifyThat("#signUpButton", isDisabled());
        
        clickOn("#mailTextField");
        verifyThat("#userErrorLabel", isInvisible());
        write("ann@gmail.com");
        verifyThat("#signUpButton", isDisabled());
        
        clickOn("#passwordTextField");
        verifyThat("#mailErrorLabel", isInvisible());
        write("Abcd*1234");
        verifyThat("#signUpButton", isDisabled());
        verifyThat("#passwordErrorLabel", isInvisible());
        
        clickOn("#confirmPasswordTextField");
        write("Abcd*1234");
        clickOn("#userTextField");
        verifyThat("#signUpButton", isEnabled());
        verifyThat("#confirmPasswordErrorLabel", isInvisible());
    }
    
    
    @Test
    public void test3_correctFormatInput() {
        //Clicks on hyperlink
        clickOn("#signUpLink");
        clickOn("#userTextField");
        write("Ann");
     
        clickOn("#mailTextField");
        verifyThat("#userErrorLabel", isInvisible());
        write("ann@gmail.com");
        
        clickOn("#passwordTextField");
        verifyThat("#mailErrorLabel", isInvisible());
        write("Abcd*1234");
        verifyThat("#passwordErrorLabel", isInvisible());
        
        clickOn("#confirmPasswordTextField");
        write("Abcd*1234");
        
        clickOn("#phoneTextField");
        write("688888888");
        clickOn("#userTextField");
        verifyThat("#phoneErrorLabel", isInvisible());
        
        clickOn("#phoneTextField");
        eraseText(9);
        write("+31688888888");
        clickOn("#addressTextField");
        verifyThat("#phoneErrorLabel", isInvisible());
        
        write("55555 Street 45D 4A");
        clickOn("#userTextField");
        verifyThat("#addressErrorLabel", isInvisible());
    }
    
    @Test
    public void test4_userTextFieldError(){
        clickOn("#signUpLink");
        clickOn("#userTextField");
        write("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla libero non velit luctus, ut bibendum purus gravida. Sed auctor justo a nisi volutpat, nec cursus purus commodo. Proin ultrices, purus non tempus convallis, justo felis tincidunt elit, a cursus purus nunc ac nunc. Vivamus a sem id orci consectetur eleifend in eget justo. Curabitur varius euismod bibendum. Morbi sit amet justo sit amet tellus ultricies tincidunt. Suspendisse potenti. Nam euismod turpis sit amet est accumsan laoreet. Vestibulum scelerisque mauris a sagittis fermentum. Etiam a libero vel libero semper bibendum. Sed sit amet turpis feugiat, vehicula metus a, bibendum tellus. Sed auctor dolor sit amet tortor tincidunt, eu scelerisque dolor volutpat. Sed sed justo a urna posuere venenatis. Sed gravida, justo ut tristique varius, metus libero tincidunt mi, id finibus ipsum elit ut nulla. Proin dignissim, sapien nec congue ultrices, sapien purus rhoncus quam, eget malesuada nisl est ac quam");
        clickOn("#phoneTextField");
        verifyThat("#userErrorLabel", isVisible());
        clickOn("#userTextField");
        eraseText(500);
        write("2");
        clickOn("#phoneTextField");
        verifyThat("#userErrorLabel", isVisible());
        clickOn("#userTextField");
        eraseText(1);
        write(".");
        clickOn("#phoneTextField");
        verifyThat("#userErrorLabel", isVisible());
        
        
    }
    
}
