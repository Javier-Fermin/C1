/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author javie
 */
public class SignUpController implements ChangeListener<String>{
    private Stage stage;
    
    @FXML
    private Label UserLabel;

    @FXML
    private TextField UserTextField;

    @FXML
    private Label UserErrorLabel;

    @FXML
    private Label PhoneLabel;

    @FXML
    private TextField PhoneTextField;

    @FXML
    private Label PhoneErrorLabel;

    @FXML
    private Label MailLabel;

    @FXML
    private TextField MailTextField;

    @FXML
    private Label MailErrorLabel;

    @FXML
    private Label AddressLabel;

    @FXML
    private TextField AddressTextField;

    @FXML
    private Label AddressErrorLabel;

    @FXML
    private Label PasswordLabel;

    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private Label ConfirmPasswordLabel;

    @FXML
    private PasswordField ConfirmPasswordTextField;

    @FXML
    private Label PasswordErrorLabel;

    @FXML
    private Label ConfirmPasswordErrorLabel;

    @FXML
    private Label SignInLabel;

    @FXML
    private Hyperlink SignInHyperlink;

    @FXML
    private Button SignUpButton;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Sign Up");
        
        //All the error labels are set to invisible
        UserErrorLabel.setVisible(false);
        PhoneErrorLabel.setVisible(false);
        MailErrorLabel.setVisible(false);
        AddressErrorLabel.setVisible(false);
        PasswordErrorLabel.setVisible(false);
        ConfirmPasswordErrorLabel.setVisible(false);
        
        //The signUpButton is disabled
        SignUpButton.disableProperty().set(true);
        
        //We set the textProperty listener to all the fields
        UserTextField.textProperty().addListener(this);
        PhoneTextField.textProperty().addListener(this);
        MailTextField.textProperty().addListener(this);
        AddressTextField.textProperty().addListener(this);
        PasswordTextField.textProperty().addListener(this);
        ConfirmPasswordTextField.textProperty().addListener(this);
        
        
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if(!newValue.isEmpty()
                && !UserTextField.getText().isEmpty()
                && !PhoneTextField.getText().isEmpty()
                && !MailTextField.getText().isEmpty()
                && !AddressTextField.getText().isEmpty()
                && !PasswordTextField.getText().isEmpty()
                && !ConfirmPasswordTextField.getText().isEmpty()){
            SignUpButton.disableProperty().set(false);
        }else{
            SignUpButton.disableProperty().set(true);
        }
    }
}
