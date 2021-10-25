package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.SchedulingCollections;
import utilities.DBConnection;

/**
 *
 * @author Shawn
 */
public class LoginController implements Initializable {
    
    @FXML private Label loginLabel;
    @FXML private Label usernameLabel;
    @FXML private TextField usernameTextField;
    @FXML private Label PasswordLabel;
    @FXML private PasswordField userPasswordField;
    @FXML private Button loginButton;
    
    //These items are for localizing error
    private String errorTitle;
    private String errorHeader;
    private String errorMessage;
    
    
    /**
     * Validates user's log in credentials and 
     * changes scene to main application screen.
     */
    public void loginButtonPushed(ActionEvent event) throws IOException{
        String username = usernameTextField.getText();
        String password = userPasswordField.getText();
        //validate user
        boolean checkedUser = DBConnection.validateLogin(username, password);
        if(checkedUser){
            SchedulingCollections.populateCustomersList();
            SchedulingCollections.populateWeeklyAppointmentsList();
            SchedulingCollections.populateMonthlyAppointmentsList();
            SchedulingCollections.populateAllAppointmentsList();
            
            //change scene to main
            Parent mainViewParent = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            //create new scene object
            Scene mainScene = new Scene(mainViewParent);
            //this line gets the Stage information and casts it as a stage
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainScene);
            window.show();
            
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(errorTitle);
            alert.setHeaderText(errorHeader);
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Get Locale from user's system
        //Test for loacalization in german language
//        Locale locale = new Locale("de", "CH");
//        Locale.setDefault(locale);

        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("utilities/Login", locale); 
        loginLabel.setText(rb.getString("Login"));
        usernameLabel.setText(rb.getString("username"));
        PasswordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("Login"));
        errorTitle = rb.getString("errorTitle");
        errorHeader = rb.getString("errorHeader");
        errorMessage = rb.getString("errorMessage");
    }    
    
}
