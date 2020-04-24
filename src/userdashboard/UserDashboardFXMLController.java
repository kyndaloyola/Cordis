/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userdashboard;

import com.jfoenix.controls.JFXCheckBox;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kynda
 */
public class UserDashboardFXMLController implements Initializable {

    @FXML
    private BarChart<?, ?> projectsPerYearChart;
    @FXML
    private NumberAxis NoOfProjectsXAxis;
    @FXML
    private CategoryAxis yearsYAxis;

    HashMap<String, Double> values;

    XYChart.Series seriesLineChartHome = new XYChart.Series();
    XYChart.Series seriesLineChartDetailsPane = new XYChart.Series();
    XYChart.Series series1 = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();

    XYChart.Series seriesBarChartHome = new XYChart.Series();
    XYChart.Series seriesBarChartDetailsPane = new XYChart.Series();
    @FXML
    private MenuItem settingsItem;
    @FXML
    private Button kyndaBtn;
    @FXML
    private Pane kyndaPane;
    @FXML
    private Button SubmitKyndaGraph;
    @FXML
    private Pane issamPane;
    private LineChart<?, ?> lineChartIssam;
    @FXML
    private Button issamBtn;
    private AnchorPane settingsMainPane;
    private AnchorPane organisationsPane1;

    @FXML
    private Button trungBtn;
    @FXML
    private Pane trungPane;
    @FXML
    private StackedAreaChart<BigDecimal, Double> areaChart;
    @FXML
    private JFXCheckBox costs;
    @FXML
    private JFXCheckBox contributions;
    @FXML
    private Button refreshBtn;
    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    @FXML
    private BubbleChart<Integer, Integer> bubbleChartIssam;
    @FXML
    private NumberAxis numOfPro;
    @FXML
    private NumberAxis numOfOrg;

    @FXML
    private RadioButton sup75M;
    @FXML
    private RadioButton sup50M;
    @FXML
    private RadioButton sup25M;
    @FXML
    private RadioButton allFund;
    @FXML
    private RadioButton sup15M;
    @FXML
    private ToggleGroup fundRangeToggleGroup;
    @FXML
    private Pane statisticsPane;
    @FXML
    private Pane OrganisationsPane;
    @FXML
    private Pane projectsPane;
    @FXML
    private Pane statisticsMenuButton;
    @FXML
    private Pane orgMenuButton;
    @FXML
    private Pane projectMenuButton;
    @FXML
    private ComboBox<String> yearSelectorKynda;
    private Label homeBtn;
    @FXML
    private Pane homePane;
    private Label welcomeText;
    private int userId;
    private String email;
    private String username;
    private String password;
    private String fname;
    private String lname;

    @FXML
    private TextField emailTextfield;

    @FXML
    private TextField usernameTextfield;

    @FXML
    private TextField fnameTextfield;

    @FXML
    private TextField lnameTextfield;

    @FXML
    private PasswordField password2Field;

    @FXML
    private PasswordField password1Field;

    @FXML
    private TableView<ArrayList<String>> organisationsTableView;
    @FXML
    private TableColumn<ArrayList<String>, String> vatOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> urlOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> streetOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> shortNameOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> pCodeOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> nameOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> idOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> endOfPartOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> countryOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> cityOrgColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> aTypeOrgColumn;

    @FXML
    private TableView<ArrayList<String>> projectTableView;

    @FXML
    private TableColumn<ArrayList<String>, String> projectIdColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectRCNColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectAcronymColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectStatusColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectTitleColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectStartDateColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectEndDateColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectURLColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectObjectiveColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectTotalCostColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectSubjectColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectMaxEcContribution;
    @FXML
    private TableColumn<ArrayList<String>, String> projectFSchemeColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> topicProjectColumn;
    @FXML
    private TableColumn<ArrayList<String>, String> projectCoordinatorColumn;

