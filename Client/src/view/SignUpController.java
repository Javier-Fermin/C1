/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import exceptions.BadAddressException;
import exceptions.BadEmailException;
import exceptions.BadPasswordException;
import exceptions.BadPhoneException;
import exceptions.BadUserException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This is the class that is responsible of controlling the responses for the
 * actions of the SignUp window.
 *
 * @author Javier, Imanol
 */
public class SignUpController {
    /**
     * The stage to use by the controller
     */
    private Stage stage;

    /**
     * Label for the userTextField
     */
    @FXML
    private Label userLabel;

    /**
     * TextField for the user's name
     */
    @FXML
    private TextField userTextField;

    /**
     * Label for the errors related to the userTextField
     */
    @FXML
    private Label userErrorLabel;

    /**
     * Label for the phoneTextField
     */
    @FXML
    private Label phoneLabel;

    /**
     * TextField for the user's phone
     */
    @FXML
    private TextField phoneTextField;

    /**
     * Label for the errors related to the phoneTextField
     */
    @FXML
    private Label phoneErrorLabel;

    /**
     * Label for the mailTextField
     */
    @FXML
    private Label mailLabel;

    /**
     * TextField for the user's mail
     */
    @FXML
    private TextField mailTextField;

    /**
     * Label for the errors related to the mailTextField
     */
    @FXML
    private Label mailErrorLabel;

    /**
     * Label for the addressTextField
     */
    @FXML
    private Label addressLabel;

    /**
     * TextField for the user's address
     */
    @FXML
    private TextField addressTextField;

    /**
     * Label for the errors related to the addressTextField
     */
    @FXML
    private Label addressErrorLabel;

    /**
     * Label for the passwordPasswordField
     */
    @FXML
    private Label passwordLabel;

    /**
     * PasswordField for the user's password
     */
    @FXML
    private PasswordField passwordTextField;

    /**
     * Label for the errors related to the passwordPasswordField
     */
    @FXML
    private Label passwordErrorLabel;

    /**
     * Label for the confirmPasswordPasswordField
     */
    @FXML
    private Label confirmPasswordLabel;

    /**
     * PasswordField for the user's password confirmation
     */
    @FXML
    private PasswordField confirmPasswordTextField;

    /**
     * Label for the errors related to the confirmPasswordPasswordField
     */
    @FXML
    private Label confirmPasswordErrorLabel;

    /**
     * Hyperlink to go to the SignIn window
     */
    @FXML
    private Hyperlink signInHyperlink;

    /**
     * Button to sign up the user
     */
    @FXML
    private Button signUpButton;

    /**
     * Setter for the stage
     * 
     * @param stage 
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * The initialization of this window.
     * 
     * @param root the DOM of the window
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);

        //Set the title of the window to "Odoo SignUp".
        stage.setTitle("Odoo Sign Up");
        //The window shouldn't be resizable
        stage.setResizable(false);
        //Establish the icon of the window 
        stage.getIcons().add(new Image("/resources/images/odoo_icon.png"));

        //All the error labels are set to invisible
        userErrorLabel.setVisible(false);
        phoneErrorLabel.setVisible(false);
        mailErrorLabel.setVisible(false);
        addressErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        confirmPasswordErrorLabel.setVisible(false);

        //The signUpButton is disabled
        signUpButton.disableProperty().set(true);
        //We set the handler of the button
        signUpButton.setOnAction(this::handleButtonSignUpOnAction);

        //We set the textProperty listeners to all the fields
        userTextField.textProperty().addListener(this::handleTextPropertyChange);
        phoneTextField.textProperty().addListener(this::handleTextPropertyChange);
        mailTextField.textProperty().addListener(this::handleTextPropertyChange);
        addressTextField.textProperty().addListener(this::handleTextPropertyChange);
        passwordTextField.textProperty().addListener(this::handleTextPropertyChange);
        confirmPasswordTextField.textProperty().addListener(this::handleTextPropertyChange);

        //We set the focusProperty listeners to all the fields
        userTextField.focusedProperty().addListener(this::handleUserFocusPropertyChange);
        phoneTextField.focusedProperty().addListener(this::handlePhoneFocusPropertyChange);
        mailTextField.focusedProperty().addListener(this::handleMailFocusPropertyChange);
        addressTextField.focusedProperty().addListener(this::handleAddressFocusPropertyChange);
        passwordTextField.focusedProperty().addListener(this::handlePasswordFocusPropertyChange);
        confirmPasswordTextField.focusedProperty().addListener(this::handleConfirmPasswordFocusPropertyChange);

        //Hyperlink view change 
        signInHyperlink.setOnAction(this::handleHyperlinkSignInOnAction);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * buttonSignUp action event handler
     * 
     * @param event An ActionEvent object
     */
    public void handleButtonSignUpOnAction(Event event) {
        signUpButton.requestFocus();
    }
    
