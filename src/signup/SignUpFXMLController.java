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
    private JFXTextField fnameTextField;

    @FXML
    private JFXTextField lnameTextField;

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXPasswordField password1Field;

    @FXML
    private JFXPasswordField password2Field;

    @FXML
    private JFXCheckBox checkbox;

    @FXML
    void resetDetails(ActionEvent event) { //when pressed, all fields will become empty
        fnameTextField.setText(null);
        lnameTextField.setText(null);
        usernameTextField.setText(null);
        emailTextField.setText(null);
        password1Field.setText(null);
        password2Field.setText(null);
    }

    @FXML
    void LogInLinkPressed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{6,25}$"; //validation for usernameTextField
        String emailPattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; //validation for emailTextField
        String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,128})"; //validation for password

        String fname = fnameTextField.getText();
        String lname = lnameTextField.getText();
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password1 = password1Field.getText();
        String password2 = password2Field.getText();

        if (fname.isEmpty() || lname.isEmpty() || username.isEmpty() || email.isEmpty()
            || password1.isEmpty() || password2.isEmpty() || !checkbox.isSelected()) { //if any fields are empty

            alert.setHeaderText("The following field(s) are missing:");
            if (fname.isEmpty()) {
                errorMessage.append("The First Name field is empty!");
                errorMessage.append("\n");
            }
            if (lname.isEmpty()) {
                errorMessage.append("The Last Name field is empty!");
                errorMessage.append("\n");
            }
            if (username.isEmpty()) {
                errorMessage.append("The Username field is empty!");
                errorMessage.append("\n");
            }
            if (email.isEmpty()) {
                errorMessage.append("The Email field is empty!");
                errorMessage.append("\n");
            }
            if (password1.isEmpty()) {
                errorMessage.append("The Password field is empty!");
                errorMessage.append("\n");
            }
            if (password2.isEmpty()) {
                errorMessage.append("The Confirm Password field is empty!");
                errorMessage.append("\n");
            }
            if (!checkbox.isSelected()) {
                errorMessage.append("The Terms and Conditions has not been accepted!");
            }
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        } else if (!fname.matches(namePattern) || !lname.matches(namePattern) || !username.matches(usernamePattern)
                || !email.matches(emailPattern) || !password1.matches(passwordPattern) || !password2.matches(passwordPattern)) {

            alert.setHeaderText("The following field(s) have invalid characters:");
            if (!fname.matches(namePattern)) {
                errorMessage.append("First Name");
                errorMessage.append("\n");
            }
            if (!lname.matches(namePattern)) {
                errorMessage.append("Last Name");
                errorMessage.append("\n");
            }
            if (!username.matches(usernamePattern)) {
                errorMessage.append("Username");
                errorMessage.append("\n");
            }
            if (!email.matches(emailPattern)) {
                errorMessage.append("Email");
                errorMessage.append("\n");
            }
            if (!password1.matches(passwordPattern)) {
                errorMessage.append("Password");
                errorMessage.append("\n");
            }
            if (!password2.matches(passwordPattern)) {
                errorMessage.append("Confirm Password");
                errorMessage.append("\n");
            }

            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        } else if (!password1.equals(password2)) { //if both entered passwords are not the same
            alert.setHeaderText("Passwords do not match!");
            alert.setContentText("Please re-check your password.");
            alert.showAndWait();
        } else {
            SignUpDbManager register = new SignUpDbManager();
            Boolean found = register.checkDuplicates(username, email); //checks to see if username/email already exists
            if (!found) { //if username/email are unique
                register.insertData(username, email, password1, fname, lname); //inserts data into database
                alert = new Alert(Alert.AlertType.INFORMATION); //if user credentials are found, creates an alert
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Sign-Up successful!");
                alert.setContentText("Welcome " + fname + " " + lname + "!");
                alert.showAndWait();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                LoginPanel(stage); //redirects to login Panel
            }
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