    @FXML
    private Text textOrgId;
    @FXML
    private Text textOrgName;
    @FXML
    private Text textOrgActivityType;
    @FXML
    private Text textOrgPostCode;
    @FXML
    private Text textOrgStreet;
    @FXML
    private Text textOrgCity;
    @FXML
    private Text textOrgCountry;
    @FXML
    private Pane projectDetailsPane;
    @FXML
    private Text projectTitleText;
    @FXML
    private Text projectDescripText;
    @FXML
    private Text projectAcronymText;
    @FXML
    private Text projectStartDatetext;
    @FXML
    private Text projectEndDatetext;
    @FXML
    private Text projectBudgetText;
    @FXML
    private Text projectEcContributionText;
    @FXML
    private StackedAreaChart<BigDecimal, Double> trungReducedGraph;
    @FXML
    private BarChart<?, ?> kyndaReducedGraph;
    @FXML
    private BubbleChart<?, ?> issamReducedGraph;
    @FXML
    private ImageView searchProjectBtn;
    @FXML
    private ComboBox<String> searchByProjects;
    @FXML
    private TextField inputTextSearchProj;
    @FXML
    private VBox graphMenuItems;
    @FXML
    private Pane HomeBtn;
    @FXML
    private AnchorPane settingsAnchorPane;
    @FXML
    private Pane settingsPane;
    @FXML
    private Text orgCoordinName;
    @FXML
    private Text coordinatorText;
    @FXML
    private ComboBox<String> orgSearchSelector;
    @FXML
    private TextField searchFieldOrg;
    @FXML
    private MenuItem logOutItem;
    @FXML
    private Text textOrgVATNum;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createChartKyndas();
        createAreaChart();
        createChartIssam(0, Integer.MAX_VALUE);
        setOrganisationTableValues();
        setProjectTableValues();

        initaliseHomeCharts();

