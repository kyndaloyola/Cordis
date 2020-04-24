/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import userdashboard.UserDashboardFXMLController;

/**
 *
 * @author 44796
 */
public class LoginFXMLController implements Initializable {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField password;

    @FXML
    void enterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String username = usernameField.getText(); //gets text from username field
            username = username.trim(); //removes unneeded spaces on the username field
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Login(username, password.getText(), stage); //passes the username and password to the login manager class
        }
    }

    @FXML
    void LogInButtonPressed(ActionEvent event) { //if the log in button is pressed
        String username = usernameField.getText(); //gets text from username field
        username = username.trim(); //removes unneeded spaces on the username field
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Login(username, password.getText(), stage); //passes the username and password to the login manager class
    }

    @FXML
    void SignUpLinkPressed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        String url = "/signup/SignUpFXML.fxml";  //gets the file path
        loader.setLocation(getClass().getResource(url));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void Login(String username, String password, Stage stage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");

        if (username.isEmpty() && password.isEmpty()) {  //if both username and password fields are empty
            alert.setHeaderText("Username/Email and Password fields are empty!");
            alert.setContentText("Please enter your login details!");
            alert.showAndWait();
        } else if (username.isEmpty()) { //if username field is empty
            alert.setHeaderText("Username/Email field is empty!");
            alert.setContentText("Please enter your Username/Email!");
            alert.showAndWait();
        } else if (password.isEmpty()) { //if password field is empty
            alert.setHeaderText("Password field is empty!");
            alert.setContentText("Please enter your password!");
            alert.showAndWait();
        } else {
            LoginDbManager login = new LoginDbManager();
            boolean userFound = login.connection(username, password); //starts the connection, returns userType if user found
            if (userFound) { //if user has been found
                if (login.getUserType().equals("U")) { //if the user is a user, go to user dashboard
                    login.setLogConnetionDetails();
                    userDashboard(login.getUserType(), login.getUserId(), login.getUsername(),
                    login.getEmail(), password, login.getFirstName(), login.getSurname(), stage);
                } else if (login.getUserType().equals("A")) { //if the user is an admin, go to admin dashboard
                    login.setLogConnetionDetails();
                    adminDashboard(stage);
                }
            } else { //if user hasnt been found, username/password is incorrect
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Username/Password is incorrect");
                alert.setContentText("Please check your user details!");
                alert.showAndWait();
            }
        }
    }

    void userDashboard(String userType, int userId, String username, String email, String password, String firstName, String surname, Stage stage) { //switches to either admin or user dashboard
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/userdashboard/UserDashboardFXML.fxml"));
        try {
            Parent userDashboard = loader.load();
            UserDashboardFXMLController userController = loader.getController();
            userController.setUserDetails(userId, username, email, password, firstName, surname);
            Scene userScene = new Scene(userDashboard); //sets new scene for user dashboard
            stage.setScene(userScene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void adminDashboard(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/admindashboard/AdminDashboardFXML.fxml"));
        try {
            Parent adminDashboard = loader.load();
            Scene adminScene = new Scene(adminDashboard);
            stage.setScene(adminScene); //sets new scene for admin dashboard
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
