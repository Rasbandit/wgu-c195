package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Shawn
 */
public class Customer {
    
    //fields
    public static ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList();
    private int customerID;
    private int countryID;
    private int addressID;
    private int cityID;
    private String customerName;
    private String customerAddress;
    private String customerCountry;
    private String customerCity;
    private String postalCode;
    private String customerPhoneNum;
    private boolean active;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    //constructor
    public Customer(int customer_ID, String customerName, String address, String country, String phoneNum, boolean active, LocalDateTime createDate, 
            String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.customerID = customer_ID;
        this.customerName = customerName;
        this.customerAddress = address;
        this.customerCountry = country;
        this.customerPhoneNum = phoneNum;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public Customer(String customerName, String country, String address, String city, String postal, String phoneNum) {
        this.customerName = customerName;
        this.customerCountry = country;
        this.customerAddress = address;
        this.customerCity = city;
        this.postalCode = postal;
        this.customerPhoneNum = phoneNum;
    }

    public Customer() {
        
    }
    
    public static ObservableList<Appointment> getAssociatedAppointments() {
        return associatedAppointments;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerId) {
        this.customerID = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String address) {
        this.customerAddress = address;
    }
    
    public void setCustomerCity(String city) {
        this.customerCity = city;
    }
    
    public String getCustomerCity(){
        return customerCity;
    }
    
    public void setCustomerPostalCode(String p) {
        this.postalCode = p;
    }
    
    public String getCustomerPostalCode() {
        return postalCode;
    }   
    
    public String getCustomerCountry(){
        return customerCountry;
    }
    
    public void setCustomerCountry(String country){
        this.customerCountry = country;
    }
    public void setCustomerCountryID(int id){
        this.countryID = id;
    }
    public int getCustomerCountryID(){
        return countryID;
    }
    public void setCustomerAddressID(int id){
        this.addressID = id;
    }
    public int getCustomerAddressID(){
        return addressID;
    }
    public void setCustomerCityID(int id){
        this.cityID = id;
    }
    public int getCustomerCityID(){
        return cityID;
    }
    public String getCustomerPhoneNum(){
        return customerPhoneNum;
    }
    
    public void setCustomerPhone(String phoneNum){
        this.customerPhoneNum = phoneNum;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
