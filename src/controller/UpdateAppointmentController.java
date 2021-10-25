package controller;

import DAO.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;


/** This class controls the 'UPDATE APPOINTMENT' screen of my application.
 *
 * @author Ryan Zeigler*/
public class UpdateAppointmentController implements Initializable
{

    Stage stage;
    Parent scene;

    /** Table for customer information. */
    @FXML
    private TableView<Customer> customerTable;

    /** Table column for customer id. */
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;

    /** Table column for customer name. */
    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    /** Text field for appointment id.*/
    @FXML
    private TextField appointmentIdText;

    /** Text field for appointment title.*/
    @FXML
    private TextField titleText;

    /** Text field for appointment description.*/
    @FXML
    private TextField descriptionText;

    /** Text field for appointment location.*/
    @FXML
    private TextField locationText;

    /** Combobox for selecting a contact.*/
    @FXML
    private ComboBox<Contact> contactComboBox;

    /** Text field for appointment type.*/
    @FXML
    private TextField typeText;

    /** Combobox for selecting an appointment start time.*/
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;

    /** Combobox for selecting an appointment end time.*/
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;

    /** Date picker for selecting an appointment date.*/
    @FXML
    private DatePicker datePicker;

    /** Text field for customer id.*/
    @FXML
    private TextField customerId;

    /** Combobox for selecting a user.*/
    @FXML
    private ComboBox<User> userIdComboBox;


    /**
     * This method fills the customer id text field from a selected customer in the table.
     *
     * @param event clicking on a customer entry in the table.
     */
    @FXML
    void onMouseClickFillCustomerTextField(MouseEvent event)
    {
        customerId.setText(String.valueOf(customerTable.getSelectionModel().getSelectedItem().getCustomerId()));

    }


    /** This method saves the appointment to the database, and directs back to the 'APPOINTMENTS' screen. It gives errors if invalid data is entered.
     *
     * @param event clicking the save button.
     * @throws IOException The exception that will be thrown in an error.
     */

