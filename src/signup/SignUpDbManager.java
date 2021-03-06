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
 * @author Trung
 */
public class SignUpDbManager {

    boolean checkDuplicates(String username, String email) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");

        String query = "SELECT u_username, u_email FROM Login_Credentials"; //Selects data from Login Credentials

        try {
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(username)) { //if username already exists
                    alert.setHeaderText("Username already exists!");
                    alert.setContentText("Please re-enter a different username.");
                    alert.showAndWait();
                    ps.close();
                    return true;
                } else if (rs.getString(2).equals(email)) { //if email already exists
                    alert.setHeaderText("Email already exists!");
                    alert.setContentText("Please re-enter a different email.");
                    alert.showAndWait();
                    ps.close();
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFXMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; //return false if there are no duplicates already in database
    }

    void insertData(String username, String email, String password, String fname, String lname) {
        password = BCrypt.hashpw(password, BCrypt.gensalt(12)); //hashes password
        String query = "INSERT INTO Login_Credentials (u_username, u_email, u_password, u_fname, u_sname, u_type)"
                + " values (?, ?, ?, ?, ?, ?)";
        try { //inserts the data into database
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, fname);
            ps.setString(5, lname);
            ps.setString(6, "U");
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(SignUpDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
