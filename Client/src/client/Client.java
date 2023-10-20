/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import src.User;
import view.SignInController;

/**
 *
 * @author javie
 */
public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
        Parent root = (Parent) loader.load();

        SignInController cont = ((SignInController) loader.getController());

        cont.setStage(stage);
        cont.initStage(root, null);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
