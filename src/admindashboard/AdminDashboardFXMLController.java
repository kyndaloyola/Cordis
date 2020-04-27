/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admindashboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import databaseconnection.DbConnection;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import login.LoginDbManager;
import login.LoginFXMain;
import userdashboard.UserDashboardFXMLController;

/**
 * FXML Controller class
 *
 * @author Issam
 */
public class AdminDashboardFXMLController implements Initializable
{

    @FXML
    private AnchorPane navigatorPane;
    @FXML
    private Pane logoutPane;
    @FXML
    private VBox navBarVbox;
    @FXML
    private Pane homeBtn;
    @FXML
    private Text homeText;
    @FXML
    private Text manageUsersText;
    @FXML
    private Text manageLogsText;
    @FXML
    private Pane homePane;
    @FXML
    private Text usersOnlineText;
    @FXML
    private Text newUsersText;
    @FXML
    private Text userOnlineValue;
    @FXML
    private Text newUsersValue;
    @FXML
    private Pane manageUsersPane;
    @FXML
    private TextField searchByTextArea;
    @FXML
    private ImageView searchUserBtn;
    @FXML
    private TextField deleteUserTextArea;
    @FXML
    private Button deleteUserBtn;
    @FXML
    private Pane manageLogPane;
    @FXML
    private TableView<ArrayList<String>> logTable;
    @FXML
    private TableColumn<ArrayList<String>, String> IdLogColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> fNameLog;
    @FXML
    private TableColumn<ArrayList<String>, String> sNameLog;
    @FXML
    private TableColumn<ArrayList<String>, String> emailLog;
    @FXML
    private TableColumn<ArrayList<String>, String> unameLog;
    @FXML
    private TableColumn<ArrayList<String>, String> logInLogColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> logOutLogColumn;
    @FXML
    private TextField searchByLogTextArea;
    @FXML
    private ImageView logSearchBrn;
    @FXML
    private TableView<ArrayList<String>> usersTableView;
    @FXML
    private TableColumn<ArrayList<String>, String> idColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> fNameColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> lNamColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> emailColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> usernameColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> typeColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> logInColumn;
    @FXML
    private Pane manageUsersBtn;
    @FXML
    private Pane manageLogsBtn;
    @FXML

    private Pane menuPane;

    private Pane navigatorPanes;
    @FXML
    private ComboBox<String> manageUserCombobox;
    @FXML

    private ComboBox<String> searchByLogChoiceBox;
    private int userId;
    private String email;
    private String username;
    private String firstName;
    private String surname;
    private String password;
    @FXML
    private Pane logOutButton;

    private int adminId;
    @FXML
    private LineChart<?, ?> userActivityLineChart;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    
   //issam
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        initialiseChart();
        
        setTableViewManageUsers();
        setTableViewLog();
        setOnlineUserNum();
        setNumberOfNewUser();
        
        manageUserCombobox.getItems().addAll("ID","Type","Username","Email");
        manageUserCombobox.setValue("Search BY");
        
