/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.SignUpController;

/**
 *
 * @author javie
 */
public class Client extends javafx.application.Application{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
        Parent root = (Parent)loader.load();
        
        SignUpController cont = ((SignUpController)loader.getController());
        
        cont.setStage(stage);
        cont.initStage(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
