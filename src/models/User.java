package models;

/**
 *
 * @author Shawn
 */
public class User {
    
    private int userID;
    private String username;
    private String password;
    private int active;
    
    
    public int getuserID(){
        return userID;
    }
    public void setuserID(int id){
        userID = id;
    }
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
}
