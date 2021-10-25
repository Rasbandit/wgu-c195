package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static controllers.MainController.modifyCustomer;
import static controllers.MainController.modifyCustomerIndex;
import static models.SchedulingCollections.updateCustomer;
import models.Customer;
import utilities.DBConnection;
import utilities.QueryManager;

/**
 * FXML Controller class
 *
 * @author Shawn
 */
public class ModifyCustomerController implements Initializable {

    @FXML TextField custNameTextField;
    @FXML TextField custCountryTextField;
    @FXML TextField custAddressTextField;
    @FXML TextField custCityTextField;
    @FXML TextField custPostalTextField;
    @FXML TextField custPhoneNumberTextField;
    
    /**
     * This saves updated customer to DB, updates allCustomersList
     * and returns to main scene
     */
    public void saveButtonPushed(ActionEvent event) throws IOException{
        
        String customerName = custNameTextField.getText();
        String country = custCountryTextField.getText();
        String address = custAddressTextField.getText();
        String city = custCityTextField.getText();
        String postalCode = custPostalTextField.getText();
        String phoneNumber = custPhoneNumberTextField.getText();
        String loggedInUser = DBConnection.loggedInUser.getUsername();
        int countryId = modifyCustomer.getCustomerCountryID();
        int customerId = modifyCustomer.getCustomerID();
        int addressId = modifyCustomer.getCustomerAddressID();
        int cityId = modifyCustomer.getCustomerCityID();
        
        //update country
        QueryManager.makeQuery("UPDATE country SET country = '" + country + "', lastUpdate = NOW(),"+
                " lastUpdateBy='" + loggedInUser + "' WHERE Country_Id=" + countryId + "");
        System.out.println("update country to: " + country);
        //update city
        QueryManager.makeQuery("UPDATE city SET city='" + city + "', lastUpdate = NOW(), "+
                "lastUpdateBy='" + loggedInUser + "' WHERE City_Id=" + cityId + "");
        System.out.println("update city to: " + city);
        //update address
        QueryManager.makeQuery("UPDATE address SET address = '" + address + "', "+
                "postal_Code = '" + postalCode + "', phone = '" + phoneNumber + "' WHERE Address_ID = " + addressId + " ");
        System.out.println("update address to: " + address);
        //update customer
        QueryManager.makeQuery("UPDATE customer SET customerName = '" + customerName + "', "+
                "lastUpdate = NOW(), lastUpdateBy = '" + loggedInUser + "'\n" +
                "WHERE Customer_ID = " + customerId + "");
        System.out.println("update customer name to: " + customerName);
        
        //create new customer and set values
        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerID(customerId);
        updatedCustomer.setCustomerName(customerName);
        updatedCustomer.setCustomerAddress(address);
        updatedCustomer.setCustomerPhone(phoneNumber);
        updatedCustomer.setCustomerCountry(country);
        updatedCustomer.setCustomerCountryID(countryId);
        updatedCustomer.setCustomerAddressID(addressId);
        updatedCustomer.setCustomerCityID(cityId);
        updatedCustomer.setCustomerCity(city);
        updatedCustomer.setCustomerPostalCode(postalCode);
        //update customerList
        updateCustomer(modifyCustomerIndex, updatedCustomer);
        
        //change scene to main
        Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
        Scene mainScene = new Scene(mainViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
        
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
        
        // set Texfields to items from selected customer
        custNameTextField.setText(modifyCustomer.getCustomerName());
        custCountryTextField.setText(modifyCustomer.getCustomerCountry());
        custAddressTextField.setText(modifyCustomer.getCustomerAddress());
        custCityTextField.setText(modifyCustomer.getCustomerCity());
        custPostalTextField.setText(modifyCustomer.getCustomerPostalCode());
        custPhoneNumberTextField.setText(modifyCustomer.getCustomerPhoneNum());
    }    
    
}
