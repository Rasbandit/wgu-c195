package controller;

import DAO.DBAppointments;
import DAO.DBUsers;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


/** This class controls the 'LOGIN' screen of my application.
 *
 * @author Ryan Zeigler*/
public class LoginController implements Initializable
{
    Stage stage;
    Parent scene;

    /** Button for logging in. */
    @FXML
    private Button loginButton;

    /** Label for title. */
    @FXML
    private Label titleLabel;

    /** Button for exiting. */
    @FXML
    private Button exitButton;

    /** Label for sub-title. */
    @FXML
    private Label subtitleLabel;

    /** Label for zoneid. */
    @FXML
    private Label zoneIdLabel;

    /** Label for zoneid switch label. */
    @FXML
    private Label switchLabelZoneId;

    /** Label for user name. */
    @FXML
    private Label usernameLabel;

    /** Text field for username. */
    @FXML
    private TextField usernameText;

    /** Label for password. */
    @FXML
    private Label passwordLabel;

    /** Text field for password. */
    @FXML
    private TextField passwordText;

    private String confirmSure;
    private String confirmExit;
    private String invalidLoginData;
    private String pleaseEnterValid;



    /**
     * This method authenticates the user name and password, and tracks the user activity by recording user login attempts to a text file.
     *
     * Discussion of lambda - I implemented a lambda expression to facilitate the filtering of the appointments list by user id to check for any within 15 minutes of login, for the user that is logging in.
     *
     * @param event clicking the login button.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onActionLogin(ActionEvent event) throws IOException
    {
        String username = usernameText.getText();
        String password = passwordText.getText();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        String s = dtf.format(ldt);


        int userId = DBUsers.validateUser(username, password);

        FileWriter fWriter = new FileWriter("login_activity.txt", true);
        PrintWriter outputFile = new PrintWriter(fWriter);

        if (userId > 0)
        {


            outputFile.println(s + " " + usernameText.getText() + " successfully logged in");
            outputFile.close();

            LocalDateTime now = LocalDateTime.now();

            ObservableList<Appointment> aList = DBAppointments.getAllAppointments();
            ObservableList<Appointment> uList = aList.filtered(ap -> {

                if (ap.getUserId() == userId)
                {
                    return true;
                }
                return false;

            });

            boolean name = false;

            for (Appointment a : uList)
            {

                {
                    if (a.getStart().toLocalDateTime().isAfter(now) && a.getStart().toLocalDateTime().isBefore(now.plusMinutes(15)))
                    {
                        Alert alert3 = new Alert(Alert.AlertType.ERROR);
                        alert3.setHeaderText("UPCOMING APPOINTMENT");
                        alert3.setContentText("You have an appointment scheduled within the next 15 minutes: appointment " + a.getAppointmentId() + " at " + a.getStart().toLocalDateTime());
                        alert3.showAndWait();

                        name = true;
                    }
                }
            }

            if (!name)
            {
                Alert alert3 = new Alert(Alert.AlertType.ERROR);
                alert3.setHeaderText("NO UPCOMING APPOINTMENTS");
                alert3.setContentText("You have no appointments scheduled within the next 15 minutes.");
                alert3.showAndWait();
            }


            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else
        {
            outputFile.println(s + " " + usernameText.getText() + " unsuccessfully attempted to log in");
            outputFile.close();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(invalidLoginData);
            alert.setContentText(pleaseEnterValid);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK)
            {
                usernameText.clear();
                passwordText.clear();
            }

        }



    }


    /**
     * This method authenticates the user name and password, and tracks the user activity by recording user login attempts to a text file.
     *
     * this method is the same as the first one, but activated by pressing the enter key instead.
     *
     * Discussion of lambda - I implemented a lambda expression to facilitate the filtering of the appointments list by user id to check for any within 15 minutes of login, for the user that is logging in.
     *
     * @param event pressing the ENTER key on the keyboard.
     * @throws IOException The exception that will be thrown in an error.
     */
    @FXML
    void onPressEnterKeyLogin(KeyEvent event) throws IOException
    {
        if(event.getCode().equals(KeyCode.ENTER))
        {

            String username = usernameText.getText();
            String password = passwordText.getText();

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.now();
            String s = dtf.format(ldt);


            int userId = DBUsers.validateUser(username, password);

            FileWriter fWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fWriter);

            if (userId > 0)
            {


                outputFile.println(s + " " + usernameText.getText() + " successfully logged in");
                outputFile.close();

                LocalDateTime now = LocalDateTime.now();

                ObservableList<Appointment> aList = DBAppointments.getAllAppointments();
                ObservableList<Appointment> uList = aList.filtered(ap ->
                {

                    if (ap.getUserId() == userId)
                    {
                        return true;
                    }
                    return false;

                });

                boolean name = false;

                for (Appointment a : uList)
                {

                    {
                        if (a.getStart().toLocalDateTime().isAfter(now) && a.getStart().toLocalDateTime().isBefore(now.plusMinutes(15)))
                        {
                            Alert alert3 = new Alert(Alert.AlertType.ERROR);
                            alert3.setHeaderText("UPCOMING APPOINTMENT");
                            alert3.setContentText("You have an appointment scheduled within the next 15 minutes: appointment " + a.getAppointmentId() + " at " + a.getStart().toLocalDateTime());
                            alert3.showAndWait();

                            name = true;
                        }
                    }
                }

                if (!name)
                {
                    Alert alert3 = new Alert(Alert.AlertType.ERROR);
                    alert3.setHeaderText("NO UPCOMING APPOINTMENTS");
                    alert3.setContentText("You have no appointments scheduled within the next 15 minutes.");
                    alert3.showAndWait();

                }


                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("../view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else
            {
                outputFile.println(s + " " + usernameText.getText() + " unsuccessfully attempted to log in");
                outputFile.close();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(invalidLoginData);
                alert.setContentText(pleaseEnterValid);

                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK)
                {
                    usernameText.clear();
                    passwordText.clear();
                }

            }
        }
    }




    /** This method exits the application.
     *
     * @param event clicking the exit button.
     */
    @FXML
    void onActionExitApplication(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(confirmSure);
        alert.setContentText(confirmExit);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Button)(event.getSource())).getScene().getWindow().hide();
        }

    }


    /**
     * This method initializes the 'LOGIN' screen. It utilizes a language resource bundle to allow for the entire screen to be translated to French, based on the locale of the local machine.
     *
     * @param url the location.
     * @param resourceBundle the resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        try
        {

            ResourceBundle rb = ResourceBundle.getBundle("languages/language", Locale.getDefault());

            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr"))
            {
                titleLabel.setText(rb.getString("title"));
                subtitleLabel.setText(rb.getString("subtitle"));
                usernameLabel.setText(rb.getString("usernameLabel"));
                passwordLabel.setText(rb.getString("passwordLabel"));
                zoneIdLabel.setText(rb.getString("zoneIdLabel"));
                loginButton.setText(rb.getString("loginButton"));
                exitButton.setText(rb.getString("exit"));
                switchLabelZoneId.setText((ZoneId.systemDefault()).getId());
                confirmSure = rb.getString("confirmSure");
                confirmExit = rb.getString("confirmExit");
                invalidLoginData = rb.getString("invalidLoginData");
                pleaseEnterValid = rb.getString("pleaseEnterValid");

            }
        }
        catch (Exception e)
        {
            System.out.println();
        }



    }



}
