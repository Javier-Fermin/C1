/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import client.Client;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import javafx.scene.input.KeyCode;
import org.junit.BeforeClass;
import org.junit.Test;
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
 * @author Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpTest extends ApplicationTest {

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }

    public void eraseAll() {
        doubleClickOn("#userTextField");
        eraseText(1);
        doubleClickOn("#phoneTextField");
        eraseText(2);
        doubleClickOn("#mailTextField");
        eraseText(1);
        doubleClickOn("#mailTextField");
        eraseText(1);
        doubleClickOn("#addressTextField");
        eraseText(1);
        doubleClickOn("#addressTextField");
        eraseText(1);
        doubleClickOn("#addressTextField");
        eraseText(1);
        doubleClickOn("#passwordTextField");
        eraseText(1);
        doubleClickOn("#confirmPasswordTextField");
        eraseText(1);
    }

    /**
     * Tests if the initalization of the SignUp window is correct
     */
    @Test
    public void test01_InitialState() {
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

    /**
     * Test the state of the SignUp window when all the mandatory fields are
     * written
     */
    @Test
    public void test02_mandatoryFieldButtonEnable() {

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

    /**
     * Tests all text and password fields when the format is correct
     */
    @Test
    public void test03_correctFormatInput() {
        eraseAll();
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

    /**
     * Tests if the user error label pops up when inputing a wrong format
     */
    //@Test
    public void test04_userTextFieldError() {
        eraseAll();
        verifyThat("#userErrorLabel", isInvisible());
        clickOn("#userTextField");
        write("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla libero non velit luctus, ut bibendum purus gravida. Sed auctor justo a nisi volutpat, nec cursus purus commodo. Proin ultrices, purus non tempus convallis, justo felis tincidunt elit, a cursus purus nunc ac nunc. Vivamus a sem id orci consectetur eleifend in eget justo. Curabitur varius euismod bibendum. Morbi sit amet justo sit amet tellus ultricies tincidunt. Suspendisse potenti. Nam euismod turpis sit amet est accumsan laoreet. Vestibulum scelerisque mauris a sagittis fermentum. Etiam a libero vel libero semper bibendum. Sed sit amet turpis feugiat, vehicula metus a, bibendum tellus. Sed auctor dolor sit amet tortor tincidunt, eu scelerisque dolor volutpat. Sed sed justo a urna posuere venenatis. Sed gravida, justo ut tristique varius, metus libero tincidunt mi, id finibus ipsum elit ut nulla. Proin dignissim, sapien nec congue ultrices, sapien purus rhoncus quam, eget malesuada nisl est ac quam");
        clickOn("#phoneTextField");
        verifyThat("#userErrorLabel", isVisible());
        clickOn("#userTextField");
        verifyThat("#userErrorLabel", isInvisible());
        push(KeyCode.END);
        eraseText(500);
        write("2");
        clickOn("#phoneTextField");
        verifyThat("#userErrorLabel", isVisible());
        clickOn("#userTextField");
        verifyThat("#userErrorLabel", isInvisible());
        eraseText(1);
        write(".");
        clickOn("#phoneTextField");
        verifyThat("#userErrorLabel", isVisible());
    }

    /**
     * Tests if the phone error label pops up when inputing a wrong format
     */
    @Test
    public void test05_phoneTextFieldError() {
        eraseAll();
        verifyThat("#phoneErrorLabel", isInvisible());
        clickOn("#phoneTextField");
        write("asdf");
        clickOn("#userTextField");
        verifyThat("#phoneErrorLabel", isVisible());
        clickOn("#phoneTextField");
        verifyThat("#phoneErrorLabel", isInvisible());
        eraseText(4);
        clickOn("#userTextField");
        verifyThat("#phoneErrorLabel", isInvisible());
        clickOn("#phoneTextField");
        write("123456789012345678901");
        clickOn("#userTextField");
        verifyThat("#phoneErrorLabel", isVisible());

    }

    /**
     * Tests if the mail error label pops up when inputing a wrong format
     */
    @Test
    public void test06_mailTextFieldError() {
        eraseAll();
        verifyThat("#mailErrorLabel", isInvisible());
        clickOn("#mailTextField");
        write("hello");
        clickOn("#userTextField");
        verifyThat("#mailErrorLabel", isVisible());
        clickOn("#mailTextField");
        eraseText(5);
        write("hello@hello");
        clickOn("#userTextField");
        verifyThat("#mailErrorLabel", isVisible());
        clickOn("#mailTextField");
        eraseText(11);
        write("hello@hello.");
        clickOn("#userTextField");
        verifyThat("#mailErrorLabel", isVisible());

    }

    /**
     * Tests if the address error label pops up when inputing a wrong format
     */
    //@Test
    public void test07_addressTextFieldError() {
        eraseAll();
        verifyThat("#addressErrorLabel", isInvisible());
        clickOn("#addressTextField");
        write("asdasdasd");
        clickOn("#userTextField");
        verifyThat("#addressErrorLabel", isVisible());
        clickOn("#addressTextField");
        verifyThat("#addressErrorLabel", isInvisible());
        eraseText(9);
        write("5555 asdasd");
        clickOn("#userTextField");
        verifyThat("#addressErrorLabel", isVisible());
        clickOn("#addressTextField");
        eraseText(11);
        write("55555");
        clickOn("#userTextField");
        verifyThat("#addressErrorLabel", isVisible());
        clickOn("#addressTextField");
        eraseText(5);
        write("55555 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse luctus vel libero quis bibendum. Proin venenatis varius dui, a consectetur nulla auctor in. Vestibulum sit amet scelerisque risus. Quisque vulputate sem id mi cursus, at ultricies libero viverra. Sed vel elit non quam interdum malesuada. In hendrerit augue a turpis sagittis, vel scelerisque justo viverra. Maecenas non euismod augue, a eleifend lectus. Vivamus vestibulum vel ante id suscipit. Vestibulum aliquam nisl in risus fermentum, sit amet sagittis mi facilisis. Nunc laoreet, libero in mattis bibendum, felis elit euismod velit, ut venenatis elit ante nec odio");
        clickOn("#userTextField");
        verifyThat("#addressErrorLabel", isVisible());
    }

    /**
     * Tests if the password error label pops up when inputing a wrong format
     */
    @Test
    public void test08_passwordTextFieldError() {
        eraseAll();
        verifyThat("#passwordErrorLabel", isInvisible());
        clickOn("#passwordTextField");
        write("123456a");
        clickOn("#confirmPasswordTextField");
        verifyThat("#passwordErrorLabel", isVisible());
        clickOn("#passwordTextField");
        eraseText(7);
        write("abcd*1234");
        clickOn("#confirmPasswordTextField");
        verifyThat("#passwordErrorLabel", isVisible());
        clickOn("#passwordTextField");
        eraseText(9);
        write("ABCD*1234");
        clickOn("#confirmPasswordTextField");
        verifyThat("#passwordErrorLabel", isVisible());

    }

    /**
     * Tests if the confirm password error label error label pops up when
     * inputing a wrong format
     */
    @Test
    public void test09_confirmPasswordTextfieldError() {
        eraseAll();
        verifyThat("#confirmPasswordErrorLabel", isInvisible());
        verifyThat("#passwordErrorLabel", isInvisible());
        clickOn("#passwordTextField");
        write("Abcd*1234");
        clickOn("#confirmPasswordTextField");
        write("Abcd*123");
        clickOn("#passwordTextField");
        verifyThat("#confirmPasswordErrorLabel", isVisible());
    }

    /**
     * Tests if the Hyperlink is working correctly
     */
    @Test
    public void test10_hyperlinkWorking() {
        clickOn("#signInHyperlink");
        verifyThat("#signInButton", isVisible());
    }

    /**
     * This test gins up a user and then tries again to check if an AlreadyExistsUserException is raised.
     */
    @Test
    public void test11_UserSignUpAndUserAlreadyExists() {
        String user = "User" + new Random().nextInt() + "@gmail.com";
        clickOn("#signUpLink");
        clickOn("#userTextField");
        write("User");
        clickOn("#mailTextField");
        write(user);
        clickOn("#passwordTextField");
        write("Abcd*1234");
        clickOn("#confirmPasswordTextField");
        write("Abcd*1234");
        clickOn("#signUpButton");
        clickOn("#signUpLink");
        clickOn("#userTextField");
        write("User");
        clickOn("#mailTextField");
        write(user);
        clickOn("#passwordTextField");
        write("Abcd*1234");
        clickOn("#confirmPasswordTextField");
        write("Abcd*1234");
        clickOn("#signUpButton");
        verifyThat(user+"\nThe user already exists.", isVisible());
        clickOn("Aceptar");
    }
}
