/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import databaseconnection.DbConnection;
import encryption.BCrypt;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author 44796
 */
public class LoginDbManager {

    private int userId;
    private String username;
    private String firstName;
    private String surname;
    private String password;
    private String email;
    
    String connection(String username, String password) {
        PreparedStatement ps;
        ResultSet rs;

        String query;
        Alert alert;

            query = "SELECT * FROM Login_Credentials"; //Selects data from Login Credentials
            String userType = null;
            
            try {
                ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
                rs = ps.executeQuery();
                while (rs.next()) {
                    if ((rs.getString("u_username").equals(username) || rs.getString("u_email").equals(username)) && BCrypt.checkpw(password, rs.getString("u_password"))) {
                        alert = new Alert(AlertType.INFORMATION); //if user credentials are found, creates an alert
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Log In successful!");
                        alert.showAndWait();
                        userType = rs.getString("u_type");
                        this.userId = rs.getInt("u_id");
                        this.email = rs.getString("u_email");
                        this.username = rs.getString("u_username");
                        this.password = rs.getString("u_password");
                        this.firstName = rs.getString("u_fname");
                        this.surname = rs.getString("u_sname");  
                    }
                }
                return userType;
            } catch (SQLException ex) {
                Logger.getLogger(LoginFXMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }
    
    public int userId() {
        return userId;
    }
    
    public String email() {
        return email;
    }
    
    public String username() {
        return username;
    }
    
    public String password() {
        return password;
    }
    
    public String firstName() {
        return firstName;
    }
    
    public String surname() {
        return surname;
    }
    
}

