package client;

import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view.SignInController;

/**
 * The application class to initialize the app
 * 
 * @author Fran
 */
public class Client extends javafx.application.Application{
    /**
     * A Logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    
    /**
     * Entry point for the JavaFX application. 
     * 
     * @param stage The primary window of the application
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {

        //Get the SignInFXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
        //Load the DOM
        Parent root = (Parent) loader.load();

        //Get the controller from SignIn
        SignInController cont = ((SignInController) loader.getController());

        //Set the stage
        cont.setStage(stage);
        //Initialize the window
        cont.initStage(root);
    }

    /**
     * Entry point for the Java application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Starting the application.");
        launch(args);
    }

}
