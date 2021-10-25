package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.Appointment;
import models.AppointmentTypeByMonth;
import models.SchedulingCollections;
import utilities.DBConnection;
import utilities.QueryManager;

/**
 * FXML Controller class
 *
 * @author Shawn
 */
public class ReportsController implements Initializable {
    
    //These items are for the consultant schedule TableView
    @FXML private Label consultantNameLabel;
    @FXML private TableView<Appointment> scheduleTableView;
    @FXML private TableColumn<Appointment, String> schedCustomerColumn;
    @FXML private TableColumn<Appointment, String> schedTitleColumn;
    @FXML private TableColumn<Appointment, String> schedTypeColumn;
    @FXML private TableColumn<Appointment, String> schedStartColumn;
    @FXML private TableColumn<Appointment, String> schedEndColumn;
    
    //These items are for appointment type by month TableView
    @FXML private TableView<AppointmentTypeByMonth> typeByMonthTableView;
    @FXML private TableColumn<AppointmentTypeByMonth, String> byMonthColumn;
    @FXML private TableColumn<AppointmentTypeByMonth, String> byMonthTypeColumn;
    @FXML private TableColumn<AppointmentTypeByMonth, String> byMonthTotalColumn;
    
    
    @FXML private BarChart scoreChart;
    
    private final DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("M/d/yy h:mm a");
    private String consultantName = DBConnection.loggedInUser.getUsername();
    
    public void homeButtonPushed(ActionEvent event) throws IOException{
        //change scene to main
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
        Scene mainScene = new Scene(mainViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
    
    public void populateScheduleTable(){
        consultantNameLabel.setText("Schedule For Consultant: " + consultantName);
        schedCustomerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentCustomer()));
        schedTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentTitle()));
        schedTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentType()));
        schedStartColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentStart().format(formatDateTime)));
        schedEndColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getAppointmentEnd().format(formatDateTime)));
        scheduleTableView.setItems(SchedulingCollections.getConsultantAppointments());
    }
    
    public void populateAppTypeByMonthTable(){
        byMonthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMonth()));
        byMonthTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        byMonthTotalColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmount()));
        typeByMonthTableView.setItems(SchedulingCollections.getAppointmentTypesByMonth());
    }

    public void populateScoreChart(){
        ObservableList<XYChart.Data<String, Integer>> data = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        
        try {
            QueryManager.makeQuery("SELECT userName, COUNT(userName) AS Appointments\n" +
                "FROM appointment, user\n" +
                "WHERE appointment.userId = user.userId\n" +
                "GROUP BY userName");
            ResultSet rs = QueryManager.getResult();
            
            while(rs.next()){
                String user = rs.getString("userName");
                Integer count = rs.getInt("Appointments");
                data.add(new XYChart.Data<>(user, count));
            }
        } catch (SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        series.getData().addAll(data);
        scoreChart.getData().add(series);
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // populate consultant schedule TableView
        populateScheduleTable();
        
        // populate appointment type by month TableView
        populateAppTypeByMonthTable();
        
        // populate consultant score chart
        populateScoreChart();
    }    
    
}
