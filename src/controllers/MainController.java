package controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import models.Appointment;
import models.Customer;
import models.SchedulingCollections;

/**
 * FXML Controller class
 *
 * @author Shawn
 */
public class MainController implements Initializable {

    //These items are for the customers TableView
    @FXML private TableView<Customer> customersTableView;
    @FXML private TableColumn<Customer, String> custNameColumn;
    @FXML private TableColumn<Customer, String> custAddressColumn;
    @FXML private TableColumn<Customer, String> custphoneNumberColumn;
    @FXML private TableColumn<Customer, String> custCountryColumn;
    //These items are for the appointment radio buttons
    @FXML private RadioButton weeklyRadioButton;
    @FXML private RadioButton monthlyRadioButton;
    @FXML private RadioButton allRadioButton;
    private ToggleGroup appmntToggleGroup;
    //These items are for the appointments TableView
    @FXML private TableView<Appointment> appmntTableView;
    @FXML private TableColumn<Appointment, String> appmntCustomerColumn;
    @FXML private TableColumn<Appointment, String> appmntConsultantColumn;
    @FXML private TableColumn<Appointment, String> appmntStartColumn;
    @FXML private TableColumn<Appointment, String> appmntEndColumn;
    @FXML private TableColumn<Appointment, String> appmntTitleColumn;
    @FXML private TableColumn<Appointment, String> appmntTypeColumn;
    //These items are for modifying customers and appointments
    public static Customer modifyCustomer;
    public static int modifyCustomerIndex;
    public static Appointment selectedAppointment;
    public static int modifyAppointmentIndex;
    //date formatter formats date into more readable format
    private final DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("M/d/yy h:mm a");
    
