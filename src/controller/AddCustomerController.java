package controller;

import DAO.DBCountries;
import DAO.DBCustomers;
import DAO.DBDivisions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class controls the 'ADD CUSTOMER' screen of my application.
 *
 * @author Todd Rasband*/
public class AddCustomerController implements Initializable
{
    Stage stage;
    Parent scene;
    /** Text field for customer id.*/
    @FXML
    private TextField customerIdText;
    /** Text field for customer name.*/
    @FXML
    private TextField nameText;
    /** Text field for customer address.*/
    @FXML
    private TextField addressText;
    /** Combobox for selecting a country.*/
    @FXML
    private ComboBox<Country> countryComboBox;
    /** Combobox for selecting a division.*/
    @FXML
    private ComboBox<Division> divisionComboBox;
    /** Text field for customer postal code.*/
    @FXML
    private TextField postalCodeText;
    /** Text field for customer phone number.*/
    @FXML
    private TextField phoneText;
    /** Label of divisions.*/
    @FXML
    private Label divisionSwitchLabel;


    /** This method saves the customer to the database, and directs back to the 'CUSTOMERS' screen. It gives errors if invalid data is entered.
     *
     * @param event clicking the save button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ARE YOU SURE?");
        alert.setContentText("The new customer will be added to the database, are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {

            String customerName = nameText.getText();
            String address = addressText.getText();
            String postalCode = postalCodeText.getText();
            String phone = phoneText.getText();
            Division division = divisionComboBox.getValue();

            if (!customerName.isEmpty() && !address.isEmpty() && !postalCode.isEmpty() && !phone.isEmpty() && !(division == null))
            {
                DBCustomers.addCustomer(customerName, address, postalCode, phone, division.getDivisionId());


                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../view/ViewCustomers.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else
                {
                    Alert alert3 = new Alert(Alert.AlertType.ERROR);
                    alert3.setHeaderText("BLANK FIELDS");
                    alert3.setContentText("Please enter a valid value for each field! All fields are required.");
                    alert3.showAndWait();
                }
        }
    }

    /** This method cancels adding the customer, and directs back to the 'CUSTOMERS' screen.
     *
     * @param event clicking the cancel button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionCancelAdd(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ARE YOU SURE?");
        alert.setContentText("This will clear all fields and cancel adding a customer, are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/ViewCustomers.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /**
     * This method determines the content of the divisions label and populates the divisions combobox, based on the country combobox selection.
     *
     * @param event making a selection in the country combobox.
     */
    @FXML
    void onActionDivision(ActionEvent event)
    {

        Country country = countryComboBox.getSelectionModel().getSelectedItem();

        if (country.getCountryId() == 1)
        {
            divisionSwitchLabel.setText("State:");
        }
        else if (country.getCountryId() == 2)
        {
            divisionSwitchLabel.setText("Sub-division:");
        }
        else if (country.getCountryId() == 3)
        {
            divisionSwitchLabel.setText("Province:");
        }


        if (country.getCountryId() == 1)
        {
            divisionComboBox.setItems(DBDivisions.getUSDivisions());
        }
        else if (country.getCountryId() == 2)
        {
            divisionComboBox.setItems(DBDivisions.getUKDivisions());
        }
        else if (country.getCountryId() == 3)
        {
            divisionComboBox.setItems(DBDivisions.getCADivisions());
        }
        else
        {
            divisionComboBox.isDisabled();
        }
    }

/**
 * This method initializes the 'ADD CUSTOMER' screen. It populates the country combobox, and clears the contents of the division combobox.
 *
 * @param url the location.
 * @param resourceBundle the resources.
 */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        countryComboBox.setItems(DBCountries.getAllCountries());
        divisionComboBox.getSelectionModel().clearSelection();
    }
}
