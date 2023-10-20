/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 * Sample Skeleton for 'SignInWindowFXML.fxml' Controller Class
 */
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.RegistrableFactory;
import src.AuthenticationException;
import src.Registrable;
import src.User;
import model.RegistrableImplementation;
import src.ServerErrorException;
import src.TimeOutException;

public class SignInController implements ChangeListener<String> {

    Registrable registro;

    private Stage stage; //This window Stage

    @FXML // fx:id="signInWindow"
    private Pane signInWindow; // Value injected by FXMLLoader

    @FXML // fx:id="imageLabel"
    private ImageView imageLabel; // Value injected by FXMLLoader

    @FXML // fx:id="logInLabel"
    private Label logInLabel; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLabel"
    private Label usernameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="passwordLabel"
    private Label passwordLabel; // Value injected by FXMLLoader

    @FXML // fx:id="usernameText"
    private TextField usernameText; // Value injected by FXMLLoader

    @FXML // fx:id="passwordText"
    private PasswordField passwordText; // Value injected by FXMLLoader

    @FXML // fx:id="signInButton"
    private Button signInButton; // Value injected by FXMLLoader

    @FXML // fx:id="signUpAccessLabel"
    private Label signUpAccessLabel; // Value injected by FXMLLoader

    @FXML // fx:id="signUpLink"
    private Hyperlink signUpLink; // Value injected by FXMLLoader

    @FXML // fx:id="showPasswordButton"
    private ToggleButton showPasswordButton; // Value injected by FXMLLoader

    @FXML // fx:id="showPasswordText"
    private TextField showPasswordText; // Value injected by FXMLLoader

    @FXML
    public void passwordButtonAction(ActionEvent event) {
        if (!showPasswordButton.isSelected()) {
            showPasswordButton.setGraphic(new ImageView("/res/show.png"));
        } else {
            showPasswordButton.setGraphic(new ImageView("/res/hide.png"));
        }
    }

    @FXML
    public void signInButtonAction(ActionEvent event) {
        try {
            User user;
            if (isValid(usernameText.getText())) {

                registro = new RegistrableFactory().getRegistrable();

                user = registro.SignIn(new User("", passwordText.getText(), "", usernameText.getText(), ""));

                if (user != null) {
                    Stage sStage = new Stage();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainWindowFXML.fxml"));
                    Parent root = (Parent) loader.load();

                    MainWindowController cont = ((MainWindowController) loader.getController());

                    cont.setMainStage(sStage);
                    cont.initStage(root, user);

                    stage.close();
                } else {
                    throw new AuthenticationException();
                }
            } else {
                throw new AuthenticationException();
            }
        } catch (AuthenticationException ex) {
            new Alert(Alert.AlertType.ERROR, "Authentication error").showAndWait();
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        } catch (TimeOutException ex) {
            new Alert(Alert.AlertType.ERROR, "Server Time out error").showAndWait();
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        } catch (ServerErrorException ex) {
            new Alert(Alert.AlertType.ERROR, "Server error").showAndWait();
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, "App error").showAndWait();
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
    }

    @FXML
    public void signUpClicked(ActionEvent event) {
        try {
            Stage sStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUP.fxml"));
            Parent root = (Parent) loader.load();

            SignUpController cont = ((SignUpController) loader.getController());

            cont.setSignUpStage(sStage);
            cont.initStage(root);

            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, User signUpUser) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/res/icon.png"));
        stage.setTitle("Odoo - SignIn");

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    return;
                }
                event.consume();
            }
        });

        addTextLimiter(usernameText, 500);
        addTextLimiter(passwordText, 500);
        usernameText.textProperty().addListener(this);
        passwordText.textProperty().addListener(this);

        if (signUpUser != null) {
            usernameText.setText(signUpUser.getEmail());
        }

        showPasswordButton.setOnAction(this::passwordButtonAction);
        signUpLink.setOnAction(this::signUpClicked);
        signInButton.setOnAction(this::signInButtonAction);

        signInButton.disableProperty().set(true);
        signInButton.setDefaultButton(true);

        stage.show();
    }

    private void handleWindowShowing(WindowEvent event) {
        showPasswordText.textProperty().bindBidirectional(passwordText.textProperty());
        showPasswordButton.setGraphic(new ImageView("/res/show.png"));
        passwordText.visibleProperty().bind(showPasswordButton.selectedProperty().not());
        showPasswordText.visibleProperty().bind(showPasswordButton.selectedProperty());
    }

    private static final String EMAIL_PATTERN
            = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValid(final String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.isEmpty() && !usernameText.getText().isEmpty() && !passwordText.getText().isEmpty()) {
            signInButton.disableProperty().set(false);
        } else {
            signInButton.disableProperty().set(true);
        }
    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

}
