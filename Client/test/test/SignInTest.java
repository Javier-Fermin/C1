/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import client.Client;
import java.util.concurrent.TimeoutException;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author fdeys
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignInTest extends ApplicationTest {

    private ImageView showPasswdImg = new ImageView("resources/images/show.png");
    private ImageView hidePasswdImg = new ImageView("resources/images/hide.png");

    @Override
    public void stop(){}
    
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }

    //Test 1: Inicializacion(Comprobar campos vacios y desabilitados).
    @Test
    public void test01_InitialWindowState() {
        clickOn("#signUpLink");
        clickOn("#signInHyperlink");
        verifyThat("#usernameText", isVisible());
        verifyThat("#usernameText",  (TextField tf) -> tf.isFocused());
        verifyThat("#usernameText", hasText(""));
        verifyThat("#passwordText", isVisible());
        verifyThat("#passwordText", hasText(""));
        verifyThat("#showPasswordText", isInvisible());
        verifyThat("#showPasswordText", hasText(""));
        verifyThat("#signUpLink", isEnabled());
        verifyThat("#showPasswordButton", isEnabled());
        verifyThat(showPasswdImg, (ImageView b) -> b.isVisible());
        verifyThat("#signInButton", isDisabled());
    }
    
    //Test 2: Comprobar que el SignIn boton este deshabilitado si alguno de los 
    //        campos o ambos estan vacios(usernameText y passwordText).
    @Test
    public void test02_SignInButtonIsDisabled() {
        clickOn("#usernameText");
        write("manolo");
        verifyThat("#signInButton", isDisabled());
        eraseText(7);
        clickOn("#passwordText");
        write("password");
        verifyThat("#signInButton", isDisabled());
        eraseText(8);
        verifyThat("#signInButton", isDisabled());
    }

    //Test 3: Comprobar que el SignIn boton esta habilitado cuando los campos
    //        usernameText y passwordText no estan vacios.
    @Test
    public void test03_SignInButtonIsEnabled() {
        clickOn("#usernameText");
        write("manolo");
        clickOn("#passwordText");
        write("password");
        verifyThat("#signInButton", isEnabled());
        clickOn("#passwordText");
        eraseText(8);
        clickOn("#usernameText");
        eraseText(7);
    }

    //Test 4: Comprobar que se cambia el icono del ToggleButton y que se muestre
    //        o passwordText o showPasswordText.
    @Test
    public void test04_showPasswordButton() {
        clickOn("#passwordText");
        write("password");
        verifyThat("#showPasswordText", hasText("password"));
        clickOn("#showPasswordButton");
        verifyThat("#showPasswordButton", (ToggleButton tb) -> tb.isSelected());
        verifyThat("#passwordText", isInvisible());
        verifyThat("#showPasswordText", isVisible());
        verifyThat(hidePasswdImg, (ImageView iv) -> iv.isVisible());
        clickOn("#showPasswordButton");
        verifyThat("#showPasswordButton", (ToggleButton bt) -> !bt.isSelected());
        verifyThat("#showPasswordText", isInvisible());
        verifyThat("#passwordText", isVisible());
        verifyThat(showPasswdImg, (ImageView iv) -> iv.isVisible());
        clickOn("#passwordText");
        eraseText(8);
    }

    //Test 5: Comprobar que al clickar el boton SignInButton la ventana 
    //        MainWindow es visible.
    @Test
    public void test05_SignInCliked() {
        clickOn("#usernameText");
        write("manolo@gmail.com");
        clickOn("#passwordText");
        write("abcd*1234");
        clickOn("#signInButton");
        verifyThat("#mainWindowPane", isVisible());
        clickOn("#logOutButton");
        verifyThat("#signInWindow", isVisible());
    }

    //Test 6: Comprobar que se muestra un Alert cuando se introduce un username
    //        con formato de correo erroneo y pulsamos el boton SignInButton.
    @Test
    public void test06_badUsernameException() {
        clickOn("#usernameText");
        write("manolo");
        clickOn("#passwordText");
        write("password");
        clickOn("#signInButton");
        verifyThat("Email error: Bad email format", isVisible());
        clickOn("Aceptar");
        clickOn("#usernameText");
        eraseText(7);
        clickOn("#passwordText");
        eraseText(8);
    }

    //Test 7: Comprobar que se muestra un Alert cuando al hacer el metodo de 
    //        logica SignIn nos devuelva un objeto User nulo.
    @Test
    public void test07_AuthenticationError() {
        clickOn("#usernameText");
        write("manoloNoExiste@wakeup.please");
        clickOn("#passwordText");
        write("password");
        clickOn("#signInButton");
        verifyThat("Authentication error", isVisible());
        clickOn("Aceptar");
        doubleClickOn("#usernameText");
        eraseText(28);
        doubleClickOn("#usernameText");
        eraseText(28);
        clickOn("#passwordText");
        eraseText(8);
    }

    //Test 8: Comprobar que se muestra un Alert cuando salte el TimeOutException
    //        por tardar demasiado en conectarse con el servidor.
    @Test
    public void test08_TimeOutException() {
        clickOn("#usernameText");
        write("manolo@gmail.com");
        clickOn("#passwordText");
        write("abcd*1234");
        clickOn("#signInButton");
        verifyThat("Server Time out error", isVisible());
        clickOn("Aceptar");
        clickOn("#usernameText");
        eraseText(16);
        clickOn("#passwordText");
        eraseText(9);
    }
    
    //Test 9: Comprobar que se muestra un Alert cuando salte el TimeOutException
    //        por tardar demasiado en conectarse con el servidor.
    @Test
    public void test09_ServerErrorException() {
        clickOn("#usernameText");
        write("manolo@gmail.com");
        clickOn("#passwordText");
        write("abcd*1234");
        clickOn("#signInButton");
        verifyThat("Server error", isVisible());
        clickOn("Aceptar");
        clickOn("#usernameText");
        eraseText(16);
        clickOn("#passwordText");
        eraseText(9);
    }
    
    //Test 10: Comprobar que muestra un Alert de confirmacion al cerrar la 
    //         ventana y y esta no se cierra cuando el usuario cancela la 
    //         operacion.
    @Test
    public void test10_CancelCloseWindow() {
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
        verifyThat("Are you sure you want to exit?", isVisible());
        clickOn("Cancelar");
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
        verifyThat("Are you sure you want to exit?", isVisible());
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
    }
    //Test 11: Comprobar que muestra un Alert de confirmacion al cerrar la 
    //         ventana y esta se cierra cuando el usuario confirma la operacion.
    @Test
    public void test11_ConfirmCloseWindow() {
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
        verifyThat("Are you sure you want to exit?", isVisible());
        clickOn("Aceptar");
    }
}