    /**
     * Changes scene to add customers view when add button is pushed
     */
    public void addCustomerButtonPushed(ActionEvent event) throws IOException{
        
        Parent addCustomerParent = FXMLLoader.load(getClass().getResource("/views/AddCustomerView.fxml"));
        //create new scene object
        Scene mainScene = new Scene(addCustomerParent);

        //this line gets the Stage information and casts it as a stage
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
    
    /**
     * Changes scene to modify customers view when modify customer button is pushed
     */
    public void modifyCustomerButtonPushed(ActionEvent event) throws IOException{
        
        if(customersTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected!");
            alert.setContentText("Please ensure a customer is selected to be modified.");
            alert.showAndWait();
        } else {
            modifyCustomer = customersTableView.getSelectionModel().getSelectedItem();
            modifyCustomerIndex = SchedulingCollections.getAllCustomers().indexOf(modifyCustomer);
            Parent modifyCustomerParent = FXMLLoader.load(getClass().getResource("/views/ModifyCustomerView.fxml"));
            //create new scene object
            Scene mainScene = new Scene(modifyCustomerParent);
            //this line gets the Stage information and casts it as a stage
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        }
        
    }
    
    /**
     * deletes the customer that is selected in the TableView
     */
    public void deleteCustomerButtonPushed(ActionEvent event){
        if (customersTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No customer selected!");
            alert.setContentText("Please ensure a customer is selected to be deleted.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete this customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                //delete customer
                Customer c = customersTableView.getSelectionModel().getSelectedItem();
                SchedulingCollections.deleteCustomer(c);
                customersTableView.setItems(SchedulingCollections.getAllCustomers());
            }
        }
        
    }
    
    /**
     * Changes scene to add appointment view when add appointment button is pushed
     */
    public void addAppButtonPushed(ActionEvent event) throws IOException{
        Parent addAppParent = FXMLLoader.load(getClass().getResource("/views/AddAppointmentView.fxml"));
        Scene mainScene = new Scene(addAppParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }
    
    /**
     * Changes scene to modify appointment view when modify appointment button is pushed
     */
    public void modAppButtonPushed(ActionEvent event) throws IOException{
        if(appmntTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected!");
            alert.setContentText("Please ensure a appointment is selected to be modified.");
            alert.showAndWait();
        } else {
            selectedAppointment = appmntTableView.getSelectionModel().getSelectedItem();
            modifyAppointmentIndex = SchedulingCollections.getAllAppointments().indexOf(selectedAppointment);
            
            //change scene
            Parent modAppParent = FXMLLoader.load(getClass().getResource("/views/ModAppointmentView.fxml"));
            Scene modAppScene = new Scene(modAppParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(modAppScene);
            window.show();
        }
    }
    
    /**
     * deletes the appointment that is selected in the TableView
     */
    public void deleteAppmntButtonPushed(ActionEvent event){
        
        if (appmntTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No appointment selected!");
            alert.setContentText("Please ensure an appointment is selected to be deleted.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                //delete appointment
                Appointment a = appmntTableView.getSelectionModel().getSelectedItem();
                SchedulingCollections.deleteAppointment(a);
                if(appmntToggleGroup.getSelectedToggle().equals(this.allRadioButton)){
                    appmntTableView.setItems(SchedulingCollections.getAllAppointments());
                }
                if(appmntToggleGroup.getSelectedToggle().equals(this.monthlyRadioButton)){
                    appmntTableView.setItems(SchedulingCollections.getMonthlyAppointments());
                }
                if(appmntToggleGroup.getSelectedToggle().equals(this.weeklyRadioButton)){
                    appmntTableView.setItems(SchedulingCollections.getWeeklyAppointments());
                }
                
            }
        }
        
    }
    
    /**
     * This method will open the reports page
     */
    public void reportsButtonPushed(ActionEvent event) throws IOException{
        //change scene
        Parent modAppParent = FXMLLoader.load(getClass().getResource("/views/ReportsView.fxml"));
        Scene reportScene = new Scene(modAppParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(reportScene);
        window.show();
    }
    
    public void logsButtonPushed(ActionEvent event){
        File file = new File("log.txt");
        if(file.exists()) {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    System.out.println("Error Opening Log File: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * This will close the program when the Exit button is pushed
     */
    public void exitButtonPushed(ActionEvent event) throws IOException{
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }
    
    /**
     * This method will update appointment TableView whenever a different
     * radio button is selected.
     */
    public void radioButtonChanged(){
        if(appmntToggleGroup.getSelectedToggle().equals(this.allRadioButton)){
            //change TableView data to all appointments
            populateAllAppointmentTable();
        }
        if(appmntToggleGroup.getSelectedToggle().equals(this.monthlyRadioButton)){
            //change TableView data to monthly appointments
            populateMonthlyAppointmentTable();
        }
        if(appmntToggleGroup.getSelectedToggle().equals(this.weeklyRadioButton)){
            //change TableView data to weekly appointments
            populateWeeklyAppointmentTable();
        }
    }
    //this table method uses Lambda expressions to conveniently populate table columns
    public void populateCustomerTable(){
        custNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        custAddressColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerAddress()));
        custphoneNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerPhoneNum()));
        custCountryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerCountry()));
        customersTableView.setItems(SchedulingCollections.getAllCustomers());
    }
    //this table method uses Lambda expressions to conveniently populate table columns
    public void populateAllAppointmentTable(){
        appmntTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentTitle()));
        appmntTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentType()));
        appmntCustomerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentCustomer()));
        appmntConsultantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentConsultant()));
        
        appmntStartColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentStart().format(formatDateTime)));
        appmntEndColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getAppointmentEnd().format(formatDateTime)));
        appmntTableView.setItems(SchedulingCollections.getAllAppointments());
    }
    //this table method uses Lambda expressions to conveniently populate table columns
    public void populateMonthlyAppointmentTable(){
        appmntTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentTitle()));
        appmntTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentType()));
        appmntCustomerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentCustomer()));
        appmntConsultantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentConsultant()));
        
        appmntStartColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentStart().format(formatDateTime)));
        appmntEndColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getAppointmentEnd().format(formatDateTime)));
        appmntTableView.setItems(SchedulingCollections.getMonthlyAppointments());
    }
    //this table method uses Lambda expressions to conveniently populate table columns
    public void populateWeeklyAppointmentTable(){
        appmntTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentTitle()));
        appmntTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentType()));
        appmntCustomerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentCustomer()));
        appmntConsultantColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentConsultant()));
        
        appmntStartColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentStart().format(formatDateTime)));
        appmntEndColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getAppointmentEnd().format(formatDateTime)));
        appmntTableView.setItems(SchedulingCollections.getWeeklyAppointments());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appmntToggleGroup = new ToggleGroup();
        this.weeklyRadioButton.setToggleGroup(appmntToggleGroup);
        this.monthlyRadioButton.setToggleGroup(appmntToggleGroup);
        this.allRadioButton.setToggleGroup(appmntToggleGroup);
        
        //this sets default radio button
        allRadioButton.setSelected(true);
        //populate tables
        populateAllAppointmentTable();
        populateCustomerTable();
        
        // check for upcoming appointments within 15 min
        Appointment upcomingAppointment = SchedulingCollections.checkUpcomingAppointment();
        if(upcomingAppointment != null){
            String message = String.format("You have a %s appointment with %s scheduled at %s",
            upcomingAppointment.getAppointmentType(),
            upcomingAppointment.getAppointmentCustomer(),
            upcomingAppointment.getAppointmentStart().format(formatDateTime));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Reminder");
            alert.setHeaderText("Appointment Within 15 Minutes");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }    
    
}
