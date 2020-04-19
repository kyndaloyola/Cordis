/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signup;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *
 * @author 44796
 */
public class SignUpFXMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {   //used to start the Sign Up Panel
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SignUpFXML.fxml"));
        Parent root = loader.load();
        stage.setTitle("Cordis Application");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