        searchByProjects.getItems().addAll("All", "ID", "RCN", "Acronym");
        orgSearchSelector.getItems().addAll("Id", "Name", "Activity type", "VAT Number", "Country");
        homePane.setVisible(true);
        kyndaPane.setVisible(false);
        issamPane.setVisible(false);
        trungPane.setVisible(false);
        statisticsPane.setVisible(false);
        OrganisationsPane.setVisible(false);
        projectsPane.setVisible(false);
        graphMenuItems.setVisible(false);
        settingsPane.setVisible(false);
        settingsAnchorPane.setVisible(false);
        projectDetailsPane.setVisible(false);

    }

    public void initaliseHomeCharts() {
        UserDashboardDbManager manager = new UserDashboardDbManager();
        kyndaReducedGraph.getData().addAll(manager.intialiseChartKyndas("All"));

        ArrayList<XYChart.Series> bubbleChartSeries = manager.intialiseChartIssam(0, Integer.MAX_VALUE);
        for (int i = 0; i < bubbleChartSeries.size(); i++) {
            issamReducedGraph.getData().add(bubbleChartSeries.get(i));
        }

        XYChart.Series<BigDecimal, Double> averageCost = manager.AreaChartSeries1();
        XYChart.Series<BigDecimal, Double> averageContribution = manager.AreaChartSeries2();
        trungReducedGraph.getData().addAll(averageCost, averageContribution);
        createCostTooltips(averageCost);
        createContributionTooltips(averageContribution);

    }

    public void setUserDetails(int userId, String username, String email, String password, String fname, String lname) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        fnameTextfield.setText(fname);
        lnameTextfield.setText(lname);
        usernameTextfield.setText(username);
        emailTextfield.setText(email);
    }

    private void createChartKyndas() {
        UserDashboardDbManager manager = new UserDashboardDbManager();
        yearSelectorKynda.getItems().addAll("All", "2014", "2015", "2016", "2017", "2018", "2019", "2020");
        projectsPerYearChart.getData().addAll(manager.intialiseChartKyndas("All"));
         for (Series  <?, ?> serie: projectsPerYearChart.getData()){
            for (Data<?, ?> item: serie.getData()){
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(item.getYValue().toString()+" Projects");
                        Tooltip.install(item.getNode(), tooltip);
               
            }
        }  
    }

    private void createChartIssam(int lowerBound, int upperBound) {
        UserDashboardDbManager manager = new UserDashboardDbManager();
        ArrayList<XYChart.Series> bubbleChartSeries = manager.intialiseChartIssam(lowerBound, upperBound);
        for (int i = 0; i < bubbleChartSeries.size(); i++) {
            bubbleChartIssam.getData().add(bubbleChartSeries.get(i));
        }
    }

    @FXML
    private void applyChangesToBubbleChart(MouseEvent event) {
        if (sup75M.isSelected()) {
            bubbleChartIssam.getData().clear();
            createChartIssam(50000000, Integer.MAX_VALUE);
        } else if (sup50M.isSelected()) {
            bubbleChartIssam.getData().clear();
            createChartIssam(20000000, 49999999);
        } else if (sup25M.isSelected()) {
            bubbleChartIssam.getData().clear();
            createChartIssam(5000000, 19999999);
        } else if (sup15M.isSelected()) {
            bubbleChartIssam.getData().clear();
            createChartIssam(0, 4999999);
        } else {
            bubbleChartIssam.getData().clear();
            createChartIssam(0, Integer.MAX_VALUE);
        }

    }

    private void createAreaChart() {
        costs.setSelected(true); //checkbox is marked green
        contributions.setSelected(true); //checkbox is marked green
        UserDashboardDbManager manager = new UserDashboardDbManager();
        XYChart.Series<BigDecimal, Double> averageCost = manager.AreaChartSeries1();
        XYChart.Series<BigDecimal, Double> averageContribution = manager.AreaChartSeries2();
        areaChart.getData().addAll(averageCost, averageContribution);
        createCostTooltips(averageCost);
        createContributionTooltips(averageContribution);

    }

    void createCostTooltips(XYChart.Series averageCost) {
        XYChart.Series<BigDecimal, Double> series = averageCost;
        for (Data<BigDecimal, Double> data : series.getData()) {
            Tooltip tooltip = new Tooltip();
            StringBuilder sb = new StringBuilder();
            sb.append("Year: ").append(data.getXValue());
            sb.append("\n");
            sb.append("Average Total Project Cost: €").append(Math.round(data.getYValue()));
            tooltip.setText(sb.toString());
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    public void createContributionTooltips(XYChart.Series averageContribution) {
        XYChart.Series<BigDecimal, Double> series = averageContribution;
        for (Data<BigDecimal, Double> data : series.getData()) {
            Tooltip tooltip = new Tooltip();
            StringBuilder sb = new StringBuilder();
            sb.append("Year: ").append(data.getXValue());
            sb.append("\n");
            sb.append("Average Max EC Contribution: €").append(Math.round(data.getYValue()));
            tooltip.setText(sb.toString());
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    @FXML
    public void refreshAreaChart(ActionEvent event) {
        XYChart.Series<BigDecimal, Double> averageCost;
        XYChart.Series<BigDecimal, Double> averageContribution;
        UserDashboardDbManager manager = new UserDashboardDbManager();
        areaChart.getData().removeAll(areaChart.getData());

        if (costs.isSelected() && contributions.isSelected()) {
            averageCost = manager.AreaChartSeries1();
            averageContribution = manager.AreaChartSeries2();
            areaChart.getData().addAll(averageCost, averageContribution);
            createCostTooltips(averageCost);
            createContributionTooltips(averageContribution);
        } else if (costs.isSelected()) {
            averageCost = manager.AreaChartSeries1();
            areaChart.getData().add(averageCost);
            createCostTooltips(averageCost);
        } else if (contributions.isSelected()) {
            averageContribution = manager.AreaChartSeries2();
            areaChart.getData().add(averageContribution);
            createContributionTooltips(averageContribution);
        }
    }

    public void refreshButtonEnabled(ActionEvent event) {
        if (!(costs.isSelected() || contributions.isSelected())) {
            refreshBtn.setDisable(true); //disables button if both checkboxes are unselected to prevent running queries when the refresh button is pressed
        } else {
            refreshBtn.setDisable(false);
        }

    }

    @FXML
    private void onKyndaBtn(ActionEvent event) {

    }

    @FXML
    private void changeGraph(MouseEvent event) {
        if (event.getSource() == kyndaBtn) {
            kyndaPane.setVisible(true);
            issamPane.setVisible(false);
            trungPane.setVisible(false);
            OrganisationsPane.setVisible(false);
            projectsPane.setVisible(false);
            statisticsPane.setVisible(false);
            homePane.setVisible(false);
            settingsPane.setVisible(false);
            settingsAnchorPane.setVisible(false);
            graphMenuItems.setVisible(true);
            projectDetailsPane.setVisible(false);
        } else if (event.getSource() == issamBtn) {
            issamPane.setVisible(true);
            kyndaPane.setVisible(false);
            trungPane.setVisible(false);
            OrganisationsPane.setVisible(false);
            projectsPane.setVisible(false);
            statisticsPane.setVisible(false);
            homePane.setVisible(false);
            settingsPane.setVisible(false);
            settingsAnchorPane.setVisible(false);
            graphMenuItems.setVisible(true);
            projectDetailsPane.setVisible(false);
        } else if (event.getSource() == trungBtn) {
            trungPane.setVisible(true);
            kyndaPane.setVisible(false);
            issamPane.setVisible(false);
            OrganisationsPane.setVisible(false);
            projectsPane.setVisible(false);
            statisticsPane.setVisible(false);
            homePane.setVisible(false);
            settingsPane.setVisible(false);
            settingsAnchorPane.setVisible(false);
            graphMenuItems.setVisible(true);
            projectDetailsPane.setVisible(false);
        } else if (event.getSource() == statisticsMenuButton) {
            kyndaPane.setVisible(false);
            issamPane.setVisible(false);
            trungPane.setVisible(false);
            OrganisationsPane.setVisible(false);
            projectsPane.setVisible(false);
            homePane.setVisible(false);
            statisticsPane.setVisible(true);
            settingsPane.setVisible(false);
            settingsAnchorPane.setVisible(false);
            graphMenuItems.setVisible(true);
            projectDetailsPane.setVisible(false);
        } else if (event.getSource() == orgMenuButton) {
            kyndaPane.setVisible(false);
            issamPane.setVisible(false);
            trungPane.setVisible(false);
            projectsPane.setVisible(false);
            statisticsPane.setVisible(false);
            homePane.setVisible(false);
            OrganisationsPane.setVisible(true);
            settingsPane.setVisible(false);
            settingsAnchorPane.setVisible(false);
            graphMenuItems.setVisible(false);
            projectDetailsPane.setVisible(false);
        } else if (event.getSource() == projectMenuButton) {
            kyndaPane.setVisible(false);
            issamPane.setVisible(false);
            trungPane.setVisible(false);
            statisticsPane.setVisible(false);
            OrganisationsPane.setVisible(false);
            homePane.setVisible(false);
            projectsPane.setVisible(true);
            settingsPane.setVisible(false);
            settingsAnchorPane.setVisible(false);
            graphMenuItems.setVisible(false);
            projectDetailsPane.setVisible(false);
        } else if (event.getSource() == HomeBtn) {
            homePane.setVisible(true);
            kyndaPane.setVisible(false);
            issamPane.setVisible(false);
            trungPane.setVisible(false);
            statisticsPane.setVisible(false);
            OrganisationsPane.setVisible(false);
            projectsPane.setVisible(false);
            settingsPane.setVisible(false);
            settingsAnchorPane.setVisible(false);
            graphMenuItems.setVisible(false);
            projectDetailsPane.setVisible(false);
        }
    }

    private void onOrgClick(ActionEvent event) {
        organisationsPane1.toFront();
    }

    @FXML
    private void changeColorBackgroundExited(MouseEvent event) {
        BackgroundFill background_fill = new BackgroundFill(Color.rgb(125, 177, 209), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        if (event.getSource() == statisticsMenuButton) {
            statisticsMenuButton.setBackground(background);
        } else if (event.getSource() == orgMenuButton) {
            orgMenuButton.setBackground(background);
        } else if (event.getSource() == projectMenuButton) {
            projectMenuButton.setBackground(background);
        }
    }

    @FXML
    private void changeColorBackgroundEntered(MouseEvent event) {
        BackgroundFill background_fill = new BackgroundFill(Color.rgb(53, 113, 151), CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        if (event.getSource() == statisticsMenuButton) {
            statisticsMenuButton.setBackground(background);
        } else if (event.getSource() == orgMenuButton) {
            orgMenuButton.setBackground(background);
        } else if (event.getSource() == projectMenuButton) {
            projectMenuButton.setBackground(background);
        }
    }

    @FXML
    private void OnSubmitKynda(ActionEvent event) {
        UserDashboardDbManager manager = new UserDashboardDbManager();
        String choice = yearSelectorKynda.getSelectionModel().getSelectedItem();

        projectsPerYearChart.getData().clear();
        projectsPerYearChart.getData().addAll(manager.intialiseChartKyndas(choice));

         for (Series  <?, ?> serie: projectsPerYearChart.getData()){
            for (Data<?, ?> item: serie.getData()){
                        Tooltip tooltip = new Tooltip();
                        tooltip.setText(item.getYValue().toString()+" Projects");
                        Tooltip.install(item.getNode(), tooltip);
               
            }
        }  
        

    }

    public void setOrganisationTableValues() {
        ArrayList<ArrayList<String>> data;
        UserDashboardDbManager manager = new UserDashboardDbManager();
        data = manager.getOrganisationDetails();

        setCellValue(idOrgColumn, 0);
        setCellValue(endOfPartOrgColumn, 1);
        setCellValue(shortNameOrgColumn, 2);
        setCellValue(nameOrgColumn, 3);
        setCellValue(urlOrgColumn, 4);
        setCellValue(vatOrgColumn, 5);
        setCellValue(aTypeOrgColumn, 6);
        setCellValue(pCodeOrgColumn, 7);
        setCellValue(streetOrgColumn, 8);
        setCellValue(cityOrgColumn, 9);
        setCellValue(countryOrgColumn, 10);
        organisationsTableView.getItems().addAll(data);
    }

    public void setProjectTableValues() {
        ArrayList<ArrayList<String>> data;
        UserDashboardDbManager manager = new UserDashboardDbManager();
        data = manager.getProjectDetails();

        setCellValue(projectIdColumn, 0);
        setCellValue(projectRCNColumn, 1);
        setCellValue(projectAcronymColumn, 2);
        setCellValue(projectStatusColumn, 3);
        setCellValue(projectTitleColumn, 4);
        setCellValue(projectStartDateColumn, 5);
        setCellValue(projectEndDateColumn, 6);
        setCellValue(projectURLColumn, 7);
        setCellValue(projectObjectiveColumn, 8);
        setCellValue(projectTotalCostColumn, 9);
        setCellValue(projectSubjectColumn, 10);
        setCellValue(projectMaxEcContribution, 11);
        setCellValue(projectFSchemeColumn, 12);
        setCellValue(topicProjectColumn, 13);
        setCellValue(projectCoordinatorColumn, 14);
        projectTableView.getItems().addAll(data);
    }

    private void setCellValue(TableColumn<ArrayList<String>, String> table, int index) {
        table.setCellValueFactory((p) -> {
            ArrayList<String> x = p.getValue();
            return new SimpleStringProperty(x != null && x.size() > 0 ? x.get(index) : "<no name>");
        });
    }

    @FXML
    public void displayProjectValues() {
        kyndaPane.setVisible(false);
        issamPane.setVisible(false);
        trungPane.setVisible(false);
        statisticsPane.setVisible(false);
        OrganisationsPane.setVisible(false);
        homePane.setVisible(false);
        projectsPane.setVisible(false);
        projectDetailsPane.setVisible(true);

        int selection = projectTableView.getSelectionModel().getSelectedCells().get(0).getRow();

        UserDashboardDbManager manager = new UserDashboardDbManager();
        ArrayList<ArrayList<String>> data = manager.getProjectDetails();

        //textId.setText(data.get(selection).get(0));
        int id = Integer.parseInt(data.get(selection).get(0));
        String coordinator= manager.getCoordinator(id);
        
        if(coordinator.equals(null)){
             coordinatorText.setVisible(false);
             orgCoordinName.setVisible(false);
         }else{
             coordinatorText.setVisible(true);
             orgCoordinName.setVisible(true);
             orgCoordinName.setText(coordinator);
         }
        
        double ecCon  = Double.parseDouble(data.get(selection).get(11));
        double budget = Double.parseDouble(data.get(selection).get(9));
        String bg = ""+String.valueOf(budget);
        String ec = ""+String.valueOf(ecCon);
        projectAcronymText.setText(data.get(selection).get(2));
        projectTitleText.setText(data.get(selection).get(4));
        projectStartDatetext.setText(data.get(selection).get(5));
        projectEndDatetext.setText(data.get(selection).get(6));
        projectDescripText.setText(data.get(selection).get(8));
        projectBudgetText.setText(bg);
        projectEcContributionText.setText(ec);
        
        
    }

    @FXML
    public void displayOrganisationValues() {
        int selection = organisationsTableView.getSelectionModel().getSelectedCells().get(0).getRow();

        UserDashboardDbManager manager = new UserDashboardDbManager();
        ArrayList<ArrayList<String>> data = manager.getOrganisationDetails();

        textOrgId.setText(data.get(selection).get(0));
        textOrgName.setText(data.get(selection).get(3));
        textOrgActivityType.setText(data.get(selection).get(6));
        textOrgPostCode.setText(data.get(selection).get(7));
        textOrgStreet.setText(data.get(selection).get(8));
        textOrgCity.setText(data.get(selection).get(9));
        textOrgCountry.setText(data.get(selection).get(10));
    }

    @FXML
    private void onTrungClick(MouseEvent event) {
        trungPane.setVisible(true);
        kyndaPane.setVisible(false);
        issamPane.setVisible(false);
        OrganisationsPane.setVisible(false);
        projectsPane.setVisible(false);
        statisticsPane.setVisible(false);
        homePane.setVisible(false);
        settingsPane.setVisible(false);
        settingsAnchorPane.setVisible(false);
        graphMenuItems.setVisible(true);
    }

    @FXML
    private void OnKyndaClick(MouseEvent event) {
        kyndaPane.setVisible(true);
        issamPane.setVisible(false);
        trungPane.setVisible(false);
        OrganisationsPane.setVisible(false);
        projectsPane.setVisible(false);
        statisticsPane.setVisible(false);
        homePane.setVisible(false);
        settingsPane.setVisible(false);
        settingsAnchorPane.setVisible(false);
        graphMenuItems.setVisible(true);
    }

    @FXML
    private void onIssamClick(MouseEvent event) {
        issamPane.setVisible(true);
        kyndaPane.setVisible(false);
        trungPane.setVisible(false);
        OrganisationsPane.setVisible(false);
        projectsPane.setVisible(false);
        statisticsPane.setVisible(false);
        homePane.setVisible(false);
        graphMenuItems.setVisible(true);
    }

    @FXML
    private void onProjectSearch(MouseEvent event) {
        String sel;
        String userInput;
        Alert a = new Alert(AlertType.NONE);
        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

        UserDashboardDbManager manager = new UserDashboardDbManager();

        sel = searchByProjects.getSelectionModel().getSelectedItem();
        userInput = inputTextSearchProj.getText();

        if (sel.equals("")) {
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Invalid Selection !");
            a.show();
        } else if (userInput.equals("") && sel.equals("")) {
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Invalid Input !");
            a.show();
        } else if (sel.equals("All")) {
            setProjectTableValues();
        } else {
            projectTableView.getItems().clear();

            if (manager.searchProj(sel, userInput, data)) {

                setCellValue(projectIdColumn, 0);
                setCellValue(projectRCNColumn, 1);
                setCellValue(projectAcronymColumn, 2);
                setCellValue(projectStatusColumn, 3);
                setCellValue(projectTitleColumn, 4);
                setCellValue(projectStartDateColumn, 5);
                setCellValue(projectEndDateColumn, 6);
                setCellValue(projectURLColumn, 7);
                setCellValue(projectObjectiveColumn, 8);
                setCellValue(projectTotalCostColumn, 9);
                setCellValue(projectSubjectColumn, 10);
                setCellValue(projectMaxEcContribution, 11);
                setCellValue(projectFSchemeColumn, 12);
                setCellValue(topicProjectColumn, 13);
                setCellValue(projectCoordinatorColumn, 14);

                projectTableView.getItems().addAll(data);
            } else {
                a.setAlertType(AlertType.ERROR);
                a.setContentText("Invalid user Input !");
                a.show();
            }

            //data = manager.getProjectDetails();
        }
    }

    @FXML
    private void OnSettingsClick(ActionEvent event) {
        homePane.setVisible(false);
        kyndaPane.setVisible(false);
        issamPane.setVisible(false);
        trungPane.setVisible(false);
        statisticsPane.setVisible(false);
        OrganisationsPane.setVisible(false);
        projectsPane.setVisible(false);
        graphMenuItems.setVisible(false);
        projectDetailsPane.setVisible(false);
        settingsPane.setVisible(true);
        settingsAnchorPane.setVisible(true);
    }

    @FXML
    void resetDetails(ActionEvent event) {
        fnameTextfield.setText(fname);
        lnameTextfield.setText(lname);
        usernameTextfield.setText(username);
        emailTextfield.setText(email);
    }

    @FXML
    void saveProfile(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR); //sets the alert for incoming errors
        alert.setTitle("Error Dialog");
        StringBuilder errorMessage = new StringBuilder();
        String namePattern = "(?i)(^[a-z]+)[a-z .,-]((?! .,-)$){1,50}$"; //validation for first/last name
        String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{6,25}$"; //validation for username
        String emailPattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; //validation for email

        if (fnameTextfield.getText().equals(fname) && lnameTextfield.getText().equals(lname)
                && usernameTextfield.getText().equals(username) && emailTextfield.getText().equals(email)) { //if fields havent been changed, do nothing
        } else if (!fnameTextfield.getText().matches(namePattern) || !lnameTextfield.getText().matches(namePattern) //if the fields contain invalid characters
                || !usernameTextfield.getText().matches(usernamePattern) || !emailTextfield.getText().matches(emailPattern)) {

            alert.setHeaderText("The following field(s) have invalid characters:");

            if (!fnameTextfield.getText().matches(namePattern)) {
                errorMessage.append("First Name");
                errorMessage.append("\n");
            }
            if (!lnameTextfield.getText().matches(namePattern)) {
                errorMessage.append("Last Name");
                errorMessage.append("\n");
            }
            if (!usernameTextfield.getText().matches(usernamePattern)) {
                errorMessage.append("Username");
                errorMessage.append("\n");
            }
            if (!emailTextfield.getText().matches(emailPattern)) {
                errorMessage.append("Email");
                errorMessage.append("\n");
            }

            alert.setContentText(errorMessage.toString()); //prints out names of fields with invalid characters
            alert.showAndWait();
        } else {
            UserDashboardDbManager DbManager = new UserDashboardDbManager();
            boolean usernameFound;
            boolean emailFound;
            if (!usernameTextfield.getText().equals(username)) { //checks if username already exists
                usernameFound = DbManager.checkUsername(usernameTextfield.getText());
                if (usernameFound) {
                    return;
                }
            }
            if (!emailTextfield.getText().equals(email)) { //checks if email already exists
                emailFound = DbManager.checkEmail(emailTextfield.getText());
                if (emailFound) {
                    return;
                }
            } //will update profile from new fields
            DbManager.updateProfile(userId, fnameTextfield.getText(), lnameTextfield.getText(), usernameTextfield.getText(), emailTextfield.getText());
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("User Profile has been updated!");
            alert.showAndWait();
            this.fname = fnameTextfield.getText();
            this.lname = lnameTextfield.getText();
            this.username = usernameTextfield.getText();
            this.email = emailTextfield.getText();
        }
    }

    @FXML
    void changePassword(ActionEvent event) {
        String passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,128})"; //validation for password
        Alert alert = new Alert(Alert.AlertType.ERROR); //sets the alert for incoming errors
        StringBuilder errorMessage = new StringBuilder();

        if (password1Field.getText().equals(password) && password2Field.getText().equals(password)) { //if password fields are empty
            alert.setHeaderText("You cannot change your password!");
            alert.setContentText("The password is the same for your login credentials.");
            alert.show();
        } else if (!password1Field.getText().equals(password2Field.getText())) { //if password fields dont match
            alert.setHeaderText("Passwords do not match!");
            alert.setContentText("Please re-check your password.");
            alert.showAndWait();
        } else if (!password1Field.getText().matches(passwordPattern) || !password2Field.getText().matches(passwordPattern)) { //if password is in a invalid format
            alert.setHeaderText("Your password must contain:");
            errorMessage.append("At least 1 uppercase letter (A-Z)\n");
            errorMessage.append("At least one number (0-9\n)");
            errorMessage.append("At least 6 characters");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        } else { //passes validation test
            UserDashboardDbManager DbManager = new UserDashboardDbManager();
            DbManager.updatePassword(userId, password1Field.getText()); //updates password in database
            alert = new Alert(Alert.AlertType.INFORMATION); //new alert
            alert.setHeaderText("Your password has been changed!");
            alert.setContentText("Your new password is: " + password1Field.getText());
            alert.showAndWait();
            this.password = password1Field.getText();
        }
    }

    @FXML
    private void onSearchOrganisation(KeyEvent event) {
        //orgSearchSelector.getItems().addAll("All", "Id", "Name", "Activity type", "VAT Number", "Country");
        organisationsTableView.getItems().clear();
        UserDashboardDbManager manager = new UserDashboardDbManager();
        String selector = orgSearchSelector.getSelectionModel().getSelectedItem();
        String filter = searchFieldOrg.getText();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        
        if(!filter.equals("")) {
            if("Id".equals(selector)) {
                data = manager.searchOrganisation("OrgParticipant.orgId", filter);
            } else if ("Name".equals(selector)) {
                data = manager.searchOrganisation("OrgParticipant.orgName", "'"+filter+"'");
            } else if ("Activity type".equals(selector)) {
                data = manager.searchOrganisation("OrgParticipant.orgActivityType", "'"+filter+"'");
            } else if ("VAT Number".equals(selector)) {
                data = manager.searchOrganisation("OrgParticipant.orgVATNum", "'"+filter+"'");
            } else if ("Country".equals(selector)) {
                data = manager.searchOrganisation("Country.countryName", "'"+filter+"'");
            }
            
            setCellValue(idOrgColumn, 0);
            setCellValue(endOfPartOrgColumn, 1);
            setCellValue(shortNameOrgColumn, 2);
            setCellValue(nameOrgColumn, 3);
            setCellValue(urlOrgColumn, 4);
            setCellValue(vatOrgColumn, 5);
            setCellValue(aTypeOrgColumn, 6);
            setCellValue(pCodeOrgColumn, 7);
            setCellValue(streetOrgColumn, 8);
            setCellValue(cityOrgColumn, 9);
            setCellValue(countryOrgColumn, 10);
            organisationsTableView.getItems().addAll(data);
        } else {
            setOrganisationTableValues();
        }
        
    }

    @FXML
    private void OnLogOut(ActionEvent event) {
        try {
            System.out.println("LOG OUT");
            UserDashboardDbManager manager = new UserDashboardDbManager();
            manager.setLogOutUser(userId);
            Stage stage = (Stage) ((Node)textOrgVATNum).getScene().getWindow();
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