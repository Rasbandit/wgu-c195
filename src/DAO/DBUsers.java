package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/** This class handles the database interactions dealing with users.
 *
 * @author Ryan Zeigler*/
public class DBUsers
{

    /**
     * This method returns all users in the database.
     *
     * @return all users in the database
     */
    public static ObservableList<User> getAllUsers()
    {

        ObservableList<User> userlist = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from users";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User u = new User(userId, userName, password);
                userlist.add(u);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return userlist;
    }

    /**
     * This method validates the login entry information that is supplied by the user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return true if valid information is supplied, false if invalid information is supplied
     */
    public static int validateUser(String username, String password)
    {
        String sql = "SELECT * FROM users WHERE user_name = '" + username + "' AND password = '" + password +"'";

        try
        {
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            {
                rs.next();
                if (rs.getString("User_Name").equals(username))
                {
                   if (rs.getString("Password").equals(password))
                    {
                        return rs.getInt("User_ID");
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

}
