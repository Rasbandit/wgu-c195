package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/** This class handles the database interactions dealing with contacts.
 *
 * @author Todd Rasband*/
public class DBContacts
{

    /**
     * This method returns all contacts in the database.
     *
     * @return all contacts in the database
     */
    public static ObservableList<Contact> getAllContacts()
    {

        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try
        {
            String sql = "SELECT * from contacts";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                Contact c = new Contact(contactId, contactName, contactEmail);
                contactList.add(c);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return contactList;
    }
}

