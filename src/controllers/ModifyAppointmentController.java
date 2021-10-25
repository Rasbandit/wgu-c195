package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Customer;
import models.SchedulingCollections;
import static controllers.MainController.selectedAppointment;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.Appointment;
import utilities.DBConnection;
import utilities.QueryManager;

/**
 * FXML Controller class
 *
 * @author Shawn
 */
public class ModifyAppointmentController implements Initializable {

    @FXML private TableView<Customer> customersTableView;
    @FXML private TableColumn<Customer, String> custNameColumn;
    @FXML private TableColumn<Customer, String> custphoneNumberColumn;
    
    @FXML TextField appWith;
    @FXML TextField appTitleTextField;
    @FXML TextField appTypeTextField;
    @FXML DatePicker appDatePicker;
    @FXML ComboBox<String> appStartComboBox;
    @FXML ComboBox<String> appEndComboBox;
    
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final ZoneId sysid = ZoneId.systemDefault();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    
    /**
     * updates existing appointment in DB and
     * updates appointment list using index
     */
    public void saveButtonPushed(ActionEvent event) throws IOException{
        
        if(validateAppointment()){
            String customerName = appWith.getText();
            String appointmentTitle = appTitleTextField.getText();
            String appointmentType = appTypeTextField.getText();
            String loggedInUser = DBConnection.loggedInUser.getUsername();
            int appointmentId = selectedAppointment.getAppointmentID();
            int appCustomerId;
            if(customersTableView.getSelectionModel().getSelectedItem() == null){
                appCustomerId = selectedAppointment.getAppCustomerID();
            } else{
                appCustomerId = customersTableView.getSelectionModel().getSelectedItem().getCustomerID();
            }

            LocalDate appDate = appDatePicker.getValue();
            LocalTime appStartTime = LocalTime.parse(appStartComboBox.getSelectionModel().getSelectedItem(), timeDTF);
            LocalTime appEndTime = LocalTime.parse(appEndComboBox.getSelectionModel().getSelectedItem(), timeDTF);

            LocalDateTime appStartDT = LocalDateTime.of(appDate, appStartTime);
            LocalDateTime appEndDT = LocalDateTime.of(appDate, appEndTime);
            //UTC timezone
            ZonedDateTime startUTC = appStartDT.atZone(sysid).withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime endUTC = appEndDT.atZone(sysid).withZoneSameInstant(ZoneId.of("UTC"));

            Timestamp startTS = Timestamp.valueOf(startUTC.toLocalDateTime());
            Timestamp endTS = Timestamp.valueOf(endUTC.toLocalDateTime());

            ZonedDateTime localStartZDT = appStartDT.atZone(sysid);
            ZonedDateTime localEndZDT = appEndDT.atZone(sysid);

            //update customerId, title, type, start, end, lastupdateby
            QueryManager.makeQuery("UPDATE appointment SET customerId = " + appCustomerId + ", title = '" + appointmentTitle + "', " +
                    "type = '" + appointmentType + "', start = '"+ startTS +"' , end = '"+ endTS +"', lastUpdate = NOW(), " +
                    "lastupdateby = '" + loggedInUser + "'\nWHERE appointmentId = " + appointmentId + "");


            // create appointment
            Appointment freshAppointment = new Appointment();
            freshAppointment.setAppCustomerID(appCustomerId);
            freshAppointment.setAppointmentTitle(appointmentTitle);
            freshAppointment.setAppointmentType(appointmentType);
            freshAppointment.setAppointmentCustomer(customerName);
            freshAppointment.setAppointmentConsultant(loggedInUser);
            freshAppointment.setAppointmentStart(localStartZDT);
            freshAppointment.setAppointmentEnd(localEndZDT);

            LocalDate now = LocalDate.now();
            LocalDate monthLater = now.plusMonths(1);
            LocalDate weekLater = now.plusDays(7);
            SchedulingCollections.getAllAppointments().clear();
            SchedulingCollections.getWeeklyAppointments().clear();
            SchedulingCollections.getMonthlyAppointments().clear();
            SchedulingCollections.populateAllAppointmentsList();
            SchedulingCollections.populateWeeklyAppointmentsList();
            SchedulingCollections.populateMonthlyAppointmentsList();

            //change scene to main
            Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            Scene mainScene = new Scene(mainViewParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        }
        
    }
    
    private boolean validateAppointment(){
        String appCustomer = appWith.getText();
        String title = appTitleTextField.getText();
        String appType = appTypeTextField.getText();
        LocalDate appDate = appDatePicker.getValue();
        LocalTime appStartTime = LocalTime.parse(appStartComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime appEndTime = LocalTime.parse(appEndComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        
        LocalDateTime appStartDT = LocalDateTime.of(appDate, appStartTime);
        LocalDateTime appEndDT = LocalDateTime.of(appDate, appEndTime);
        //UTC timezone
        ZonedDateTime startUTC = appStartDT.atZone(sysid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = appEndDT.atZone(sysid).withZoneSameInstant(ZoneId.of("UTC"));
        
        String errorMessage = "";
        //check for null fields
        if (appCustomer == null || appCustomer.length() == 0) {
            errorMessage += "Please Select a Customer.\n"; 
        } 
        if (title == null || title.length() == 0) {
            errorMessage += "Please enter an Appointment title.\n"; 
        }
        if (appType == null || appType.length() == 0) {
            errorMessage += "Please select an Appointment type.\n";  
        } 
        if (startUTC == null) {
            errorMessage += "Please select a Start time"; 
        }         
        if (endUTC == null) {
            errorMessage += "Please select an End time.\n"; 
            //checks that Start and End times are not the same 
            //and that end time is not before start time
            } else if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)){
                errorMessage += "End time must be after Start time.\n";
            } else try {
                //checks logged in user's appointments for time conflicts
                if (hasApptConflict(startUTC, endUTC)){
                    errorMessage += "Appointment times conflict with Consultant's existing appointments. Please select a new time.\n";
                }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Please correct invalid Appointment fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }
    
    private boolean hasApptConflict(ZonedDateTime newStart, ZonedDateTime newEnd) throws SQLException {
        
        int appointmentId = selectedAppointment.getAppointmentID();
        String consultant = selectedAppointment.getAppointmentConsultant();
        
        try {
            QueryManager.makeQuery("SELECT * FROM appointment "
                + "WHERE ('"+ Timestamp.valueOf(newStart.toLocalDateTime()) +"' BETWEEN start AND end OR '"+ Timestamp.valueOf(newEnd.toLocalDateTime()) +"' "+
                "BETWEEN start AND end OR '"+ Timestamp.valueOf(newStart.toLocalDateTime()) +"' < start AND '" + Timestamp.valueOf(newEnd.toLocalDateTime()) + "' > end) "
                + "AND (createdBy = '"+ consultant +"' AND appointmentId != " + appointmentId + ")");

            ResultSet rs = QueryManager.getResult();

            if(rs.next()) {
                return true;
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return false;
    }
    
    
    /**
     * This cancels and returns to main screen
     */
    public void cancelButtionPushed(ActionEvent event) throws IOException{
        //change scene to main
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
        Scene mainScene = new Scene(mainViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // populate customer TableView
        custNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        custphoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerPhoneNum()));
        customersTableView.setItems(SchedulingCollections.getAllCustomers());
        appWith.setText(selectedAppointment.getAppointmentCustomer());
        
        //Lambda Expression: adds an eventListener to TableView
        customersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                appWith.setText(newSelection.getCustomerName());
            }
        });
        
        /**
         * set time combo boxes based on normal business hours 8am-5pm
         */
        LocalTime time = LocalTime.of(8, 0);
	do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(15);
	} while(!time.equals(LocalTime.of(17, 15)));
            startTimes.remove(startTimes.size() - 1);
            endTimes.remove(0);
        appDatePicker.setValue(selectedAppointment.getAppointmentStart().toLocalDate());
        appStartComboBox.setItems(startTimes);
        appEndComboBox.setItems(endTimes);
        
        
        appStartComboBox.getSelectionModel().select(LocalTime.of(8, 0).format(timeDTF));
	appEndComboBox.getSelectionModel().select(LocalTime.of(8, 15).format(timeDTF));
        
        
        //set fields to values from selected appointment
        appTitleTextField.setText(selectedAppointment.getAppointmentTitle());
        appTypeTextField.setText(selectedAppointment.getAppointmentType());
        
        appStartComboBox.getSelectionModel().select(selectedAppointment.getAppointmentStart().toLocalTime().format(timeDTF));
        appEndComboBox.getSelectionModel().select(selectedAppointment.getAppointmentEnd().toLocalTime().format(timeDTF));

    }    
    
}
