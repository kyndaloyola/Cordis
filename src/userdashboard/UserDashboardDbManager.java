/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userdashboard;

import databaseconnection.DbConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author kynda
 */
public class UserDashboardDbManager
{
     private XYChart.Series series = new XYChart.Series();
    private XYChart.Series seriesLineChartHome = new XYChart.Series();
    
    public XYChart.Series intialiseChartKyndas(String year){
        String date;
        String sqll="SELECT STRFTIME('%Y',projectStartDate)years,COUNT(projectId)NoProjects  "
                +"FROM Project WHERE years NOT NULL GROUP BY STRFTIME('%Y',projectStartDate);";
        String sqlCreateView ="SELECT * FROM projectsPerYear;";
        String sqlSelected ="SELECT STRFTIME('%m',projectStartDate)months,COUNT(projectId)NoProjects  "
                + "FROM Project WHERE strftime('%Y', projectStartDate)='"+year+"' GROUP BY STRFTIME('%m',projectStartDate);";
        DbConnection connectionDb = new DbConnection();
        Connection connection = connectionDb.getConnectionDataDB();
        
        try
        {
            ResultSet rs;
            if(year.equals("All")){
                date ="years";
                
              rs= connection.createStatement().executeQuery(sqll);
              series.setName("Projects per year");
            }else{
                date ="months";
               rs= connection.createStatement().executeQuery(sqlSelected);
               series.setName("Projects per month");
             }
            while(rs.next()){
               if(date.equals("years")){
                    
                     XYChart.Data<String,Number> data = new XYChart.Data<>(rs.getString(date),rs.getInt("NoProjects"));
                     series.getData().add(data);
                }else{
                   
                    XYChart.Data<String,Number> data1 = new XYChart.Data<>(getMonthKynda(rs.getString(date)),rs.getInt("NoProjects"));
                    series.getData().add(data1);
                }
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        
        return series;
    }
    
    public ArrayList<XYChart.Series> intialiseChartIssam(int lowerBound, int upperBound) {
        ArrayList<XYChart.Series> bubbleChartSeries = new ArrayList<>();
        Statement stmt;
        DbConnection connection = new DbConnection();
        try {
            stmt = connection.getConnectionDataDB().createStatement(); 
            String sql = "SELECT * FROM " +
                        "(" +
                        "SELECT Country.countryName AS cN3, SUM(Fund.fundEcContribution)  AS Fund " +
                        "FROM Country " +
                        "JOIN OrgParticipant " +
                        "ON Country.countryId=OrgParticipant.countryId " +
                        "JOIN Fund " +
                        "ON OrgParticipant.orgId=Fund.orgId " +
                        "GROUP BY Country.countryName " +
                        "), " +
                        "(" +
                        "SELECT COUNT(Project.orgId) AS NumProject, Country.countryName AS cN1 " +
                        "FROM OrgParticipant " +
                        "JOIN Project " +
                        "ON OrgParticipant.orgId=Project.orgId " +
                        "JOIN Country " +
                        "ON OrgParticipant.countryId=Country.countryId " +
                        "GROUP BY Country.countryName " +
                        "),(" +
                        "SELECT Country.countryName AS cN2, COUNT(OrgParticipant.countryId) " +
                        "FROM OrgParticipant " +
                        "JOIN Country " +
                        "ON OrgParticipant.countryId=Country.countryId " +
                        "GROUP BY OrgParticipant.countryId " +
                        ") WHERE cN1=cN2 AND cN2=cN3;";
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();

            int counter = 0;
            try {
                while(rs.next()) {
                    String country = rs.getString(1);
                    int fund = rs.getInt(2);
                    int numPro = rs.getInt(3);
                    int numOrg = rs.getInt(6);
                    if((fund>=lowerBound) && (fund<=upperBound)) {
                        bubbleChartSeries.add(new XYChart.Series());
                        bubbleChartSeries.get(counter).setName(country);
                        bubbleChartSeries.get(counter).getData().add(new XYChart.Data(numOrg*10, numPro/2, fund/1000000));
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
    
    public XYChart.Series AreaChartSeries1() {
        String query = "SELECT strftime('%Y' ,projectEndDate) AS endDate, AVG(projectTotalCost), AVG(projectMaxEcContribution) FROM Project  WHERE endDate <> '' GROUP by endDate;";
        XYChart.Series series1 = new XYChart.Series<>(); //creates a new series for the costs line
        series1.setName("Total Project Costs");
        DbConnection connectionDb = new DbConnection();
        Connection connection = connectionDb.getConnectionDataDB();
        try {
            ResultSet rs= connection.createStatement().executeQuery(query);
            while (rs.next()) {
                series1.getData().add(new XYChart.Data<>(rs.getBigDecimal(1), rs.getDouble(2))); //gets data from sql
            }
        } catch (SQLException ex) { //catches the error
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
         return series1;
    }
    
    public XYChart.Series AreaChartSeries2() {
        String query = "SELECT strftime('%Y' ,projectEndDate) AS endDate, AVG(projectTotalCost), AVG(projectMaxEcContribution) FROM Project  WHERE endDate <> '' GROUP by endDate;";
        XYChart.Series series2 = new XYChart.Series<>(); //creates a new series for the contributions line
        series2.setName("Max EC Contributions");
        DbConnection connectionDb = new DbConnection();
        Connection connection = connectionDb.getConnectionDataDB();
        try {
            ResultSet rs= connection.createStatement().executeQuery(query);
            while (rs.next()) {
                
                series2.getData().add(new XYChart.Data<>(rs.getBigDecimal(1), rs.getDouble(3))); //gets data from sql
            }
        } catch (SQLException ex) { //catches the error
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
         return series2;
    }
    
    public ArrayList getOrganisationDetails() {
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
                            " ON OrgParticipant.countryId=Country.countryId;";
                    
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                
                while(rs.next()) {
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
    
    public ArrayList getProjectDetails() {
        DbConnection connection = new DbConnection();
        ArrayList<ArrayList<String>> data = new ArrayList<>();        
        Statement stmt;
        try {
            stmt = connection.getConnectionDataDB().createStatement(); 
            String sql = "SELECT Project.projectId," +
                            " Project.projectRCN," +
                            " Project.projectAcronym," +
                            " Project.projectStatus," +
                            " Project.projectTitle," +
                            " Project.projectStartDate," +
                            " Project.projectEndDate," +
                            " Project.projectURL," +
                            " Project.projectObjective," +
                            " Project.projectTotalCost," +
                            " Project.projectSubject," +
                            " Project.projectMaxEcContribution," +
                            " FundingScheme.fundingSchemeName," +
                            " Topic.topicName," +
                            " OrgParticipant.orgShortName" +
                            " FROM Project" +
                            " JOIN FundingScheme" +
                            " ON Project.fundingSchemeId=FundingScheme.fundingSchemeId" +
                            " JOIN Topic" +
                            " ON Project.topicId=Topic.topicId" +
                            " JOIN OrgParticipant" +
                            " ON Project.orgId=OrgParticipant.orgId;";
                    
            stmt.execute(sql);
            ResultSet rs = stmt.getResultSet();
            
            try {
                while(rs.next()) {
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
    
    public String getMonthKynda(String month){
        String m="";
        if(month.equals("01")){
            m = "January";
        }else if(month.equals("02")){
            m = "February";
        }else if(month.equals("03")){
            m ="March";
        }else if(month.equals("12")){
            m ="December";
        }else if(month.equals("04")){
            m ="April";
        }else if(month.equals("05")){
            m ="May";
        }else if(month.equals("06")){
            m ="June";
        }else if(month.equals("07")){
            m ="July";
        }else if(month.equals("08")){
            m ="August";
        }else if(month.equals("09")){
            m ="September";
        }else if(month.equals("10")){
            m ="October";
        }else if(month.equals("11")){
            m ="November";
        }else{
            m="";
        }
        
        return m;
        
    }
    
    public String getRowProj(String selection){
        
        String row ;
        if(selection.equals("ID")){
            row = "Project.projectId";
        }else if(selection.equals("RCN")){
            row="Project.projectRCN";
        }else if(selection.equals("Acronym")){
            row="Project.projectAcronym";
        }else{
            row= null;
        }
        
        return row;
    }
    
    public boolean searchProj(String selection ,String values,ArrayList<ArrayList<String>> d){
           //String table = getRowLog(selection);
         Statement stmt;
         DbConnection connection = new DbConnection();
         
         String sel;
         sel = getRowProj(selection);
         
    try
    {
        
         stmt =connection.getConnectionDataDB().createStatement(); 
        // String sql = "SELECT * FROM logFile WHERE "+table+"='"+values+"';"; 
         String sqlSelect ="SELECT Project.projectId," +
                            " Project.projectRCN," +
                            " Project.projectAcronym," +
                            " Project.projectStatus," +
                            " Project.projectTitle," +
                            " Project.projectStartDate," +
                            " Project.projectEndDate," +
                            " Project.projectURL," +
                            " Project.projectObjective," +
                            " Project.projectTotalCost," +
                            " Project.projectSubject," +
                            " Project.projectMaxEcContribution," +
                            " FundingScheme.fundingSchemeName," +
                            " Topic.topicName," +
                            " OrgParticipant.orgShortName" +
                            " FROM Project" +
                            " JOIN FundingScheme" +
                            " ON Project.fundingSchemeId=FundingScheme.fundingSchemeId" +
                            " JOIN Topic" +
                            " ON Project.topicId=Topic.topicId" +
                            " JOIN OrgParticipant" +
                            " ON Project.orgId=OrgParticipant.orgId"+" Where "+sel+"='"+values+"';";
         String sqlID="SELECT Project.projectId," +
                            " Project.projectRCN," +
                            " Project.projectAcronym," +
                            " Project.projectStatus," +
                            " Project.projectTitle," +
                            " Project.projectStartDate," +
                            " Project.projectEndDate," +
                            " Project.projectURL," +
                            " Project.projectObjective," +
                            " Project.projectTotalCost," +
                            " Project.projectSubject," +
                            " Project.projectMaxEcContribution," +
                            " FundingScheme.fundingSchemeName," +
                            " Topic.topicName," +
                            " OrgParticipant.orgShortName" +
                            " FROM Project" +
                            " JOIN FundingScheme" +
                            " ON Project.fundingSchemeId=FundingScheme.fundingSchemeId" +
                            " JOIN Topic" +
                            " ON Project.topicId=Topic.topicId" +
                            " JOIN OrgParticipant" +
                            " ON Project.orgId=OrgParticipant.orgId"+" Where "+sel+" ="+values+";";
       ResultSet rs;
         if(selection.equals("ID")){
             stmt.execute(sqlID);
           rs = stmt.getResultSet();
         }else{
              stmt.execute(sqlSelect);
           rs = stmt.getResultSet();
         }
           
           
        try{
         while(rs.next()){
          
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
        }finally{
                  stmt.close();
             }
        
         connection.getConnectionLoginDB().close();
         return true;
    } catch (SQLException ex)
    {
        Logger.getLogger(UserDashboardDbManager.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
    }
}
