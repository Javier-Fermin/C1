/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author javie
 */
public class MainWindowController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logOutButton;

    private Stage stage; //This window Stage

    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        
        logOutButton.setOnAction(this::logOutAction);

        stage.show();

    }

    @FXML
    public void logOutAction (ActionEvent event) {
        try {
            Stage sStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
            Parent root = (Parent) loader.load();

            SignInController cont = ((SignInController) loader.getController());

            cont.setStage(sStage);
            cont.initStage(root);

            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
