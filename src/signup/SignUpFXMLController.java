/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signup;

import com.jfoenix.controls.JFXCheckBox;
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
import javafx.stage.Stage;


/**
 * @author Trung Le
 */
public class SignUpFXMLController implements Initializable {
    
    @FXML
    private JFXTextField fname;

    @FXML
    private JFXTextField lname;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXPasswordField password1;

    @FXML
    private JFXPasswordField password2;

    @FXML
    private JFXCheckBox checkbox;
    
    
    @FXML
    void resetDetails(ActionEvent event) { //when pressed, all fields will become empty
        fname.setText(null);
        lname.setText(null);
        username.setText(null);
        email.setText(null);
        password1.setText(null);
        password2.setText(null);
    }
    
    @FXML
    void LogInLinkPressed(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        String url = "/login/LoginFXML.fxml";  //gets the file path
        loader.setLocation(getClass().getResource(url));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignUpFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    void validateDetails(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR); //sets the alert for incoming errors
        alert.setTitle("Error Dialog");
        StringBuilder errorMessage = new StringBuilder(); //used to append error messages
        String namePattern = "(?i)(^[a-z]+)[a-z .,-]((?! .,-)$){1,50}$"; //validation for first/last name
        String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{6,25}$"; //validation for username
        String emailPattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; //validation for email
        String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,128})"; //validation for password
        
        if (fname.getText().isEmpty() || lname.getText().isEmpty() || username.getText().isEmpty() ||
        email.getText().isEmpty() || password1.getText().isEmpty() || password2.getText().isEmpty() ||
        !checkbox.isSelected()) { //if any fields are empty
            
            alert.setHeaderText("The following field(s) are missing:");
        
            if (fname.getText().isEmpty()) {
                errorMessage.append("The First Name field is empty!");
                errorMessage.append("\n");
            }
            if (lname.getText().isEmpty()) {
                errorMessage.append("The Last Name field is empty!");
                errorMessage.append("\n");
            }
            if (username.getText().isEmpty()) {
                errorMessage.append("The Username field is empty!");
                errorMessage.append("\n");
            }
            if (email.getText().isEmpty()) {
                errorMessage.append("The Email field is empty!");
                errorMessage.append("\n");
            }
            if (password1.getText().isEmpty()) {
                errorMessage.append("The Password field is empty!");
                errorMessage.append("\n");
            }
            if (password2.getText().isEmpty()) {
                errorMessage.append("The Confirm Password field is empty!");
                errorMessage.append("\n");
            }
            if (!checkbox.isSelected()) {
                errorMessage.append("The Terms and Conditions has not been accepted!");
            }
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return; //will only show one error at a time
        }
        //if the fields contain any illegal characters or in an incorrect format
        if (!fname.getText().matches(namePattern) || !lname.getText().matches(namePattern) || !username.getText().matches(usernamePattern) ||
        !email.getText().matches(emailPattern) || !password1.getText().matches(passwordPattern) || !password2.getText().matches(passwordPattern)) {
        
            if (!fname.getText().matches(namePattern)) {
                errorMessage.append("First Name");
                errorMessage.append("\n");
            }
            if (!lname.getText().matches(namePattern)) {
                errorMessage.append("Last Name");
                errorMessage.append("\n");
            }
            if (!username.getText().matches(usernamePattern)) {
                errorMessage.append("Username");
                errorMessage.append("\n");
            }
            if (!email.getText().matches(emailPattern)) {
                errorMessage.append("Email");
                errorMessage.append("\n");
            }
            if (!password1.getText().matches(passwordPattern)) {
                errorMessage.append("Password");
                errorMessage.append("\n");
            }
            if (!password2.getText().matches(passwordPattern)) {
                errorMessage.append("Confirm Password");
                errorMessage.append("\n");
            }
            
            alert.setHeaderText("The following field(s) have invalid characters:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return; //will only show an error at a time
        }
        
        if (!password1.getText().equals(password2.getText())) { //if both entered passwords are not the same
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match. Please re-check your password.");
            alert.showAndWait();
            return;
        }
        
        SignUpDbManager register = new SignUpDbManager();
        Boolean found = register.checkDuplicates(username.getText(), email.getText()); //checks to see if username/email already exists
        if (!found) { //if username/email are unique
            register.insertData(username.getText().trim(), email.getText().trim(), password1.getText().trim(),
            fname.getText().trim(), lname.getText().trim()); //inserts data into database
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            LoginPanel(stage); //redirects to login Panel
        }
    }
    
    private void LoginPanel(Stage stage) {
        FXMLLoader loader = new FXMLLoader();
        String url = "/login/LoginFXML.fxml";  //gets the file path
        loader.setLocation(getClass().getResource(url));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(SignUpFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
    
}
