package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/** This class handles the database connection.
 *
 * @author Ryan Zeigler*/
public class DBConnection
{

    //JDBC URL parts
    private static String protocol = "jdbc";
    private static String vendorName = ":mysql:";
    private static String ipAddress = "//localhost:3306/";
    private static String dbName = "client_schedule";


    //JDBC URL
    private static String jdbcURL = protocol + vendorName + ipAddress + dbName;

    //Driver and Connection Interface reference
    private static String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    //Username and password
    private static String username = "sqlUser";
    private static String password = "Passw0rd!";


    /**
     * This method establishes a connection with the database.
     *
     * @return an established connection
     */
    public static Connection startConnection()
    {
        try
        {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection to Database was successful!");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return conn;

    }


    /**
     * This method gets the connection with the database.
     *
     * @return the connection
     */
    public static Connection getConnection()
    {
        return conn;
    }


    /**
     * This method ends the connection with the database.
     */
    public static void endConnection()
    {
        try
        {
            conn.close();
            System.out.println("Connection to Database has been terminated.");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }




}
