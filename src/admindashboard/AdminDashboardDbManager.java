/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admindashboard;

import databaseconnection.DbConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.XYChart;
import userdashboard.UserDashboardFXMLController;

/**
 *
 * @author kynda
 */
public class AdminDashboardDbManager
{
    private XYChart.Series series = new XYChart.Series();
    private int idAdmin = 0;
    
    /**
     * Kynda
     * Retrives the user details from the database
     * @return 
     */
    public ArrayList retrieveUserInfo() {
        DbConnection connection = new DbConnection();
        ArrayList<ArrayList<String>> data = new ArrayList<>();        
        Statement stmt;
        // SELECT username, MAX(logInDate) FROM logFile GROUP BY username;
        try {
            
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, Login_Credentials.u_type, Login_Credentials.u_regDate AS logInDate"+
                    " FROM Login_Credentials";
                    
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                
                while(rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("u_id")));
                    user.add(rs.getString("u_fname"));
                    user.add(rs.getString("u_sname"));
                    user.add(rs.getString("u_email"));
                    user.add(rs.getString("u_username"));
                    user.add(rs.getString("u_type"));
                    user.add(rs.getString("logInDate"));
                    data.add(user);
                }
            } finally {
                rs.close();
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return data;        
    }

    /**
     * Issam
     * Method retrieving the activity log from the database.
     * @return 
     */
    public ArrayList retrieveLogInfo() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();        
        Statement stmt;
        DbConnection connection = new DbConnection();
        // SELECT username, MAX(logInDate) FROM logFile GROUP BY username;
        try {
            
            
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                
                while(rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("u_id")));
                    user.add(rs.getString("u_fname"));
                    user.add(rs.getString("u_sname"));
                    user.add(rs.getString("u_email"));
                    user.add(rs.getString("u_username"));
                    user.add(rs.getString("logInDate"));
                    user.add(rs.getString("logOutDateTime"));
                    data.add(user);
                }
            } finally {
                rs.close();
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return data;        
    }

    /**
     * Issam
     * Method counting the number of user online (which have a null value as logout value in the database)
     * @return - String type which is caster from an integer countring the number of online users.
     */
    public String setUserOnlineValue() {       
        Statement stmt;
        int counter = 0;
        DbConnection connection = new DbConnection();
        try {
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT COUNT(*) "+
                "FROM logFile "+
                "WHERE logOutDateTime IS NULL "+
                "GROUP BY username;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            while(rs.next()) {
                counter++;
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return String.valueOf(counter);
    }

    /**
     * Issam
     * Method counting the number of new registration within this month.
     * If the registration has been done before this month, the user won't be counted as a new user.
     * @return String casted from an integer counting the number of new users. 
     */
    public String setNumberOfRegistration() {       
        Statement stmt;
        String counter="";
        DbConnection connection = new DbConnection();
        try {
            stmt = connection.getConnectionLoginDB().createStatement(); 
            String sql = "SELECT COUNT(*) FROM Login_Credentials WHERE strftime('%m', u_regDate) IN (strftime('%m', 'now'));";
            stmt.execute(sql);
           
            ResultSet rs = stmt.getResultSet();
            while(rs.next()) {
                counter = rs.getString(1);
            }
            
            stmt.close(); 
            connection.getConnectionDataDB().close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return counter;
    }
    
    /**
     * Kynda
     * Retrives the users value based on the admins input.
     * @param values
     * @param selection
     * @param data
     * @return 
     */
    public boolean searchUser(String values,String selection,ArrayList<ArrayList<String>> data){
         String table = getRowLoginCred(selection);
         Statement statement;
         DbConnection connection = new DbConnection();
    try
    {
         statement =connection.getConnectionLoginDB().createStatement();
         String sql = "SELECT * FROM Login_Credentials WHERE "+table+"='"+values+"';"; 
         String sqls ="SELECT * FROM Login_Credentials WHERE "+table+"="+values+";";
         if(selection.equals("ID")){
              statement.execute(sqls);
         }else{
             statement.execute(sql);
         }
       
         ResultSet rs = statement.getResultSet();
        try{
         while(rs.next()){
          
             
            ArrayList<String> user = new ArrayList<>();
            user.add(String.valueOf(rs.getInt("u_id")));
            user.add(rs.getString("u_fname"));
            user.add(rs.getString("u_sname"));
            user.add(rs.getString("u_email"));
            user.add(rs.getString("u_username"));
            user.add(rs.getString("u_type"));
            user.add(rs.getString("u_regDate"));
            data.add(user);
             
         }
        }finally{
                  statement.close();
             }
        
         connection.getConnectionLoginDB().close();
         return true;
    } catch (SQLException ex)
    {
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
     
    }

    /**
     * Issam
     * Method used to manage users; The selection from the user is converted in terms usable in a SQL query.
     * @param selection - Input String from user.
     * @return String usable within a SQL query
     */
    public String getRowLoginCred(String selection){
        String row ;
        if(selection.equals("ID")){
            row = "u_id";
        }else if(selection.equals("Email")){
            row="u_email";
        }else if(selection.equals("Username")){
            row="u_username";
        }else if(selection.equals("Type")){
            row="u_type";
        }else{
            row= null;
        }
        
        return row;
    }
    
    /**
     * Issam
     * Method used to manage log activity; The selection from the user is converted in terms usable in a SQL query
     * @param selection - Input String from the user
     * @return String usable within a SQL query
     */
    public String getRowLog(String selection){
        
        String row = null;
        
        if(selection == "Username"){
            row = "username";
        }else if(selection =="Password"){
            row="password";
        }else if(selection =="LogInDate"){
            row="logInDate";
        }else if(selection=="LogOutDate"){
            row="logOutDateTime";
        }else{
            row= null;
        }
        
        return row;
    }
    
    /**
     * Issam
     * Method used to search the log activity with a specified value.
     * @param values - String type representing the filter from the search
     * @param selection - Selected input converted in a different method
     * @param data 
     * @return - boolean (no semantic value but avoid to return a null value)
     */
    public boolean searchLog(String values,String selection,ArrayList<ArrayList<String>> data){
         //String table = getRowLog(selection);
         Statement statement;
         DbConnection connection = new DbConnection();
    try
    {
         statement =connection.getConnectionLoginDB().createStatement();
        // String sql = "SELECT * FROM logFile WHERE "+table+"='"+values+"';"; 
         String sqlId ="SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username WHERE Login_Credentials.u_id ="+values+";";
         String sqlEmail ="SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username WHERE Login_Credentials.u_email ='"+values+"';";
         String sqlUsername ="SELECT Login_Credentials.u_id, Login_Credentials.u_fname, Login_Credentials.u_sname, Login_Credentials.u_email, Login_Credentials.u_username, logFile.logInDate,  IFNULL(logFile.logOutDateTime, 'N/A') AS logOutDateTime "+ 
                "FROM Login_Credentials JOIN logFile ON Login_Credentials.u_username=logFile.username WHERE Login_Credentials.u_username ='"+values+"';";
         
         if(selection.equals("ID")){
              statement.execute(sqlId);
         }else if(selection.equals("Email")){
             statement.execute(sqlEmail);
         }else if (selection.equals("Username")){
              statement.execute(sqlUsername);
         }
       
         ResultSet rs = statement.getResultSet();
        try{
         while(rs.next()){
          
             
             ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("u_id")));
                    user.add(rs.getString("u_fname"));
                    user.add(rs.getString("u_sname"));
                    user.add(rs.getString("u_email"));
                    user.add(rs.getString("u_username"));
                    user.add(rs.getString("logInDate"));
                    user.add(rs.getString("logOutDateTime"));
                    data.add(user);
             
         }
        }finally{
                  statement.close();
             }
        
         connection.getConnectionLoginDB().close();
         return true;
    } catch (SQLException ex)
    {
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
          
      }
    
    /**
     * Kynda
     * Deletes a user based on the user id provided 
     * @param id
     * @return 
     */
    public boolean deleteUser(String id){
        Statement statement;
        DbConnection connection = new DbConnection();
        try{
            statement =connection.getConnectionLoginDB().createStatement();
            String sql="DELETE FROM Login_Credentials WHERE u_id="+id+";";
            statement.execute(sql);
            connection.getConnectionLoginDB().close();
            return true;
            
        }catch (SQLException ex){
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
        }
        
    }
    
    /**
     * Kynda
     * Checks if the user exists before deleting it
     * @param id
     * @return 
     */
    public boolean checkIfUserExists(String id){
        Statement statement;
        DbConnection connection = new DbConnection();
        int count= 0;
        
        try{
            
            statement =connection.getConnectionLoginDB().createStatement();
            
            
            String sql="SELECT * FROM Login_Credentials WHERE u_id ='"+ id +"';";
            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            while(rs.next()){
                count = rs.getRow();
               
            }
            statement.close();
            connection.getConnectionLoginDB().close();
            
            if(count == 0){
                return false;
                
            }else{
                
                return true;
            }
           
            
            
            
            
        }catch (SQLException ex){
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
        }
    }
    
    /**
     * Kynda
     * Adds a user to the database (used for testing)
     * @param fname
     * @param lname
     * @param email
     * @param username
     * @param password
     * @param type
     * @return 
     */
    public boolean addUser(String fname,String lname,String email,String username,String password,String type){
        Statement statement;
        DbConnection connection = new DbConnection();
        String sql="INSERT INTO Login_Credentials(u_fname,u_sname,u_email,u_username,u_password,u_type) VALUES('"+ fname +"','"+ lname +"','"+ email +"','"+ username +"','"+ password +"','"+ type +"');";
        
    try{
            
          statement =connection.getConnectionLoginDB().createStatement(); 
          statement.execute(sql);
          connection.getConnectionLoginDB().close();
          return true;
        
    }catch(SQLException ex){
        Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
    
    }
    
    /**
     * Issam
     * @param id - Integer: Identification Number
     * Method used to set the identification member of an administrator.
     * Used to access private Id within this class.
     */
    public void setIdAdministrator(int id) {
        this.idAdmin=id;
    }
    
    /**
     * Issam
     * @param id - Integer: Identification Number
     * Method add a TimeStamp to the database when an administrator logout from the Admin Dashboard
     */
    public void setLogOutUser(int id) {
        System.out.println("ID: " + id);
        try {
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatedDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateTime.format(formatedDateTime);
            
            System.out.println("INSERT TRY");
            Statement stmt = DbConnection.getConnectionLoginDB().createStatement();
            String sql = "UPDATE logFile SET logOutDateTime='"+formattedDate+"' WHERE userId="+id+" AND logInDate=(SELECT logInDate" +
                    " FROM logFile" +
                    " WHERE userId="+id+"" +
                    " ORDER BY logInDate" +
                    " DESC LIMIT 1);";
            // "UPDATE logFile SET logOutDateTime='"+formattedDate+"' WHERE userId="+userId+";";
            stmt.execute(sql);
            stmt.close();
            DbConnection.getConnectionLoginDB().close();
            System.out.println("INSERT DONE");
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
    /**
     * Kynda
     * Initialises the Line chart
     * @return 
     */
    public XYChart.Series intialiseChart() {
        String month ;
        //String date;
        LocalDate currentDate = LocalDate.now();
        String dom =Integer.toString(currentDate.getMonthValue());
        if(dom.length()>1){
            month= dom;
        }else{
           month = "0"+dom;
        }
        
        
        String sqlSelected = "SELECT STRFTIME('%d',u_regDate)days,COUNT(u_id)NoUsers "
                + "FROM Login_Credentials WHERE strftime('%m',u_regDate)='" + month + "' GROUP BY STRFTIME('%d',u_regDate);";
        
        DbConnection connectionDb = new DbConnection();
        Connection connection = connectionDb.getConnectionLoginDB();

        try {
            ResultSet rs;
            
            
                rs = connection.createStatement().executeQuery(sqlSelected);
                
            
            while (rs.next()) {
                

                    XYChart.Data<String, Number> data1 = new XYChart.Data<>(rs.getString("days"), rs.getInt("NoUsers"));
                    series.getData().add(data1);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return series;
    }
    
     
}
