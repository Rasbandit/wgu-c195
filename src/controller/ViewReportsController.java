package controller;

import DAO.DBAppointments;
import DAO.DBContacts;
import DAO.DBCustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


/** This class controls the 'REPORTS' screen of my application.
 *
 * @author Todd Rasband*/
public class ViewReportsController implements Initializable
{
    Stage stage;
    Parent scene;

    /** Combobox for selecting a month.*/
    @FXML
    private ComboBox<String> monthComboBox;
    /** Combobox for selecting a type.*/
    @FXML
    private ComboBox<String> typeComboBox;
    /** Label for results of report 1. */
    @FXML
    private Label report1ResultsLabel;
    /** Table for appointment information. */
    @FXML
    private TableView<Appointment> report2;
    /** Table column for appointment id. */
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumnReport2;
    /** Table column for appointment title. */
    @FXML
    private TableColumn<Appointment, String> appointmentTitleColumnReport2;
    /** Table column for appointment type. */
    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumnReport2;
    /** Table column for appointment description. */
    @FXML
    private TableColumn<Appointment, String> appointmentDescriptionColumnReport2;
    /** Table column for appointment start time and date. */
    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumnReport2;
    /** Table column for appointment end time and date. */
    @FXML
    private TableColumn<Appointment, LocalDateTime> endColumnReport2;
    /** Table column for customer id. */
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumnReport2;
    /** Combobox for selecting a contact.*/
    @FXML
    private ComboBox<Contact> contactComboBox;
    /** Label for results of report 3. */
    @FXML
    private Label report3ResultsLabel;

    /**
     * This method the runs the first report, displaying the number of appointments by month and type.
     *
     * @param event clicking on the button for running the report.
     */
    @FXML
    void onActionRunReport1(ActionEvent event)
    {
        String month = monthComboBox.getValue();
        if (month == null)
        {
            return;
        }

        String type = typeComboBox.getValue();
        if (type == null)
        {
            return;
        }

        int total = DBAppointments.getMonthAndTypeCount(month, type);

        report1ResultsLabel.setText(String.valueOf(total));
    }


    /**
     * This method the runs the second report, displaying the appointment information for a specific contact.
     *
     * Discussion of lambda - I implemented a lambda expression to facilitate the filtering of the appointments list by contact id to find all appointments that have the specific contact listed.
     *
     * @param event clicking on the button for running the report.
     */
    @FXML
    void onActionRunReport2(ActionEvent event)
    {
        Contact contact = contactComboBox.getValue();

        if (contact == null)
        {
            return;
        }
        ObservableList<Appointment> aList = DBAppointments.getAllAppointments();
        ObservableList<Appointment> cList = aList.filtered(ap ->
        {

            if (ap.getContactId() == contact.getContactId()) {
                return true;
            }
            return false;

        });

        report2.setItems(cList);
    }


    /**
     * This method the runs the third report, displaying the total number of appointments.
     *
     * @param event clicking on the button for running the report.
     */
    @FXML
    void onActionRunReport3(ActionEvent event)
        {
            report3ResultsLabel.setText(String.valueOf(DBCustomers.getAllCustomers().size()));
        }





    /**
     * This method sends the user to the 'MAIN MENU' screen.
     *
     * @param event clicking on the back to main menu button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionGoToMainMenu(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }



    /**
     * This method initializes the 'REPORTS' screen. It populates the appointment table and also populates the contact, type, and month comboboxes.
     *
     * @param url the location.
     * @param resourceBundle the resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        appointmentIdColumnReport2.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleColumnReport2.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDescriptionColumnReport2.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentTypeColumnReport2.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumnReport2.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
        endColumnReport2.setCellValueFactory(new PropertyValueFactory<>("endTimeLocal"));
        customerIdColumnReport2.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        monthComboBox.setItems(FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"));
        typeComboBox.setItems(DBAppointments.getAllTypes());

        contactComboBox.setItems(DBContacts.getAllContacts());
    }
}
