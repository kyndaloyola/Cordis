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
import databaseconnection.DbConnection;
import encryption.BCrypt;
import java.sql.Statement;

/**
 *
 * @author 44796
 */
public class LoginDbManager {

    private int userId;
    private String username;
    private String firstName;
    private String surname;
    private String email;
    private String userType;

    boolean connection(String username, String password) {
        String query = "SELECT * FROM Login_Credentials"; //Selects data from Login Credentials

        try {
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
            ResultSet rs = ps.executeQuery();
            while (rs.next()) { //goes through database
                if ((rs.getString("u_username").equals(username) || rs.getString("u_email").equals(username)) && BCrypt.checkpw(password, rs.getString("u_password"))) {
                    this.userType = rs.getString("u_type");
                    this.userId = rs.getInt("u_id");
                    this.email = rs.getString("u_email");
                    this.username = rs.getString("u_username");
                    this.firstName = rs.getString("u_fname");
                    this.surname = rs.getString("u_sname");
                    ps.close();
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginFXMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public void setLogConnetionDetails() {
        try {
            System.out.println("INSERT TRY");
            Statement stmt = DbConnection.getConnectionLoginDB().createStatement();
            String sql = "INSERT INTO logFile (username, userId) VALUES ('"+username+"', "+userId+");"; 
            stmt.execute(sql);
            stmt.close(); 
            DbConnection.getConnectionLoginDB().close();
            System.out.println("INSERT DONE");
        } catch (SQLException ex) {
            Logger.getLogger(LoginFXMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getUserType() {
        return userType;
    }

}
