/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admindashboard;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author kynda
 */
public class AdminDashboardFXMain extends Application
{
    
    @Override
    public void start(Stage Secondary) throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("AdminDashboardFXML.fxml"));
        Scene scene = new Scene(root);
        
        Secondary.setScene(scene);
        Secondary.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
    
}
