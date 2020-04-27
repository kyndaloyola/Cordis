/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userdashboard;

import databaseconnection.DbConnection;
import encryption.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;

/**
 *
 * @author kynda
 */
public class UserDashboardDbManager {

    private XYChart.Series series = new XYChart.Series();
    private XYChart.Series seriesLineChartHome = new XYChart.Series();

    /**
     * Kynda
     * @param year - The initialisation of the Bar Chart requires a String representing the year
     * @return XYChart.Series type to populate the Bar Chart of the User Dashboard
     * The method is used to initialise a Bar Chart on the User Dashboard
     */
    public XYChart.Series intialiseBarChart(String year) {
        String date;
        //query to retrive the number of projects per year
        String sqll = "SELECT STRFTIME('%Y',projectStartDate)years,COUNT(projectId)NoProjects  "
                + "FROM Project WHERE years NOT NULL GROUP BY STRFTIME('%Y',projectStartDate);";
        
        
       //query to retrive the number of projects across the months in the selected year.
        String sqlSelected = "SELECT STRFTIME('%m',projectStartDate)months,COUNT(projectId)NoProjects  "
                + "FROM Project WHERE strftime('%Y', projectStartDate)='" + year + "' GROUP BY STRFTIME('%m',projectStartDate);";
        //connect to the database
        DbConnection connectionDb = new DbConnection();
        Connection connection = connectionDb.getConnectionDataDB();

        try {
            ResultSet rs;
            if (year.equals("All")) {
                date = "years";
                 
                rs = connection.createStatement().executeQuery(sqll);
                series.setName("Projects per year");
            } else {
                date = "months";
                rs = connection.createStatement().executeQuery(sqlSelected);
                series.setName("Projects per month");
            }
            while (rs.next()) {
                if (date.equals("years")) {
                    //adds the retrived data to the datastructure
                    XYChart.Data<String, Number> data = new XYChart.Data<>(rs.getString(date), rs.getInt("NoProjects"));
                    series.getData().add(data);
                } else {

                    XYChart.Data<String, Number> data1 = new XYChart.Data<>(getMonthKynda(rs.getString(date)), rs.getInt("NoProjects"));
                    series.getData().add(data1);
                }
            }
            //catches sql exception
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //returns the data structure after all the data have been filled.
        return series;
    }

    /**
     * Issam
     * @param lowerBound - Integer representing the lower bound of the range for the Bubble Chart
     * @param upperBound - Integer representing the upper bound of the range for the Bubble Chart
     * @return ArrayList<XYChart.Series> type to populate the Bubble Chart of the User Dashboard
     * The method is used to initialise the Bubble Chart of the User Dashboard
     */
    public ArrayList<XYChart.Series> initialiseBubbleChart(int lowerBound, int upperBound) {
        ArrayList<XYChart.Series> bubbleChartSeries = new ArrayList<>();
        Statement stmt;
        DbConnection connection = new DbConnection();
        try {
            stmt = connection.getConnectionDataDB().createStatement();
            String sql = "SELECT * FROM "
                    + "("
                    + "SELECT Country.countryName AS cN3, SUM(Fund.fundEcContribution)  AS Fund "
                    + "FROM Country "
                    + "JOIN OrgParticipant "
                    + "ON Country.countryId=OrgParticipant.countryId "
                    + "JOIN Fund "
                    + "ON OrgParticipant.orgId=Fund.orgId "
                    + "GROUP BY Country.countryName "
                    + "), "
                    + "("
                    + "SELECT COUNT(Project.orgId) AS NumProject, Country.countryName AS cN1 "
                    + "FROM OrgParticipant "
                    + "JOIN Project "
                    + "ON OrgParticipant.orgId=Project.orgId "
                    + "JOIN Country "
                    + "ON OrgParticipant.countryId=Country.countryId "
                    + "GROUP BY Country.countryName "
                    + "),("
                    + "SELECT Country.countryName AS cN2, COUNT(OrgParticipant.countryId) "
                    + "FROM OrgParticipant "
                    + "JOIN Country "
                    + "ON OrgParticipant.countryId=Country.countryId "
                    + "GROUP BY OrgParticipant.countryId "
                    + ") WHERE cN1=cN2 AND cN2=cN3;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();

            int counter = 0;
            try {
                while (rs.next()) {
                    String country = rs.getString(1);
                    int fund = rs.getInt(2);
                    int numPro = rs.getInt(3);
                    int numOrg = rs.getInt(6);
                    if ((fund >= lowerBound) && (fund <= upperBound)) {
                        bubbleChartSeries.add(new XYChart.Series());
                        bubbleChartSeries.get(counter).setName(country);
                        bubbleChartSeries.get(counter).getData().add(new XYChart.Data(numOrg * 10, numPro / 2, fund / 1000000));
                        counter++;
                    }
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

        return bubbleChartSeries;
    }

    /**
     * Trung
     * @return XYChart.Series to populate the first serie of the Area Chart of the User Dashboard
     */
    public XYChart.Series AreaChartSeries1() {
        String query = "SELECT strftime('%Y' ,projectEndDate) AS endDate, AVG(projectTotalCost), AVG(projectMaxEcContribution) FROM Project  WHERE endDate <> '' GROUP by endDate;";
        XYChart.Series series1 = new XYChart.Series<>(); //creates a new series for the costs line
        series1.setName("Total Project Costs");
        DbConnection connectionDb = new DbConnection();
        Connection connection = connectionDb.getConnectionDataDB();
        try {
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                series1.getData().add(new XYChart.Data<>(rs.getBigDecimal(1), rs.getDouble(2))); //gets data from sql
            }
        } catch (SQLException ex) { //catches the error
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return series1;
    }

    /**
     * Trung
     * @return XYChart.Series to populate the second serie of the Area Chart of the User Dashboard
     */
    public XYChart.Series AreaChartSeries2() {
        String query = "SELECT strftime('%Y' ,projectEndDate) AS endDate, AVG(projectTotalCost), AVG(projectMaxEcContribution) FROM Project  WHERE endDate <> '' GROUP by endDate;";
        XYChart.Series series2 = new XYChart.Series<>(); //creates a new series for the contributions line
        series2.setName("Max EC Contributions");
        DbConnection connectionDb = new DbConnection();
        Connection connection = connectionDb.getConnectionDataDB();
        try {
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {

                series2.getData().add(new XYChart.Data<>(rs.getBigDecimal(1), rs.getDouble(3))); //gets data from sql
            }
        } catch (SQLException ex) { //catches the error
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return series2;
    }

    /**
     * Issam
     * The method retrieve the organisation details from the database to be displayed on the User Dashboard
     * @return ArrayList allow a different method to used the details of an organisation 
     */
    public ArrayList getOrganisationDetails() {
        DbConnection connection = new DbConnection();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        Statement stmt;
        // SELECT username, MAX(logInDate) FROM logFile GROUP BY username;
        try {
            stmt = connection.getConnectionDataDB().createStatement();
            String sql = "SELECT OrgParticipant.orgId,"
                    + " OrgParticipant.orgEndOfPart,"
                    + " OrgParticipant.orgShortName,"
                    + " OrgParticipant.orgName,"
                    + " OrgParticipant.orgURL,"
                    + " OrgParticipant.orgVATNum,"
                    + " OrgParticipant.orgActivityType,"
                    + " OrgParticipant.orgPostCode,"
                    + " OrgParticipant.orgStreet,"
                    + " OrgParticipant.orgCity,"
                    + " Country.countryName"
                    + " FROM OrgParticipant"
                    + " JOIN Country"
                    + " ON OrgParticipant.countryId=Country.countryId;";

            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();

            try {

                while (rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("orgId")));
                    user.add(rs.getString("orgEndOfPart"));
                    user.add(rs.getString("orgShortName"));
                    user.add(rs.getString("orgName"));
                    user.add(rs.getString("orgURL"));
                    user.add(rs.getString("orgVATNum"));
                    user.add(rs.getString("orgActivityType"));
                    user.add(rs.getString("orgPostCode"));
                    user.add(rs.getString("orgStreet"));
                    user.add(rs.getString("orgCity"));
                    user.add(rs.getString("countryName"));
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
     * @param source - The source is column to search within the database via a SQL query
     * @param filter - To parameter search within the data of the 'source' column
     * @return ArrayList of the organisation matching the search filter - It is then used to populate the table
     */
    public ArrayList searchOrganisation(String source, String filter) {
        DbConnection connection = new DbConnection();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        Statement stmt;
        // SELECT username, MAX(logInDate) FROM logFile GROUP BY username;
        try {
            stmt = connection.getConnectionDataDB().createStatement();
            String sql = "SELECT OrgParticipant.orgId," +
                    " OrgParticipant.orgEndOfPart," +
                    " OrgParticipant.orgShortName," +
                    " OrgParticipant.orgName," +
                    " OrgParticipant.orgURL," +
                    " OrgParticipant.orgVATNum," +
                    " OrgParticipant.orgActivityType," +
                    " OrgParticipant.orgPostCode," +
                    " OrgParticipant.orgStreet," +
                    " OrgParticipant.orgCity," +
                    " Country.countryName" +
                    " FROM OrgParticipant" +
                    " JOIN Country" +
                    " ON OrgParticipant.countryId=Country.countryId" +
                    " AND instr("+source+", "+filter+");";

            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();

            try {

                while (rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("orgId")));
                    user.add(rs.getString("orgEndOfPart"));
                    user.add(rs.getString("orgShortName"));
                    user.add(rs.getString("orgName"));
                    user.add(rs.getString("orgURL"));
                    user.add(rs.getString("orgVATNum"));
                    user.add(rs.getString("orgActivityType"));
                    user.add(rs.getString("orgPostCode"));
                    user.add(rs.getString("orgStreet"));
                    user.add(rs.getString("orgCity"));
                    user.add(rs.getString("countryName"));
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
     * Kynda
     * The method retrives the coordinator of the project selected
     * @param pid - Project Identification Number
     * @return a String representing the coordinator of a project
     */
    public String getCoordinator (int pid){
        DbConnection connection = new DbConnection();
        Statement stmt;
        String cordiname = null;
        try {
            stmt = connection.getConnectionDataDB().createStatement(); 
            
            //joins the organisations and the projects to retrive thr coordinator based on the project given.
            String sql = "SELECT Project.projectId,"+"OrgParticipant.orgName"+
                         " FROM Project" +
                            " JOIN OrgParticipant" +
                            " ON Project.orgid=OrgParticipant.orgid "+"Where Project.projectId = "+pid+" " ;
                          
                    
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                while(rs.next()) {
                    
                      cordiname  = rs.getString("orgName");
                    
                    
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
       return cordiname;
    }
    
    /**
     * Kynda
     * The method retrives the project details from the database
     * @return ArrayList with the details of one or more project(s)
     */
    public ArrayList getProjectDetails() {
        DbConnection connection = new DbConnection();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        Statement stmt;
        try {
            stmt = connection.getConnectionDataDB().createStatement();
            String sql = "SELECT Project.projectId,"
                    + " Project.projectRCN,"
                    + " Project.projectAcronym,"
                    + " Project.projectStatus,"
                    + " Project.projectTitle,"
                    + " Project.projectStartDate,"
                    + " Project.projectEndDate,"
                    + " Project.projectURL,"
                    + " Project.projectObjective,"
                    + " Project.projectTotalCost,"
                    + " Project.projectSubject,"
                    + " Project.projectMaxEcContribution,"
                    + " FundingScheme.fundingSchemeName,"
                    + " Topic.topicName,"
                    + " OrgParticipant.orgShortName"
                    + " FROM Project"
                    + " JOIN FundingScheme"
                    + " ON Project.fundingSchemeId=FundingScheme.fundingSchemeId"
                    + " JOIN Topic"
                    + " ON Project.topicId=Topic.topicId"
                    + " JOIN OrgParticipant"
                    + " ON Project.orgId=OrgParticipant.orgId;";

            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();

            try {
                while (rs.next()) {
                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("projectId")));

                    try {
                        user.add(rs.getString("projectRCN"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectAcronym"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectStatus"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectTitle"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectStartDate"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectEndDate"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectURL"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectObjective"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(String.valueOf(rs.getDouble("projectTotalCost")));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectSubject"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(String.valueOf(rs.getDouble("projectMaxEcContribution")));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("fundingSchemeName"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("topicName"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("orgShortName"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

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
     * Kynda
     * @param month - A String type representing the month selected by the user
     * @return a String representing the month in full letters
     */
    public String getMonthKynda(String month) {
        String m = "";
        if (month.equals("01")) {
            m = "January";
        } else if (month.equals("02")) {
            m = "February";
        } else if (month.equals("03")) {
            m = "March";
        } else if (month.equals("12")) {
            m = "December";
        } else if (month.equals("04")) {
            m = "April";
        } else if (month.equals("05")) {
            m = "May";
        } else if (month.equals("06")) {
            m = "June";
        } else if (month.equals("07")) {
            m = "July";
        } else if (month.equals("08")) {
            m = "August";
        } else if (month.equals("09")) {
            m = "September";
        } else if (month.equals("10")) {
            m = "October";
        } else if (month.equals("11")) {
            m = "November";
        } else {
            m = "";
        }

        return m;

    }
    
    /**
     * Kynda
     * @param selection - String type representing the selection choice of the user
     * @return a String used for the SQL database query
     * The method retrives the name of the databse row based on the option the user selected 
     */
    public String getRowProj(String selection) {

        String row;
        if (selection.equals("ID")) {
            row = "Project.projectId";
        } else if (selection.equals("RCN")) {
            row = "Project.projectRCN";
        } else if (selection.equals("Acronym")) {
            row = "Project.projectAcronym";
        } else {
            row = null;
        }

        return row;
    }

    /**
     * Kynda
     * @param selection
     * @param values
     * @param d
     * @return 
     * The method searches for thr project based on the users inputted values
     */
    public boolean searchProj(String selection, String values, ArrayList<ArrayList<String>> d) {
        //String table = getRowLog(selection);
        Statement stmt;
        DbConnection connection = new DbConnection();

        String sel;
        sel = getRowProj(selection);

        try {

            stmt = connection.getConnectionDataDB().createStatement();
            ; 
            String sqlSelect = "SELECT Project.projectId,"
                    + " Project.projectRCN,"
                    + " Project.projectAcronym,"
                    + " Project.projectStatus,"
                    + " Project.projectTitle,"
                    + " Project.projectStartDate,"
                    + " Project.projectEndDate,"
                    + " Project.projectURL,"
                    + " Project.projectObjective,"
                    + " Project.projectTotalCost,"
                    + " Project.projectSubject,"
                    + " Project.projectMaxEcContribution,"
                    + " FundingScheme.fundingSchemeName,"
                    + " Topic.topicName,"
                    + " OrgParticipant.orgShortName"
                    + " FROM Project"
                    + " JOIN FundingScheme"
                    + " ON Project.fundingSchemeId=FundingScheme.fundingSchemeId"
                    + " JOIN Topic"
                    + " ON Project.topicId=Topic.topicId"
                    + " JOIN OrgParticipant"
                    + " ON Project.orgId=OrgParticipant.orgId" + " Where " + sel + "='" + values + "';";
            String sqlID = "SELECT Project.projectId,"
                    + " Project.projectRCN,"
                    + " Project.projectAcronym,"
                    + " Project.projectStatus,"
                    + " Project.projectTitle,"
                    + " Project.projectStartDate,"
                    + " Project.projectEndDate,"
                    + " Project.projectURL,"
                    + " Project.projectObjective,"
                    + " Project.projectTotalCost,"
                    + " Project.projectSubject,"
                    + " Project.projectMaxEcContribution,"
                    + " FundingScheme.fundingSchemeName,"
                    + " Topic.topicName,"
                    + " OrgParticipant.orgShortName"
                    + " FROM Project"
                    + " JOIN FundingScheme"
                    + " ON Project.fundingSchemeId=FundingScheme.fundingSchemeId"
                    + " JOIN Topic"
                    + " ON Project.topicId=Topic.topicId"
                    + " JOIN OrgParticipant"
                    + " ON Project.orgId=OrgParticipant.orgId" + " Where " + sel + " =" + values + ";";
            ResultSet rs;
            if (selection.equals("ID")) {
                stmt.execute(sqlID);
                rs = stmt.getResultSet();
            } else {
                stmt.execute(sqlSelect);
                rs = stmt.getResultSet();
            }

            try {
                while (rs.next()) {

                    ArrayList<String> user = new ArrayList<>();
                    user.add(String.valueOf(rs.getInt("projectId")));

                    try {
                        user.add(rs.getString("projectRCN"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectAcronym"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectStatus"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectTitle"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectStartDate"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectEndDate"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectURL"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectObjective"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(String.valueOf(rs.getDouble("projectTotalCost")));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("projectSubject"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(String.valueOf(rs.getDouble("projectMaxEcContribution")));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("fundingSchemeName"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("topicName"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    try {
                        user.add(rs.getString("orgShortName"));
                    } catch (NullPointerException e) {
                        user.add("NULL");
                    }

                    d.add(user);

                }
            } finally {
                stmt.close();
            }

            connection.getConnectionLoginDB().close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Trung
     * @param username - The input from the user
     * @return - True if user is not in the database - False if already in the database
     */
    boolean checkUsername(String username) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");

        String query = "SELECT u_username FROM Login_Credentials"; //Selects data from Login Credentials

        try {
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(username)) { //if username xists
                    alert.setHeaderText("Username already exists!");
                    alert.setContentText("Please re-enter a different username.");
                    alert.showAndWait();
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Trung
     * @param email - Input from the user
     * @return  - True if email is not in the database - False if already in the database
     */
    boolean checkEmail(String email) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");

        String query = "SELECT u_email FROM Login_Credentials"; //Selects data from Login Credentials

        try {
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString(1).equals(email)) { //if email exists
                    alert.setHeaderText("Email already exists!");
                    alert.setContentText("Please re-enter a different email.");
                    alert.showAndWait();
                    return true;
                }
            ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
    
    /**
     * Trung
     * @param userId
     * @param fname
     * @param lname
     * @param username
     * @param email 
     */
    void updateProfile(int userId, String fname, String lname, String username, String email) {
        String query = "UPDATE Login_Credentials SET u_fname = ?, u_sname = ?, u_username = ?, u_email =  ? WHERE u_id = ?";
        try { //updates data using user id
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, username);
            ps.setString(4, email);
            ps.setInt(5, userId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Trung
     * @param userId - Identification number of the user
     * @param password - New password input from the user
     */
    void updatePassword(int userId, String password) {
        String query = "UPDATE Login_Credentials SET u_password = ? WHERE u_id = ?";
        password = BCrypt.hashpw(password, BCrypt.gensalt(12)); //hashes password
        try { //updates data using user id
            PreparedStatement ps = DbConnection.getConnectionLoginDB().prepareStatement(query); //prepares query in SQL
            ps.setString(1, password);
            ps.setInt(2, userId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Issam
     * @param userId - Identification number of the user
     * The method write within the log file a TimeStamp on Logout and redirect the user to the Login window.
     */
    public void setLogOutUser(int userId) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatedDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatedDateTime); 
        try {
            System.out.println("INSERT TRY");
            Statement stmt = DbConnection.getConnectionLoginDB().createStatement();
            String sql = "UPDATE logFile SET logOutDateTime='"+formattedDate+"' WHERE userId="+userId+" AND logInDate=(SELECT logInDate" +
            " FROM logFile" +
            " WHERE userId="+userId+"" +
            " ORDER BY logInDate" +
            " DESC LIMIT 1);";
            // "UPDATE logFile SET logOutDateTime='"+formattedDate+"' WHERE userId="+userId+";";
            stmt.execute(sql);
            stmt.close();
            DbConnection.getConnectionLoginDB().close();
            System.out.println("INSERT DONE");
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}