package controllers;

import exceptions.InvalidCustomerException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Address;
import models.City;
import models.Country;
import models.Customer;
import models.SchedulingCollections;
import utilities.DBConnection;
import utilities.QueryManager;

/**
 * FXML Controller class
 *
 * @author Shawn
 */
public class AddCustomerController implements Initializable {
    
    @FXML TextField custNameTextField;
    @FXML TextField custCountryTextField;
    @FXML TextField custAddressTextField;
    @FXML TextField custCityTextField;
    @FXML TextField custPostalTextField;
    @FXML TextField custPhoneNumberTextField;
    
    private Address customerAddress = new Address();
    private City customerCity = new City();
    private Country customerCountry = new Country();
    
    /**
     * This Method saves added customer to the DB
     */
    public void saveCustomerButtonPushed(ActionEvent event) throws IOException{
        
        customerCountry.setCountry(custCountryTextField.getText());
        customerAddress.setAddress(custAddressTextField.getText());
        customerCity.setCity(custCityTextField.getText());
        customerAddress.setPostalCode(custPostalTextField.getText());
        customerAddress.setPhone(custPhoneNumberTextField.getText());
        
        String customerName = custNameTextField.getText();
        String country = custCountryTextField.getText();
        String address = custAddressTextField.getText();
        String city = custCityTextField.getText();
        String postalCode = custPostalTextField.getText();
        String phoneNumber = custPhoneNumberTextField.getText();
        String loggedInUser = DBConnection.loggedInUser.getUsername();
        
        
        try{
            if(validateInput(customerName, country, address, city, postalCode, phoneNumber)){
                
                //insert into country
                QueryManager.makeQuery("INSERT INTO country (country, createDate, createdBy, lastUpdateBy)\n" +
                    "SELECT '" + country + "', NOW(), '" + loggedInUser + "', '" + loggedInUser + "'\n" +
                    "FROM country\n" +
                    "WHERE NOT EXISTS (SELECT * FROM country WHERE country='" + country + "') LIMIT 1");
                int countryId = customerCountry.getCountryId(country);
                
                //insert into city
                QueryManager.makeQuery("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdateBy)\n" +
                    "SELECT '" + city + "', " + countryId + ", NOW(), '" + loggedInUser + "', '" + loggedInUser + "'\n" +
                    "FROM city\n" +
                    "WHERE NOT EXISTS (SELECT * FROM city WHERE city = '" + city + "') LIMIT 1");
               int cityId = customerCity.getCityId(city);

                //insert into address
                QueryManager.makeQuery("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdateBy)\n" +
                    "SELECT '" + address + "', '', " + cityId + ", '" + postalCode + "', '" + phoneNumber + "', NOW(), '" + loggedInUser + "', '" + loggedInUser + "'\n" +
                    "FROM address\n" +
                    "WHERE NOT EXISTS (SELECT * FROM address WHERE address = '" + address + "') LIMIT 1");
                int addressId = customerAddress.getAddressId(address);

                //insert into customer
                QueryManager.makeQuery("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdateBy)\n" +
                    "VALUES ('" + customerName + "', " + addressId + ", 1, NOW(), '" + loggedInUser + "', '" + loggedInUser + "')");
                
                //add new customer and update customer list
                Customer newCustomer = new Customer();
                newCustomer.setCustomerName(customerName);
                newCustomer.setCustomerAddress(address);
                newCustomer.setCustomerPhone(phoneNumber);
                newCustomer.setCustomerCountry(country);
                newCustomer.setCustomerCity(city);
                newCustomer.setCustomerPostalCode(postalCode);
                SchedulingCollections.addCustomer(newCustomer);
                
                //change scene to main
                Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
                Scene mainScene = new Scene(mainViewParent);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(mainScene);
                window.show();
            }
                
        } catch(InvalidCustomerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Missing information!");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } 
    }
    
    /**
     * This cancels and returns to main screen
     */
    public void cancelButtionPushed(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancelation");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Information not saved will be lost!");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK){
            //change scene to main
            Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            //create new scene object
            Scene mainScene = new Scene(mainViewParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
        }
    }
    
    public boolean validateInput(String name, String country, String address, String city, String postal, String phone) throws InvalidCustomerException{
        
        if(name.equals("")){
            throw new InvalidCustomerException("Please enter customer's name");
        }
        if(country.equals("")){
            throw new InvalidCustomerException("Please enter a country");
        }
        if(address.equals("")){
            throw new InvalidCustomerException("Please enter a customer's address");
        }
        if(city.equals("")){
            throw new InvalidCustomerException("Please enter a customer's city");
        }
        if(postal.equals("")){
            throw new InvalidCustomerException("Please enter a customer's postal code");
        }
        if(phone.equals("")){
            throw new InvalidCustomerException("Please enter a customer's phone number");
        }
        return true;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
