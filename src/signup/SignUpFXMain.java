package signup;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

 //@author Trung
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

    public static void main(String[] args) {
        launch(args);
    }
    
}