        searchByLogChoiceBox.getItems().addAll("ID","Email","Username");
        searchByLogChoiceBox.setValue("Search by");
        
    }

    /**
     * Kynda
     * Initiliase the user activity Line Chart.
     */
    public void initialiseChart(){
        
        AdminDashboardDbManager manager = new AdminDashboardDbManager();
        userActivityLineChart.getData().addAll(manager.intialiseChart());
        
    }

    /**
     * Kynda
     * Set the values in the manager User Table.
     */
    public void setTableViewManageUsers() {        
        ArrayList<ArrayList<String>> data;
        AdminDashboardDbManager manager = new AdminDashboardDbManager();
        data = manager.retrieveUserInfo();
        
        setCellValue(idColumn, 0);
        setCellValue(fNameColumn, 1);
        setCellValue(lNamColumn, 2);
        setCellValue(emailColumn, 3);
        setCellValue(usernameColumn, 4);
        setCellValue(typeColumn, 5);
        setCellValue(logInColumn, 6);
        usersTableView.getItems().addAll(data);
    }
    
    /**
     * Issam
     * Method to set the cell of a table with an ArrayList
     * @param table
     * @param index 
     */
    private void setCellValue(TableColumn<ArrayList<String>, String> table, int index) {
        table.setCellValueFactory((p)->{
            ArrayList<String> x = p.getValue();
            return new SimpleStringProperty(x != null && x.size()>0 ? x.get(index) : "<no name>");
        });
    }
    
    /**
     * Issam
     * Method used to set the values of the activity log table within the Admin Dashboard.
     */
    public void setTableViewLog() { 
       
        ArrayList<ArrayList<String>> data;
        AdminDashboardDbManager manager = new AdminDashboardDbManager();
        data = manager.retrieveLogInfo();
        setCellValue(IdLogColumn, 0);
        setCellValue(fNameLog, 1);
        setCellValue(sNameLog, 2);
        setCellValue(emailLog, 3);
        setCellValue(unameLog, 4);
        setCellValue(logInLogColumn, 5);
        setCellValue(logOutLogColumn, 6);
        logTable.getItems().addAll(data);
    }
    
    /**
     * Issam
     * Method to set the text of the Admin Dashboard displaying the number of Online User
     */
    public void setOnlineUserNum() {
        AdminDashboardDbManager manager = new AdminDashboardDbManager();
        userOnlineValue.setText(manager.setUserOnlineValue());
    }
    
    /**
     * Issam
     * Method to set the text of the Admin Dashboard displaying the number of new User
     */
    public void setNumberOfNewUser() {
        AdminDashboardDbManager manager = new AdminDashboardDbManager();
        newUsersValue.setText(manager.setNumberOfRegistration());
    }

    /**
     * Issam
     * The method is triggered after an event and display the different panes when buttons are clicked
     * @param event - OnClick
     */
    @FXML
    private void changePanel(javafx.scene.input.MouseEvent event)
    {
        if(event.getSource()==homeBtn) {
            homePane.toFront();
            homePane.setVisible(true);
            manageUsersPane.setVisible(false);
            manageLogPane.setVisible(false);
        } else if (event.getSource()==manageUsersBtn) {
            manageUsersPane.toFront();
            manageUsersPane.setVisible(true);
//            homePane.setVisible(false);
            manageLogPane.setVisible(false);
        } else if (event.getSource()==manageLogsBtn) {
            manageLogPane.toFront();
            manageLogPane.setVisible(true);
//            homePane.setVisible(false);
            manageUsersPane.setVisible(false);
        }
    }

    /**
     * Kynda
     * When the search button is clicked this method is fired which searches for the user based on the input
     * @param event 
     */
    @FXML
    private void OnSearchUser(javafx.scene.input.MouseEvent event)
    {
        Alert a = new Alert(AlertType.NONE); 
        String value;
        String choice;
        ArrayList<ArrayList<String>> data = new ArrayList();
        //retives the choice from combobox
        choice= manageUserCombobox.getSelectionModel().getSelectedItem();
        
        value = searchByTextArea.getText();
        //validation 
        if (choice.equals("")){
             a.setAlertType(AlertType.ERROR);
            a.setContentText("Invalid Selection !");
            a.show();
            
        }else if(value.equals("")){
            usersTableView.getItems().clear();
            setTableViewManageUsers();
            
        }else{
            //adds values to the table
             AdminDashboardDbManager manager = new AdminDashboardDbManager();
             
             if(manager.searchUser(value,choice,data)){
             setCellValue(idColumn, 0);
             setCellValue(fNameColumn, 1);
             setCellValue(lNamColumn, 2);
             setCellValue(emailColumn, 3);
             setCellValue(usernameColumn, 4);
             setCellValue(typeColumn, 5);
             setCellValue(logInColumn, 6);
             usersTableView.getItems().clear();
             usersTableView.getItems().addAll(data);
             }else{
                 
                  a.setAlertType(AlertType.ERROR);
                  a.setContentText("Invalid Input !");
                  a.show();
             }
            
        }
        
    }
    
    /**
     * Issam
     * Method building the activity log table.
     * The user select a choice within the combobox.
     * When the user type an event is triggered on key release which fire the method.
     * The data matching the search filter are retrieved from the database and displayed using the method.
     * @param event - OnAction on the ComboBox
     */
    @FXML
    private void onSearchLog(javafx.scene.input.MouseEvent event)
    {
        Alert a = new Alert(AlertType.NONE); 
         String value;
        String choice;
        ArrayList<ArrayList<String>> data = new ArrayList();
        
        choice= searchByLogChoiceBox.getSelectionModel().getSelectedItem();
        
        value = searchByLogTextArea.getText();
        
        if (choice.equals("")){
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Invalid Selection !");
            a.show();
            
        }else if(value.equals("")){
            logTable.getItems().clear();
            setTableViewLog();
            
        }else{
            
             AdminDashboardDbManager manager = new AdminDashboardDbManager();
             if(manager.searchLog(value,choice,data)){
             setCellValue(IdLogColumn, 0);
             setCellValue(fNameLog, 1);
             setCellValue(sNameLog, 2);
             setCellValue(emailLog, 3);
             setCellValue(unameLog, 4);
             setCellValue(logInLogColumn, 5);
             setCellValue(logOutLogColumn, 6);
             
             logTable.getItems().clear();
             logTable.getItems().addAll(data);
             
             }else{
                 
                  a.setAlertType(AlertType.ERROR);
                  a.setContentText("Invalid Input !");
                  a.show();
             }
            
        }
        
    }

    /**
     * Kynda
     * Deletes a user from the database
     * @param event 
     */
    @FXML
    private void onUserDelete(ActionEvent event)
    {

        Alert a = new Alert(AlertType.NONE); 
        AdminDashboardDbManager manager = new AdminDashboardDbManager();
        String userId= deleteUserTextArea.getText();
        if(userId.equals("")){
            a.setAlertType(AlertType.ERROR);
            a.setContentText("You have not entered a valid ID");
            a.show();
            
            
        }else if(manager.checkIfUserExists(userId)){
            
            
            if(manager.deleteUser(userId)){
                
                //validating user input
            if(!manager.checkIfUserExists(userId)){
                 a.setAlertType(AlertType.INFORMATION);
                 a.setContentText("User with ID "+userId+"Sucessfully deleted");
                 a.show();
                 usersTableView.getItems().clear();
                 setTableViewManageUsers();
                 
            }else{
             a.setAlertType(AlertType.ERROR);
             a.setContentText("User with ID "+userId+" Was not deleted!");
             a.show();
            }
            }else{
                
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Invalid ID entered, please enter a valid user ID!");
            a.show();
            }
            
        }else{
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Invalid ID entered, please enter a valid user ID!");
            a.show();
            
        }
    }
    
    public void setIdAdmin(int id) {
        this.adminId=id;
    }

    /**
     * Issam
     * When the admin click the logout button a TimeStamp is added to the database.
     * This method redirect the admin to the Login Window.
     * @param event - OnClick
     */
    @FXML
    private void setLogOut(javafx.scene.input.MouseEvent event) {
        
        try {
            System.out.println("LOG OUT");
            AdminDashboardDbManager manager = new AdminDashboardDbManager();
            manager.setLogOutUser(adminId);
            Stage stage = (Stage) ((Node)logOutButton).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            String url = "/login/LoginFXML.fxml";  //gets the file path
            loader.setLocation(getClass().getResource(url));
            
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserDashboardFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
}