    /**
     * hyperlinkSignIn action event handler
     * 
     * @param event An ActionEvent object
     */
    public void handleHyperlinkSignInOnAction(Event event) {
        try {
            Stage sStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
            Parent rootSignIn = (Parent) loader.load();

            SignInController cont = ((SignInController) loader.getController());

            //cont.setStage(sStage);
            //cont.initStage(rootSignIn);
            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * userTextField focus property change handler
     * 
     * @param observable the focused field property from userTextField
     * @param oldValue the old focus status
     * @param newValue the new focus status
     */
    public void handleUserFocusPropertyChange(ObservableValue observable,
            Boolean oldValue,
            Boolean newValue) {
        if (newValue) {
            userLabel.getStyleClass().remove("errorLabel");
            userTextField.getStyleClass().remove("textFieldError");
            if (!userTextField.getStyleClass().contains("textFieldWithIcon")) {
                userTextField.getStyleClass().add("textFieldWithIcon");
            }
            userErrorLabel.setVisible(false);
        } else {
            try {
                if (userTextField.getText().length() > 500) {
                    throw new BadUserException();
                }
            } catch (BadUserException e) {
                userErrorLabel.setVisible(true);
                userLabel.getStyleClass().add("errorLabel");
                userTextField.getStyleClass().remove("textFieldWithIcon");
                userTextField.getStyleClass().add("textFieldError");
            }
        }
    }

    /**
     * phoneTextField focus property change handler
     * 
     * @param observable the focused field property from phoneTextField
     * @param oldValue the old focus status
     * @param newValue the new focus status
     */
    public void handlePhoneFocusPropertyChange(ObservableValue observable,
            Boolean oldValue,
            Boolean newValue) {
        if (newValue) {
            phoneLabel.getStyleClass().remove("errorLabel");
            phoneTextField.getStyleClass().remove("textFieldError");
            phoneTextField.getStyleClass().add("textFieldWithIcon");
            phoneErrorLabel.setVisible(false);
        } else {
            try {
                if (!phoneTextField.getText().isEmpty()
                        && (phoneTextField.getText().length() > 20
                        || !(phoneTextField.getText().matches("[+][0-9]+")
                        || phoneTextField.getText().matches("[0-9]+")))) {
                    throw new BadPhoneException();
                }
            } catch (BadPhoneException e) {
                phoneErrorLabel.setVisible(true);
                phoneLabel.getStyleClass().add("errorLabel");
                phoneTextField.getStyleClass().remove("textFieldWithIcon");
                phoneTextField.getStyleClass().add("textFieldError");
            }
        }
    }

    /**
     * mailTextField focus property change handler
     * 
     * @param observable the focused field property from mailTextField
     * @param oldValue the old focus status
     * @param newValue the new focus status
     */
    public void handleMailFocusPropertyChange(ObservableValue observable,
            Boolean oldValue,
            Boolean newValue) {
        if (newValue) {
            mailLabel.getStyleClass().remove("errorLabel");
            mailTextField.getStyleClass().remove("textFieldError");
            mailTextField.getStyleClass().add("textFieldWithIcon");
            mailErrorLabel.setVisible(false);
        } else {
            try {
                if (!mailTextField.getText().isEmpty()
                        && (mailTextField.getText().length() > 500
                        || !mailTextField.getText().matches("[a-zA-Z0-9]+[@][a-zA-Z0-9]+[.][a-zA-Z0-9]+"))) {
                    throw new BadEmailException();
                }
            } catch (BadEmailException e) {
                mailErrorLabel.setVisible(true);
                mailLabel.getStyleClass().add("errorLabel");
                mailTextField.getStyleClass().remove("textFieldWithIcon");
                mailTextField.getStyleClass().add("textFieldError");
            }
        }
    }

    /**
     * addressTextField focus property change handler
     * 
     * @param observable the focused field property from addressTextField
     * @param oldValue the old focus status
     * @param newValue the new focus status
     */
    public void handleAddressFocusPropertyChange(ObservableValue observable,
            Boolean oldValue,
            Boolean newValue) {
        if (newValue) {
            addressLabel.getStyleClass().remove("errorLabel");
            addressTextField.getStyleClass().remove("textFieldError");
            addressTextField.getStyleClass().add("textFieldWithIcon");
            addressErrorLabel.setVisible(false);
        } else {
            try {
                if (addressTextField.getText().length() > 500
                        || (!addressTextField.getText().matches("[0-9]\\h[a-zA-Z]\\h[a-zA-Z]\\h[a-zA-Z0-9]\\h[a-zA-Z0-9]"))
                        && !addressTextField.getText().isEmpty()) {
                    throw new BadAddressException();
                } else {
                }
            } catch (BadAddressException e) {
                addressErrorLabel.setVisible(true);
                addressLabel.getStyleClass().add("errorLabel");
                addressTextField.getStyleClass().remove("textFieldWithIcon");
                addressTextField.getStyleClass().add("textFieldError");
            }
        }
    }

    /**
     * passwordPasswordField focus property change handler
     * 
     * @param observable the focused field property from passwordPasswordField
     * @param oldValue the old focus status
     * @param newValue the new focus status
     */
    public void handlePasswordFocusPropertyChange(ObservableValue observable,
            Boolean oldValue,
            Boolean newValue) {
        if (newValue) {
            passwordLabel.getStyleClass().remove("errorLabel");
            passwordTextField.getStyleClass().remove("textFieldError");
            passwordTextField.getStyleClass().add("textFieldWithIcon");
            passwordErrorLabel.setVisible(false);
        } else {
            try {

                if (((!passwordTextField.getText().matches(".*[a-z].*")
                        || !passwordTextField.getText().matches(".*[A-Z].*")
                        || !passwordTextField.getText().matches(".*\\d.*")
                        || !passwordTextField.getText().matches(".*[^a-zA-Z0-9].*"))
                        || (passwordTextField.getText().length() < 8 || passwordTextField.getText().length() > 500))
                        && !passwordTextField.getText().isEmpty()) {
                    throw new BadPasswordException();
                }
            } catch (BadPasswordException e) {
                passwordErrorLabel.setVisible(true);
                passwordLabel.getStyleClass().add("errorLabel");
                passwordTextField.getStyleClass().remove("textFieldWithIcon");
                passwordTextField.getStyleClass().add("textFieldError");
            }
        }
    }

    /**
     * confirmPasswordPasswordField focus property change handler
     * 
     * @param observable the focused field property from confirmPasswordPasswordField
     * @param oldValue the old focus status
     * @param newValue the new focus status
     */
    public void handleConfirmPasswordFocusPropertyChange(ObservableValue observable,
            Boolean oldValue,
            Boolean newValue) {
        if (newValue) {
            confirmPasswordLabel.getStyleClass().remove("errorLabel");
            confirmPasswordTextField.getStyleClass().remove("textFieldError");
            confirmPasswordTextField.getStyleClass().add("textFieldWithIcon");
            confirmPasswordErrorLabel.setVisible(false);
        } else {
            try {
                if (!confirmPasswordTextField.getText().equals(passwordTextField.getText())) {
                    throw new BadPasswordException();
                }
            } catch (BadPasswordException e) {

                confirmPasswordErrorLabel.setVisible(!passwordErrorLabel.isVisible());
                confirmPasswordLabel.getStyleClass().add("errorLabel");
                confirmPasswordTextField.getStyleClass().remove("textFieldWithIcon");
                confirmPasswordTextField.getStyleClass().add("textFieldError");

            }
        }
    }

    /**
     * A handler for the text property change
     * 
     * @param observable the chagned field
     * @param oldValue the old value of the field
     * @param newValue the new value of the field
     */
    public void handleTextPropertyChange(ObservableValue observable,
            String oldValue,
            String newValue) {
        if (!newValue.isEmpty()
                && !userTextField.getText().isEmpty()
                && !phoneTextField.getText().isEmpty()
                && !mailTextField.getText().isEmpty()
                && !addressTextField.getText().isEmpty()
                && !passwordTextField.getText().isEmpty()
                && !confirmPasswordTextField.getText().isEmpty()) {
            signUpButton.disableProperty().set(false);
        } else {
            signUpButton.disableProperty().set(true);
        }
    }
}