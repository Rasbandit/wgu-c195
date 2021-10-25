package model;


/** This class handles the user.
 *
 * @author Todd Rasband*/
public class User
{
    public int userId;
    public String userName;
    public String password;

    /** Constructor for building an user.
     *
     * @param userId The id of the user.
     * @param userName The name of the user.
     * @param password The password of the user.
     */
    public User (int userId, String userName, String password)
    {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }



    /**
     * @return returns a name for the user for use in the combobox
     */
    @Override
    public String toString()
    {
        return "[" + userId + "] " + userName;
    }
}