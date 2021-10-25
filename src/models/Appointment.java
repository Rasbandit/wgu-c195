package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 *
 * @author Shawn
 */
public class Appointment {
    
    private int appmntID;
    private int appCustomerID;
    private String appmntTitle;
    private String appmntDescription;
    private String appmntType;
    private ZonedDateTime appmntStart;
    private ZonedDateTime appmntEnd;
    private String appmntCustomer;
    private String appmntConsultant;
    private LocalDateTime appmntCreateDate;
    private String appmntCreatedBy;
    private Timestamp appmntLastUpdate;
    private String appmntLastUpdateBy;
    
    public Appointment(){
        
    }
    
    public void setAppointmentID(int id){
        this.appmntID = id;
    }
    
    public int getAppointmentID(){
        return appmntID;
    }
    public void setAppCustomerID(int id){
        this.appCustomerID = id;
    }
    public int getAppCustomerID(){
        return appCustomerID;
    }
    public String getAppointmentTitle(){
        return appmntTitle;
    }
    
    public void setAppointmentTitle(String title){
        this.appmntTitle = title;
    }
    
    public String getAppointmentType(){
        return appmntType;
    }
    
    public void setAppointmentType(String type){
        this.appmntType = type;
    }
    
    public void setAppointmentStart(ZonedDateTime start){
        this.appmntStart = start;
    }
    
    public ZonedDateTime getAppointmentStart(){
        return appmntStart;
    }
    
    public void setAppointmentEnd(ZonedDateTime end){
        this.appmntEnd = end;
    }
    public ZonedDateTime getAppointmentEnd(){
        return appmntEnd;
    }
    
    public void setAppointmentCustomer(String customer){
        this.appmntCustomer = customer;
    }
    
    public String getAppointmentCustomer(){
        return appmntCustomer;
    }
    
    public void setAppointmentConsultant(String consultant){
        this.appmntConsultant = consultant;
    }
    
    public String getAppointmentConsultant(){
        return appmntConsultant;
    }
        
}
