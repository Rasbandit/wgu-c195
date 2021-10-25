package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class controls the 'MAIN MENU' screen of my application.
 *
 * @author Todd Rasband*/
public class MainMenuController implements Initializable
{
    Stage stage;
    Parent scene;

    /**
     * This method sends the user to the 'VIEW APPOINTMENTS' screen.
     *
     * @param event clicking on the view appointments button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionViewAppointments(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/ViewAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method sends the user to the 'VIEW CUSTOMERS' screen.
     *
     * @param event clicking on the view customers button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionViewCustomers(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/ViewCustomers.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method sends the user to the 'VIEW REPORTS' screen.
     *
     * @param event clicking on the view reports button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionViewReports(ActionEvent event) throws IOException
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../view/ViewReports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method logs the user out, and directs back to the 'LOGIN' screen.
     *
     * @param event clicking on the logout button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionLogout(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ARE YOU SURE?");
        alert.setContentText("This will log you out, are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This method initializes the 'MAIN MENU' screen.
     *
     * @param url The location.
     * @param resourceBundle The resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
