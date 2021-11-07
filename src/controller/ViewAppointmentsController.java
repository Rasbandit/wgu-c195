package controller;

import DAO.DBAppointments;
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


import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;


/** This class controls the 'VIEW APPOINTMENTS' screen of my application.
 *
 * @author Todd Rasband*/
public class ViewAppointmentsController implements Initializable
{
    Stage stage;
    Parent scene;


    /** Table for appointment information. */
    @FXML
    private TableView<Appointment> appointmentTable;
    /** Table column for appointment id. */
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    /** Table column for appointment title. */
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    /** Table column for appointment description. */
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    /** Table column for appointment location. */
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    /** Table column for appointment contact. */
    @FXML
    private TableColumn<Appointment, String> contactColumn;
    /** Table column for appointment type. */
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    /** Table column for appointment start time and date. */
    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumn;
    /** Table column for appointment end time and date. */
    @FXML
    private TableColumn<Appointment, LocalDateTime> endColumn;
    /** Table column for customer id. */
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;
    /** Radio button for viewing appointments in current week. */
    @FXML
    private RadioButton viewByWeekRadioButton;
    /** Radio button for viewing appointments in current month. */
    @FXML
    private RadioButton viewByMonthRadioButton;
    /** Radio button for viewing all appointments. */
    @FXML
    private RadioButton viewAllRadioButton;
    /** Radio button toggle group to ensure only button can be selected at a time. */
    @FXML
    private ToggleGroup viewToggleGroup;

    /**
     * This method shows all appointments in the table.
     *
     * @param event clicking on the 'ALL' radio button.
     */
    @FXML
    void onActionViewAll(ActionEvent event)
    {
        appointmentTable.setItems(DBAppointments.getAllAppointments());
    }

    /**
     * This method shows current month appointments in the table.
     *
     * @param event clicking on the 'CURRENT MONTH' radio button.
     */
    @FXML
    void onActionViewByMonth(ActionEvent event)
    {
        appointmentTable.setItems(DBAppointments.getMonthAppointments());
    }

    /**
     * This method shows current week appointments in the table.
     *
     * @param event clicking on the 'CURRENT WEEK' radio button.
     */
    @FXML
    void onActionViewByWeek(ActionEvent event)
    {
        appointmentTable.setItems(DBAppointments.getWeekAppointments());
    }

    /**
     * This method deletes an appointment from the database.
     *
     * @param event clicking on the delete appointment button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionDeleteAppointment(ActionEvent event) throws IOException
    {
        if (appointmentTable.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("PLEASE SELECT AN APPOINTMENT");
            alert.setContentText("No appointment was selected to delete.");

            Optional<ButtonType> result = alert.showAndWait();
        }
        else
        {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("ARE YOU SURE?");
            alert.setContentText("The appointment will be deleted from the database, are you sure you want to continue? This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK)
            {
                int appointmentId = appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId();

                String typeMessage = appointmentTable.getSelectionModel().getSelectedItem().getType();

                String idMessage = String.valueOf(appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId());

                DBAppointments.deleteAppointment(appointmentId);

                appointmentTable.setItems(DBAppointments.getAllAppointments());

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setHeaderText("DELETED");
                alert2.setContentText("You have successfully deleted appointment " + idMessage + ", a " + typeMessage + " appointment.");

                alert2.showAndWait();
            }
            else
            {
                Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
                alert3.setHeaderText("NOT DELETED");
                alert3.setContentText("The selected appointment was not deleted.");

                alert3.showAndWait();
            }
        }
    }

    /**
     * This method sends the user to the 'ADD APPOINTMENT' screen.
     *
     * @param event clicking on the add appointment button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionGoToAddAppointmentScreen(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * This method sends the user to the 'UPDATE APPOINTMENT' screen.
     *
     * @param event clicking on the update appointment button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionGoToUpdateAppointmentScreen(ActionEvent event) throws IOException
    {
        if (appointmentTable.getSelectionModel().isEmpty())
        {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("PLEASE SELECT AN APPOINTMENT");
            alert.setContentText("No appointment was selected to update.");

            Optional<ButtonType> result = alert.showAndWait();
        }

        else
        {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/UpdateAppointment.fxml"));
            loader.load();

            UpdateAppointmentController ADMController = loader.getController();
            ADMController.sendAppointment(appointmentTable.getSelectionModel().getSelectedItem());


            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


    /**
     * This method sends the user to the 'MAIN MENU' screen.
     *
     * @param event clicking on the back to main menu button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionGoToMainMenu(ActionEvent event) throws IOException
    {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }


    /**
     * This method initializes the 'VIEW APPOINTMENTS' screen. It populates the appointment table with all appointments and preselects the 'ALL' radio button.
     *
     * @param url the location.
     * @param resourceBundle the resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewAllRadioButton.setSelected(true);

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTimeLocal"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTimeLocal"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        appointmentTable.setItems(DBAppointments.getAllAppointments());
    }
}
