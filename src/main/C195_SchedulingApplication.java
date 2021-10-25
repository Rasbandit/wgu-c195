package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utilities.DBConnection;

/**
 *
 * @author Shawn
 */
public class C195_SchedulingApplication extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/LoginView.fxml"));
        Scene scene = new Scene(root);
        Image appIcon = new Image("/images/calendar.png");
        stage.setTitle("Scheduling Application");
        stage.getIcons().add(appIcon);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
   
        try {
            DBConnection.startConnection(); //this takes care of starting DB/creates connection object
            launch(args);
            /*any code after launch(args) is only going to be called once we/user closes all windows in applicatoin */            
            DBConnection.closeConnection();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
}




