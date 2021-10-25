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
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/** This class controls the 'UPDATE CUSTOMER' screen of my application.
 *
 * @author Todd Rasband*/
public class UpdateCustomerController implements Initializable
{
    Stage stage;
    Parent scene;

    /** Text field for customer id.*/
    @FXML
    private TextField idText;
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
    void onActionUpdateCustomer(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ARE YOU SURE?");
        alert.setContentText("The customer will be updated in the database, are you sure you want to continue?");

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
                DBCustomers.updateCustomer(customerName, address, postalCode, phone, division.getDivisionId(), customer.getCustomerId());

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


    /** This method cancels updating the customer, and directs back to the 'CUSTOMERS' screen.
     *
     * @param event clicking the cancel button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionCancelUpdate(ActionEvent event) throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("ARE YOU SURE?");
        alert.setContentText("This will clear all fields and cancel updating a customer, are you sure you want to continue?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
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


    Customer customer;

    /**
     * This method sends the customer that is selected in the customer table in the 'VIEW CUSTOMERS' screen to the 'UPDATE CUSTOMERS' screen.
     *
     * @param customer the customer to send
     */
    public void sendCustomer(Customer customer) {

        this.customer = customer;

        idText.setText(Integer.toString(customer.getCustomerId()));
        nameText.setText(customer.getCustomerName());
        addressText.setText(customer.getAddress());

        for (Country c : countryComboBox.getItems())
        {
            if(customer.countryId == c.getCountryId())
            {
                countryComboBox.setValue(c);
                break;
            }
        }

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

        for(Division d : divisionComboBox.getItems())
        {
            if(customer.divisionId == d.getDivisionId())
            {
                divisionComboBox.setValue(d);
                break;
            }
        }
        postalCodeText.setText(customer.getPostalCode());
        phoneText.setText(customer.getPhone());
    }


    /**
     * This method initializes the 'ADD CUSTOMER' screen. It populates the country combobox.
     *
     * @param url the location.
     * @param resourceBundle the resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        countryComboBox.setItems(DBCountries.getAllCountries());
    }
}
