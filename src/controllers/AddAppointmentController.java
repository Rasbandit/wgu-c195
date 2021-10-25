package controllers;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Appointment;
import models.Customer;
import models.SchedulingCollections;
import utilities.DBConnection;
import utilities.QueryManager;

/**
 * FXML Controller class
 *
 * @author Shawn
 */
public class AddAppointmentController implements Initializable {

    @FXML private TableView<Customer> customersTableView;
    @FXML private TableColumn<Customer, String> custNameColumn;
    @FXML private TableColumn<Customer, String> custphoneNumberColumn;
    
    @FXML TextField appWithTextField;
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
     * Saves new appointment to DB and updates appointment list
     */
    public void saveButtonPushed(ActionEvent event) throws IOException{
        
        if(validateAppointment()){
            String title = appTitleTextField.getText();
            String appType = appTypeTextField.getText();
            Customer withCustomer = customersTableView.getSelectionModel().getSelectedItem();
            int customerId = withCustomer.getCustomerID();
            String customerName = withCustomer.getCustomerName();
            String loggedInUser = DBConnection.loggedInUser.getUsername();
            int userId = DBConnection.loggedInUser.getuserID();

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

            //insert appointment into DB
            QueryManager.makeQuery("INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdateBy)\n" +
                "VALUES (" + customerId + ", " + userId + ", '" + title + "', '', '', '', '" + appType + "', '', '"+ startTS +"', '"+ endTS +"', NOW(), '" + loggedInUser + "', '" + loggedInUser + "')");

            Appointment newAppointment = new Appointment();
            newAppointment.setAppCustomerID(customerId);
            newAppointment.setAppointmentTitle(title);
            newAppointment.setAppointmentType(appType);
            newAppointment.setAppointmentCustomer(customerName);
            newAppointment.setAppointmentConsultant(loggedInUser);
            newAppointment.setAppointmentStart(localStartZDT);
            newAppointment.setAppointmentEnd(localEndZDT);

            LocalDate now = LocalDate.now();
            LocalDate monthLater = now.plusMonths(1);
            LocalDate weekLater = now.plusDays(7);

            SchedulingCollections.addAppointmentAll(newAppointment);

            if(appDate.isAfter(now.minusDays(1)) && appDate.isBefore(monthLater)){
                //add appointment to monthly list
                SchedulingCollections.addAppointmentMonthly(newAppointment);
            }

            if(appDate.isEqual(now.minusDays(1)) && appDate.isBefore(weekLater)){
                //add appointment to weekly list
                SchedulingCollections.addAppointmentWeekly(newAppointment);
            }
            //add appointment to all appointments list
            System.out.println(SchedulingCollections.getAllAppointments());

            //change scene to main
            Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            Scene mainScene = new Scene(mainViewParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        }
        
    }
    
    private boolean validateAppointment(){
        
        String appCustomer = appWithTextField.getText();
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
        
        String consultant = DBConnection.loggedInUser.getUsername();
        
        try {
            QueryManager.makeQuery("SELECT * FROM appointment "
                + "WHERE ('"+ Timestamp.valueOf(newStart.toLocalDateTime()) +"' BETWEEN start AND end OR '"+ Timestamp.valueOf(newEnd.toLocalDateTime()) +"' "+
                "BETWEEN start AND end OR '"+ Timestamp.valueOf(newStart.toLocalDateTime()) +"' < start AND '" + Timestamp.valueOf(newEnd.toLocalDateTime()) + "' > end) "
                + "AND (createdBy = '"+ consultant +"' AND appointmentID != 0)");

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
        
        // Lambda Expression: adds an eventListener to TableView
        customersTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null) {
                appWithTextField.setText(newSelection.getCustomerName());
            }
        });
        
        //set datepicker
        appDatePicker.setValue(LocalDate.now());
        
        /**
         * set time combo boxes based on most businesses hours (8am-5pm)
         */
        LocalTime time = LocalTime.of(8, 0);
	do {
		startTimes.add(time.format(timeDTF));
		endTimes.add(time.format(timeDTF));
		time = time.plusMinutes(15);
	} while(!time.equals(LocalTime.of(17, 15)));
            startTimes.remove(startTimes.size() - 1);
            endTimes.remove(0);
        
        appStartComboBox.setItems(startTimes);
        appEndComboBox.setItems(endTimes);
        appStartComboBox.getSelectionModel().select(LocalTime.of(8, 0).format(timeDTF));
	appEndComboBox.getSelectionModel().select(LocalTime.of(8, 15).format(timeDTF));

    }    
    
}
