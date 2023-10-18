/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import exceptions.BadEmailException;
import exceptions.BadPasswordException;
import exceptions.BadPhoneException;
import exceptions.BadUserException;
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
    private Label UserLabel,PhoneLabel,MailLabel,AddressLabel,PasswordLabel,ConfirmPasswordLabel,SignInLabel;

    @FXML
    private TextField UserTextField,PhoneTextField,MailTextField,AddressTextField;

    @FXML
    private Label UserErrorLabel,PhoneErrorLabel,MailErrorLabel,AddressErrorLabel,PasswordErrorLabel,ConfirmPasswordErrorLabel;
    
    @FXML
    private PasswordField PasswordTextField,ConfirmPasswordTextField;

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
        
        //We set the focus property listeners
        UserTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(newValue){
                UserLabel.getStyleClass().remove("errorLabel");
                UserTextField.getStyleClass().remove("textFieldError");
                UserTextField.getStyleClass().add("textFieldWithIcon");
                UserErrorLabel.setVisible(false);
            }else{
                try{
                    if(UserTextField.getText().length() > 500){
                        throw new BadUserException();
                    }
                }catch(BadUserException e){
                    UserErrorLabel.setVisible(true);
                    UserLabel.getStyleClass().add("errorLabel");
                    UserTextField.getStyleClass().remove("textFieldWithIcon");
                    UserTextField.getStyleClass().add("textFieldError");
                }
            }
        });
        
        PhoneTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(newValue){
                PhoneLabel.getStyleClass().remove("errorLabel");
                PhoneTextField.getStyleClass().remove("textFieldError");
                PhoneTextField.getStyleClass().add("textFieldWithIcon");
                PhoneErrorLabel.setVisible(false);
            }else{
                try{
                    if(!PhoneTextField.getText().isEmpty() 
                            && (PhoneTextField.getText().length() > 20 
                                || !(PhoneTextField.getText().matches("[+][0-9]+") 
                                    || PhoneTextField.getText().matches("[0-9]+")
                                    )
                                )
                            ){
                        throw new BadPhoneException();
                    }
                }catch(BadPhoneException e){
                    PhoneErrorLabel.setVisible(true);
                    PhoneLabel.getStyleClass().add("errorLabel");
                    PhoneTextField.getStyleClass().remove("textFieldWithIcon");
                    PhoneTextField.getStyleClass().add("textFieldError");
                }
            }
        });
        
        MailTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(newValue){
                MailLabel.getStyleClass().remove("errorLabel");
                MailTextField.getStyleClass().remove("textFieldError");
                MailTextField.getStyleClass().add("textFieldWithIcon");
                MailErrorLabel.setVisible(false);
            }else{
                try{
                    if(!MailTextField.getText().isEmpty() 
                            && (MailTextField.getText().length() > 500
                                || !MailTextField.getText().matches("[a-zA-Z0-9]+[@][a-zA-Z0-9]+[.][a-zA-Z0-9]+")
                                )
                            ){
                        throw new BadEmailException();
                    }
                }catch(BadEmailException e){
                    MailErrorLabel.setVisible(true);
                    MailLabel.getStyleClass().add("errorLabel");
                    MailTextField.getStyleClass().remove("textFieldWithIcon");
                    MailTextField.getStyleClass().add("textFieldError");
                }
            }
        });
        
        ConfirmPasswordTextField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(newValue){
                ConfirmPasswordLabel.getStyleClass().remove("errorLabel");
                ConfirmPasswordTextField.getStyleClass().remove("textFieldError");
                ConfirmPasswordTextField.getStyleClass().add("textFieldWithIcon");
                ConfirmPasswordErrorLabel.setVisible(false);
                PasswordLabel.getStyleClass().remove("errorLabel");
                PasswordTextField.getStyleClass().remove("textFieldError");
                PasswordTextField.getStyleClass().add("textFieldWithIcon");
                PasswordErrorLabel.setVisible(false);
            }else{
                try{
                    if(!ConfirmPasswordTextField.getText().equals(PasswordTextField.getText())){
                        throw new BadPasswordException();
                    }
                }catch(BadPasswordException e){
                    PasswordErrorLabel.setVisible(false);
                    ConfirmPasswordErrorLabel.setVisible(true);
                    ConfirmPasswordLabel.getStyleClass().add("errorLabel");
                    ConfirmPasswordTextField.getStyleClass().remove("textFieldWithIcon");
                    ConfirmPasswordTextField.getStyleClass().add("textFieldError");
                    //PasswordLabel.getStyleClass().add("errorLabel");
                    //PasswordTextField.getStyleClass().remove("textFieldWithIcon");
                    //PasswordTextField.getStyleClass().add("textFieldError");
                }
            }
        });
        
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
