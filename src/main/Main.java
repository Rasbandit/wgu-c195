package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.DBConnection;

import java.sql.*;
import java.util.Locale;

/** This is the Main class of my application.
 *
 * <p><b>
 *
 * The javadocs folder is located in the Project primary folder.
 *
 * </b></p>
 *
 * @author Todd Rasband*/
public class Main extends Application
{
    /** This method sets up the initial JavaFX application stage.
     *
     * @param primaryStage The primary stage to be set.
     * @throws Exception The exception thrown.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
        primaryStage.setTitle("SCHEDULING SYSTEM");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /** <b>
     * The javadocs folder is located in the Project primary folder.
     * </b>
     *
     * <p>
     * This method calls for and establishes the database connection.
     * </p>
     * @param args The arguments.
     * @throws SQLException The exception that will be thrown in an error.
     */
    public static void main(String[] args) throws SQLException
    {
        // The following two lines were used for testing purposes
        //Locale.setDefault(new Locale("fr"));
        //System.setProperty("user.timezone", "America/New_York");

        DBConnection.startConnection();

        launch(args);

        DBConnection.endConnection();

    }
}