    @FXML
    void onActionUpdateAppointment(ActionEvent event) throws IOException
    {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ARE YOU SURE?");
        alert.setContentText("The new appointment will be added to the calendar, are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();


        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            String customer_Id = customerId.getText();
            String title = titleText.getText();
            String description = descriptionText.getText();
            String location = locationText.getText();
            Contact contact = contactComboBox.getValue();
            String type = typeText.getText();
            LocalDate date = datePicker.getValue();

            LocalTime st = startTimeComboBox.getValue();
            LocalTime et = endTimeComboBox.getValue();
            User userId = userIdComboBox.getValue();
            int appointment_Id = Integer.parseInt(appointmentIdText.getText());


            if (contact!=null && !type.isEmpty() && date!=null && st!=null && et!=null && !customer_Id.isEmpty() && userId!=null)
            {

                Timestamp start = Timestamp.valueOf(LocalDateTime.of( date, startTimeComboBox.getValue()));
                Timestamp end = Timestamp.valueOf(LocalDateTime.of( date, endTimeComboBox.getValue()));
                int cId = Integer.parseInt(customer_Id);


                if (LocalDateTime.of(date, endTimeComboBox.getValue()).isAfter(LocalDateTime.of(date, startTimeComboBox.getValue())))
                {
                    Appointment newAppointment = new Appointment(appointment_Id, title, description, location, contact.getContactId(), contact.getContactName(), type, start, end, cId, userId.getUserId());


                    if (DBAppointments.checkForOverlappingAppointment(newAppointment))
                    {

                        Alert alert3 = new Alert(Alert.AlertType.ERROR);
                        alert3.setHeaderText("APPOINTMENT OVERLAP");
                        alert3.setContentText("Appointment overlaps with an existing appointment for the selected customer.");
                        alert3.showAndWait();
                    }
                    else {

                        DBAppointments.updateAppointment(title, description, location, type, start, end, cId, userId.getUserId(), contact.getContactId(), appointment_Id);


                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("../view/ViewAppointments.fxml"));
                        stage.setScene(new Scene(scene));
                        stage.show();
                    }
                }
                else
                {
                    Alert alert3 = new Alert(Alert.AlertType.ERROR);
                    alert3.setHeaderText("TIME ERROR");
                    alert3.setContentText("Appointment end time must be after appointment start time.");
                    alert3.showAndWait();
                }

            }
            else
            {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setHeaderText("INVALID ENTRIES");
                alert3.setContentText("Please enter a valid value for each required field!");
                alert3.showAndWait();
            }
        }
    }

    /** This method cancels updating the appointment, and directs back to the 'APPOINTMENTS' screen.
     *
     * @param event clicking the cancel button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionCancelUpdate(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ARE YOU SURE?");
        alert.setContentText("Are you sure you want to cancel updating the appointment?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/ViewAppointments.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        }

    }



    Appointment appointment;

    /**
     * This method sends the appointment that is selected in the appointment table in the 'VIEW APPOINTMENTS' screen to the 'UPDATE APPOINTMENT' screen.
     *
     * @param appointment the appointment to send
     */
    public void sendAppointment(Appointment appointment)
    {

        this.appointment = appointment;

        appointmentIdText.setText(Integer.toString(appointment.getAppointmentId()));
        titleText.setText(appointment.getTitle());
        descriptionText.setText(appointment.getDescription());
        locationText.setText(appointment.getLocation());

        for (Contact c : contactComboBox.getItems()) {
            if (appointment.contactId == c.getContactId()) {
                contactComboBox.setValue(c);
                break;
            }
        }

        typeText.setText(appointment.getType());

        LocalTime setStart = appointment.getStart().toLocalDateTime().toLocalTime();
        startTimeComboBox.setValue(setStart);
        LocalTime setEnd = appointment.getEnd().toLocalDateTime().toLocalTime();
        endTimeComboBox.setValue(setEnd);

        LocalDate appointmentDate = appointment.getStart().toLocalDateTime().toLocalDate();
        datePicker.setValue(appointmentDate);

        customerId.setText(String.valueOf(appointment.getCustomerId()));

        for (User u : userIdComboBox.getItems())
        {
            if (appointment.userId == u.getUserId())
            {
                userIdComboBox.setValue(u);
                break;
            }
        }
    }


    /**
     * This method initializes the 'UPDATE APPOINTMENT' screen. It populates the customer table, populates the contact and user comboboxes, and handles the conversion of times between local time and EST.
     *
     * @param url the location.
     * @param resourceBundle the resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        customerTable.setItems(DBCustomers.getAllCustomers());


        contactComboBox.setItems(DBContacts.getAllContacts());
        userIdComboBox.setItems(DBUsers.getAllUsers());




        LocalTime appointmentStartTimeMinEST = LocalTime.of(8, 0);
        LocalDateTime startMinEST = LocalDateTime.of(LocalDate.now(), appointmentStartTimeMinEST);
        ZonedDateTime startMinZDT = startMinEST.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime startMinLocal = startMinZDT.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime appointmentStartTimeMin = startMinLocal.toLocalTime();

        LocalTime appointmentStartTimeMaxEST = LocalTime.of(21, 45);
        LocalDateTime startMaxEST = LocalDateTime.of(LocalDate.now(), appointmentStartTimeMaxEST);
        ZonedDateTime startMaxZDT = startMaxEST.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime startMaxLocal = startMaxZDT.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime appointmentStartTimeMax = startMaxLocal.toLocalTime();

        while (appointmentStartTimeMin.isBefore(appointmentStartTimeMax.plusSeconds(1)))
        {
            startTimeComboBox.getItems().add(appointmentStartTimeMin);
            appointmentStartTimeMin = appointmentStartTimeMin.plusMinutes(15);
        }


        LocalTime appointmentEndTimeMinEST = LocalTime.of(8, 15);
        LocalDateTime endMinEST = LocalDateTime.of(LocalDate.now(), appointmentEndTimeMinEST);
        ZonedDateTime endMinZDT = endMinEST.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime endMinLocal = endMinZDT.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime appointmentEndTimeMin = endMinLocal.toLocalTime();

        LocalTime appointmentEndTimeMaxEST = LocalTime.of(22, 0);
        LocalDateTime endMaxEST = LocalDateTime.of(LocalDate.now(), appointmentEndTimeMaxEST);
        ZonedDateTime endMaxZDT = endMaxEST.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime endMaxLocal = endMaxZDT.withZoneSameInstant(ZoneId.systemDefault());
        LocalTime appointmentEndTimeMax = endMaxLocal.toLocalTime();

        while (appointmentEndTimeMin.isBefore(appointmentEndTimeMax.plusSeconds(1)))
        {
            endTimeComboBox.getItems().add(appointmentEndTimeMin);
            appointmentEndTimeMin = appointmentEndTimeMin.plusMinutes(15);
        }

    }

}
