/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection
{
   Connection connection = null;
    private String typeUser = "";
    
    public static Connection getConnectionLoginDB() {
        java.sql.Connection connection = null;
        try {
            System.out.println("CONNECT");
          connection = DriverManager.getConnection(
                 "jdbc:sqlite:DB_LOGIN.db");
           System.out.println("CONNECTION MADE");
       } catch (SQLException e) {
          System.out.println("ERROR: MySQL Connection Failed!");
          e.printStackTrace();
       }
        System.out.println("RETURN");
        return connection;
    }
    
    public Connection getConnectionDataDB() {
        try {
            System.out.println("CONNECT");
          connection = DriverManager.getConnection(
                 "jdbc:sqlite:DB_SDGP_v3.db");
           System.out.println("CONNECTION MADE");
       } catch (SQLException e) {
          System.out.println("ERROR: MySQL Connection Failed!");
          e.printStackTrace();
       }
        System.out.println("RETURN");
        return connection;
    }
    
    public ResultSet getLoginCredentials(String query) throws SQLException {
        PreparedStatement ps = getConnectionLoginDB().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        ps.close();
        getConnectionLoginDB().close();
        return rs;
    }
    
    public ResultSet getProjectsPerYear() throws SQLException {
        ResultSet rs;
        rs= getConnectionDataDB().createStatement().executeQuery("SELECT * FROM projectsPerYear;");
        getConnectionDataDB().close();
        return rs;
    }
    
    public ResultSet getCostsEcContribution() throws SQLException {
        ResultSet rs;
        rs= getConnectionDataDB().createStatement().executeQuery("SELECT strftime('%Y' ,projectEndDate) AS endDate, AVG(projectTotalCost), AVG(projectMaxEcContribution) FROM Project  WHERE endDate <> '' GROUP by endDate;");
        getConnectionDataDB().close();
        return rs;
    }
    
    
    private void setUserType(String typeUser) {
        this.typeUser=typeUser;
    }
    
    public String getUserType() {
        return typeUser;
    }
    
    public HashMap getEcContributionByCountry(double filter) {
        HashMap<String, Double> map = new HashMap<>();
        Statement stmt;
        
        try {
            stmt = getConnectionDataDB().createStatement(); 
            String sql = "SELECT countryName, fundEcContribution"+
                    " FROM Country" + 
                    " JOIN OrgParticipant"+
                    " ON Country.countryId=OrgParticipant.countryId"+
                    " JOIN Fund" + 
                    " ON OrgParticipant.orgId=Fund.orgId;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                while(rs.next()) {
                    String country = rs.getString(1);
                    double ecContribution = rs.getDouble(2);
                    if (ecContribution > filter) {
                        if (map.containsKey(country)) {
                            map.put(country, map.get(country)+ecContribution);
                        } else {
                            map.put(country, ecContribution);
                        }   
                    }

                }
            } finally {
                rs.close();
            }
            
            stmt.close(); 
            getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return map;
    }
    
    public void addUserRegistration(String firstName, String surname, String email, String username, String password) {      
        Statement stmt;

        try {
            stmt = getConnectionLoginDB().createStatement(); 
            String sql = "INSERT INTO Login_Credentials (u_fname, u_sname, u_email, u_username, u_password, u_type)"
                    + " VALUES ('"+firstName+"', '"+surname+"', '"+email+"', '"+username+"', '"+password+"', 'U')";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            stmt.close(); 
            getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public ArrayList retrieveRegistrationNum() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();        
        Statement stmt;
        // SELECT username, MAX(logInDate) FROM logFile GROUP BY username;
        try {
            stmt = getConnectionLoginDB().createStatement(); 
            String sql = "SELECT COUNT(*), u_regDate FROM Login_Credentials GROUP BY u_regDate;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                
                while(rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("COUNT(*)")));
                    user.add(String.valueOf(rs.getDate("u_regDate")));
                    data.add(user);
                }
            } finally {
                rs.close();
            }
            
            stmt.close(); 
            getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return data;        
    }    
}
