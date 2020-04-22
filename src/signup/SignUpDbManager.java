/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signup;

import databaseconnection.DbConnection;
import encryption.BCrypt;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import login.LoginFXMain;

/**
 *
 * @author kynda
 */
public class SignUpDbManager
{

    boolean checkDuplicates(String username, String email) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");

        String query = "SELECT u_username, u_email FROM Login_Credentials"; //Selects data from Login Credentials
            
            try {
                PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString(1).equals(username)) {
                        alert.setHeaderText("Username already exists!");
                        alert.setContentText("Please re-enter a different username.");
                        alert.showAndWait();
                        return true;
                    } else if (rs.getString(2).equals(email)) {
                        alert.setHeaderText("Email already exists!");
                        alert.setContentText("Please re-enter a different email.");
                        alert.showAndWait();
                        return true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginFXMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        return false;
    }

    void insertData(String username, String email, String password, String fname, String lname) {
        password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String query = "INSERT INTO Login_Credentials (u_username, u_email, u_password, u_fname, u_sname, u_type)"
        + " values (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, fname);
            ps.setString(5, lname);
            ps.setString(6, "U");
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(SignUpDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
    

