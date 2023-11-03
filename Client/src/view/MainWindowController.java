/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import src.User;
import static view.SignInController.LOGGER;

/**
 *
 * @author javie
 */
public class MainWindowController {

    protected static final Logger LOGGER = Logger.getLogger(MainWindowController.class.getName());

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button logOutButton;

    private Stage stage; //This window Stage

    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Init method for MainWindow
     *
     * @param root
     * @param user
     */
    public void initStage(Parent root, User user) {
        //Receives a User object from the SignInWindow window.
        LOGGER.info("Init Main Window");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("/resources/images/icon.png"));
        stage.setTitle("Odoo - SignIn");
        stage.setResizable(false);

        //The escape button by default closes the window.
        LOGGER.info("If the window want to exit, alert to verify");
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

        //The logOutButton button is displayed on the screen
        LOGGER.info("Log Out settings");
        logOutButton.setOnAction(this::logOutAction);
        //The logOutButton button will be the default window button.
        logOutButton.setDefaultButton(true);

        //A welcome message is displayed with the name of the User object
        LOGGER.info("Set User name in Main window Welcome");
        welcomeLabel.setText("Welcome " + user.getName());

        stage.show();

    }

    /**
     * Method for logOutButton If you accept, the MainWindow window will close
     * and the SignInWindow window will open. If you cancel, you will return to
     * the MainWindow window
     *
     * @param event
     */
    @FXML
    public void logOutAction(ActionEvent event) {
        try {
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to log out?").showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                LOGGER.info("Go to SignIn Window");
                Stage sStage = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
                Parent root = (Parent) loader.load();

                SignInController cont = ((SignInController) loader.getController());

                cont.setStage(sStage);
                cont.initStage(root);

                stage.close();
            }
            event.consume();

        } catch (IOException ex) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
