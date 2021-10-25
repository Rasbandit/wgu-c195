/*
This Class is designed to handle the following:
    1. Creation of a Static Statement Object
    2. Detecting query types
    3. Returning a ResultSet Object
 */
package utilities;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Shawn
 */
public class QueryManager {
    
    private static String query;
    private static Statement stmt;
    private static ResultSet result;
    
    // accepts query and execute based on query type
    public static void makeQuery(String q){
        query = q;
        
        try{
            //create Statement Object
            stmt = DBConnection.getConnection().createStatement();
            
            //Determine query execution
            if(query.toLowerCase().startsWith("select")){
                result = stmt.executeQuery(query);
            }
            if(query.toLowerCase().startsWith("delete") || query.toLowerCase().startsWith("insert") || query.toLowerCase().startsWith("update")){
                stmt.executeUpdate(query);
            }
        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static ResultSet getResult(){
        return result;
    }
}
