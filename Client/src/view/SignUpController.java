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
import exceptions.NotMatchingPasswordException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.RegistrableFactory;
import src.Registrable;
import src.ServerErrorException;
import src.TimeOutException;
import src.User;
import src.UserAlreadyExistsException;

/**
 * This is the class that is responsible of controlling the responses for the
 * actions of the SignUp window.
 *
 * @author Javier, Imanol
 */
public class SignUpController {

    /**
     * A Logger for the logs
     */
    protected static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());

    /**
     * The stage to use by the controller
     */
    private Stage stage;

    /**
     * A implementation of the Registrable interface
     */
    private Registrable registrable = RegistrableFactory.getRegistrable();

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
     * Button used to trigger the hyperlink with the default escape button
     */
    @FXML
    private Button hyperlinkButton;

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
        LOGGER.info("Initializing SignUp.");

        Scene scene = new Scene(root);

        //Set the title of the window to "Odoo SignUp".
        stage.setTitle("Odoo Sign Up");
        //The window shouldn't be resizable
        stage.setResizable(false);
        //Establish the icon of the window 
        stage.getIcons().add(new Image("/resources/images/odoo_icon.png"));

        LOGGER.info("SignUp labels not visible.");
        //All the error labels are set to invisible
        userErrorLabel.setVisible(false);
        phoneErrorLabel.setVisible(false);
        mailErrorLabel.setVisible(false);
        addressErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        confirmPasswordErrorLabel.setVisible(false);

        LOGGER.info("SignUp button disable.");
        //The signUpButton is disabled
        signUpButton.disableProperty().set(true);
        //We set the handler of the button
        signUpButton.setOnAction(this::handleButtonSignUpOnAction);
        //The default button is the signUp button
        signUpButton.setDefaultButton(true);

        //The default escape button is the hyperlink
        hyperlinkButton.setCancelButton(true);

        LOGGER.info("SignUp textFields add listeners.");
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

        LOGGER.info("SignUp hyperlink set action.");
        //Hyperlink view change 
        signInHyperlink.setOnAction(this::handleHyperlinkSignInOnAction);

        LOGGER.info("SignUp alert on close request.");
        //Confirmation is requested when leaving the window
        stage.setOnCloseRequest((WindowEvent event) -> {
            //If you accept, you will exit the application.
            //If you cancel, you will return to the initial window.
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
            event.consume();
        });
        stage.setScene(scene);
        stage.show();
    }

    /**
     * hyperlinkButton action event handler
     *
     * @param event An ActionEvent object
     */
    public void handleButtonHyperlink(ActionEvent event) {
        signInHyperlink.fire();
    }

    /**
     * buttonSignUp action event handler
     *
     * @param event An ActionEvent object
     */
    public void handleButtonSignUpOnAction(ActionEvent event) {
        //The focus will be requested
        signUpButton.requestFocus();
        LOGGER.info("Check all labels are invisible.");
        //If there´s any error, we stop the log in 
        if (userErrorLabel.isVisible()
                || phoneErrorLabel.isVisible()
                || mailErrorLabel.isVisible()
                || addressErrorLabel.isVisible()
                || passwordErrorLabel.isVisible()
                || confirmPasswordErrorLabel.isVisible()) {
            event.consume();
        } else {
            LOGGER.info("Execute signUp method.");
            try {
                //Using the interface method SignUp we create a user tu later be added tod¡the DB
                registrable.signUp(new User(
                        userTextField.getText(),
                        passwordTextField.getText(),
                        phoneTextField.getText(),
                        mailTextField.getText(),
                        addressTextField.getText())
                );
                
                LOGGER.info("Changing from SignUp window to SignInWindow.");
                Stage sStage = new Stage();
                //It will load the SignIn window and then exites the current window to signin
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
                Parent rootSignIn = (Parent) loader.load();

                SignInController cont = ((SignInController) loader.getController());

                cont.setStage(sStage);
                cont.initStage(rootSignIn);
                stage.close();
                
            } catch (ServerErrorException ex) {
                LOGGER.severe(ex.getMessage());
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            } catch (UserAlreadyExistsException ex) {
                LOGGER.severe(mailTextField.getText()+"\n"+ex.getMessage());
                new Alert(Alert.AlertType.ERROR, mailTextField.getText()+"\n"+ex.getMessage()).showAndWait();
            } catch (TimeOutException ex) {
                LOGGER.severe(ex.getMessage());
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
            } catch (IOException ex) {
                LOGGER.severe(ex.getMessage());
            }
        }
    }

    /**
     * hyperlinkSignIn action event handler
     *
     * @param event An ActionEvent object
     */
    public void handleHyperlinkSignInOnAction(Event event) {
        LOGGER.info("Changing from SignUp window to SignInWindow.");
        try {
            Stage sStage = new Stage();
            //It will load the SignIn window and then exites the current window to signin
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
            Parent rootSignIn = (Parent) loader.load();

            SignInController cont = ((SignInController) loader.getController());

            cont.setStage(sStage);
            cont.initStage(rootSignIn);
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
        LOGGER.info("Check values to remove the userLabel");
        if (newValue) {
            //Removes the current styles when focus gained
            userLabel.getStyleClass().remove("errorLabel");
            userTextField.getStyleClass().remove("textFieldError");
            if (!userTextField.getStyleClass().contains("textFieldWithIcon")) {
                userTextField.getStyleClass().add("textFieldWithIcon");
            }
            userErrorLabel.setVisible(false);
        } else {
            LOGGER.info("Check values to userTextField has less than 500 characters");
            try {
                //if the user doesn´t fullfill the requirements it would throw an Exception
                if ((userTextField.getText().length() > 500
                        || !userTextField.getText().matches("[A-za-z\\s]+"))
                        && !userTextField.getText().isEmpty()) {
                    throw new BadUserException();
                }
            } catch (BadUserException e) {
                LOGGER.severe("Error in usertextField, show the userErrorLabel");
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
        LOGGER.info("Check values to remove the phoneLabel");
        if (newValue) {
            //Removes the current styles when focus gained
            phoneLabel.getStyleClass().remove("errorLabel");
            phoneTextField.getStyleClass().remove("textFieldError");
            phoneTextField.getStyleClass().add("textFieldWithIcon");
            phoneErrorLabel.setVisible(false);
        } else {
            //Checks the format of the phone, if incorrect then throw an exception
            try {
                LOGGER.info("Check values to phoneTextField has less than 20 characters and all characters are numbers");
                if (!phoneTextField.getText().isEmpty()
                        && (phoneTextField.getText().length() > 20
                        || !(phoneTextField.getText().matches("[+][0-9]+")
                        || phoneTextField.getText().matches("[0-9]+")))) {
                    throw new BadPhoneException();
                }
            } catch (BadPhoneException e) {
                LOGGER.severe("Error in phoneTextField, show the phoneErrorLabel");
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
        LOGGER.info("Check values to remove the mailLabel");
        if (newValue) {
            //Removes the current styles when focus gained
            mailLabel.getStyleClass().remove("errorLabel");
            mailTextField.getStyleClass().remove("textFieldError");
            mailTextField.getStyleClass().add("textFieldWithIcon");
            mailErrorLabel.setVisible(false);
        } else {
            try {
                LOGGER.info("Check values to mailTextField has less than 500 characters and has a correct format");
                //Checks the format of the mail, if incorrect then throw an exception
                if (!mailTextField.getText().isEmpty()
                        && (mailTextField.getText().length() > 500
                        || !mailTextField.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+[@][a-zA-Z0-9]+[.][a-zA-Z0-9]+"))) {
                    throw new BadEmailException();
                }
            } catch (BadEmailException e) {
                LOGGER.severe("Error in mailTextField, show the mailErrorLabel");
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
        LOGGER.info("Check values to remove the addressLabel");
        if (newValue) {
            //Removes the current styles when focus gained
            addressLabel.getStyleClass().remove("errorLabel");
            addressTextField.getStyleClass().remove("textFieldError");
            addressTextField.getStyleClass().add("textFieldWithIcon");
            addressErrorLabel.setVisible(false);
        } else {
            try {
                LOGGER.info("Check values to addressTextField has less than 500 characters and has a correct format");
                //Checks the format of the address, if incorrect then throw an exception
                if (addressTextField.getText().length() > 500
                        || (!addressTextField.getText().matches("[0-9][0-9][0-9][0-9][0-9]\\h[[a-zA-Z0-9]||\\h]+"))
                        && !addressTextField.getText().isEmpty()) {
                    throw new BadAddressException();
                } else {
                }
            } catch (BadAddressException e) {
                LOGGER.severe("Error in addressTextField, show the addressErrorLabel");
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
        LOGGER.info("Check values to remove the passwordLabel");
        if (newValue) {
            //Removes the current styles when focus gained
            passwordLabel.getStyleClass().remove("errorLabel");
            passwordTextField.getStyleClass().remove("textFieldError");
            passwordTextField.getStyleClass().add("textFieldWithIcon");
            passwordErrorLabel.setVisible(false);
        } else {
            try {
                LOGGER.info("Check values to passwordTextField has less than 500 characters and has a correct format");
                //Checks the format of the phone, if incorrect then throw an exception
                if (((!passwordTextField.getText().matches(".*[a-z].*")
                        || !passwordTextField.getText().matches(".*[A-Z].*")
                        || !passwordTextField.getText().matches(".*\\d.*")
                        || !passwordTextField.getText().matches(".*[^a-zA-Z0-9].*"))
                        || (passwordTextField.getText().length() < 8
                        || passwordTextField.getText().length() > 500))
                        && !passwordTextField.getText().isEmpty()) {
                    if (confirmPasswordErrorLabel.isVisible()) {
                        confirmPasswordErrorLabel.setVisible(false);
                    }
                    throw new BadPasswordException();
                }
                LOGGER.info("Check values from passwordTextField is equal to confirmPasswordTextField");
                //Checks the format of the password, if incorrect then throw an exception
                if ((!confirmPasswordTextField.getText().equals(passwordTextField.getText())
                        && !confirmPasswordTextField.getText().isEmpty())
                        && !passwordTextField.getText().isEmpty()) {
                    throw new NotMatchingPasswordException();
                } else {
                    if (confirmPasswordTextField.isVisible()
                            || confirmPasswordTextField.getStyleClass().contains("textFieldError")
                            || confirmPasswordLabel.getStyleClass().contains("errorLabel")) {
                        confirmPasswordLabel.getStyleClass().remove("errorLabel");
                        confirmPasswordTextField.getStyleClass().remove("textFieldError");
                        confirmPasswordTextField.getStyleClass().add("textFieldWithIcon");
                        confirmPasswordErrorLabel.setVisible(false);
                    }
                }
            } catch (BadPasswordException e) {
                LOGGER.severe("Value from passwordTextField have incorrect format, show passwordErrorLabel");
                passwordErrorLabel.setVisible(true);
                passwordLabel.getStyleClass().add("errorLabel");
                passwordTextField.getStyleClass().remove("textFieldWithIcon");
                passwordTextField.getStyleClass().add("textFieldError");
                if (confirmPasswordLabel.isVisible()) {
                    confirmPasswordLabel.getStyleClass().remove("errorLabel");
                    confirmPasswordTextField.getStyleClass().remove("textFieldError");
                    confirmPasswordTextField.getStyleClass().add("textFieldWithIcon");
                    confirmPasswordErrorLabel.setVisible(false);
                }

            } catch (NotMatchingPasswordException ex) {
                LOGGER.severe("Values from passwordTextField and confirmPasswordTextField are diferent, show confirmPasswordErrorLabel");
                if (!confirmPasswordTextField.getText().isEmpty()) {
                    confirmPasswordErrorLabel.setVisible(true);
                    confirmPasswordLabel.getStyleClass().add("errorLabel");
                    confirmPasswordTextField.getStyleClass().remove("textFieldWithIcon");
                    confirmPasswordTextField.getStyleClass().add("textFieldError");
                }

            }
        }
    }

    /**
     * confirmPasswordPasswordField focus property change handler
     *
     * @param observable the focused field property from
     * confirmPasswordPasswordField
     * @param oldValue the old focus status
     * @param newValue the new focus status
     */
    public void handleConfirmPasswordFocusPropertyChange(ObservableValue observable,
            Boolean oldValue,
            Boolean newValue) {
        LOGGER.info("Check values to remove the confirmPasswordLabel");
        if (newValue) {
            //Removes the current styles when focus gained
            confirmPasswordLabel.getStyleClass().remove("errorLabel");
            confirmPasswordTextField.getStyleClass().remove("textFieldError");
            confirmPasswordTextField.getStyleClass().add("textFieldWithIcon");
            confirmPasswordErrorLabel.setVisible(false);
            confirmPasswordLabel.getStyleClass().remove("errorLabel");
        } else {
            try {
                LOGGER.info("Check values from confirmPasswordTextField is equal to passwordTextField");
                if (!confirmPasswordTextField.getText().equals(passwordTextField.getText())
                        && !passwordErrorLabel.isVisible()
                        && !passwordTextField.getText().isEmpty()
                        && !confirmPasswordTextField.getText().isEmpty()) {
                    throw new BadPasswordException();
                }
            } catch (BadPasswordException e) {
                LOGGER.severe("Values from passwordTextField and confirmPasswordTextField are diferent, show confirmPasswordErrorLabel");
                confirmPasswordErrorLabel.setVisible(!passwordErrorLabel.isVisible());
                confirmPasswordLabel.getStyleClass().add("errorLabel");
                confirmPasswordTextField.getStyleClass().remove("textFieldWithIcon");
                confirmPasswordTextField.getStyleClass().add("textFieldError");

            }
        }
    }

    /**
     * A handler for the text property change, it checks if the userTextField,
     * mailTextField, passwordTextField and confirmPasswordTextField are
     * fullfilled, in case all of them are filled the button signUpButton will
     * be enabled otherwise disabled.
     *
     * @param observable the chagned field
     * @param oldValue the old value of the field
     * @param newValue the new value of the field
     */
    public void handleTextPropertyChange(ObservableValue observable,
            String oldValue,
            String newValue) {
        //Enable signUpButton if the required fields contain info
        if (!newValue.isEmpty()
                && !userTextField.getText().isEmpty()
                && !mailTextField.getText().isEmpty()
                && !passwordTextField.getText().isEmpty()
                && !confirmPasswordTextField.getText().isEmpty()) {
            if(signUpButton.isDisabled()){
                LOGGER.info("signUpButton enabled.");
            }
            signUpButton.disableProperty().set(false);
        } else {
            if(!signUpButton.isDisabled()){
                LOGGER.info("signUpButton disabled.");
            }
            signUpButton.disableProperty().set(true);
        }
    }
}
