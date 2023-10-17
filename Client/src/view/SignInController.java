/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 * Sample Skeleton for 'SignInWindowFXML.fxml' Controller Class
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SignInController {

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
        }else {
            showPasswordButton.setGraphic(new ImageView("/res/hide.png"));
        }
    }

    @FXML
    void signInButtonAction(ActionEvent event) {

    }

    public void signUpClicked(ActionEvent event) {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setOnShowing(this::handleWindowShowing);
        
        
        showPasswordButton.setOnAction(this::passwordButtonAction);
        signUpLink.setOnAction(this::signUpClicked);
        signInButton.setOnAction(this::signInButtonAction);
        
        stage.show();
    }
    
    private void handleWindowShowing(WindowEvent event) {
        showPasswordText.textProperty().bindBidirectional(passwordText.textProperty());
        showPasswordButton.setGraphic(new ImageView("/res/show.png"));
        passwordText.visibleProperty().bind(showPasswordButton.selectedProperty().not());
        showPasswordText.visibleProperty().bind(showPasswordButton.selectedProperty());
    }

}
