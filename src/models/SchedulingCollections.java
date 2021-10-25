package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DBConnection;
import utilities.QueryManager;

/**
 *
 * @author Shawn
 */
public class SchedulingCollections {
    
    //fields
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    
    //get zoneId for user's local time
    private static ZoneId localZoneId = ZoneId.systemDefault();
    
    //methods
    public static ObservableList<Customer> getAllCustomers(){
        
        return allCustomers;
    }
    
    public static void addCustomer(Customer c){
        allCustomers.add(c);
    }
    
    public static void deleteCustomer(Customer c){
        QueryManager.makeQuery("DELETE FROM customer WHERE customerId = '" + c.getCustomerID() + "'");
        allCustomers.remove(c);
    }
    
    /**
     * This method returns all appointments
     */
    public static ObservableList<Appointment> getAllAppointments(){
       
        return allAppointments;
    }
    
    public static ObservableList<Appointment> getMonthlyAppointments(){
        
        return monthlyAppointments;
    }
    
    public static void addAppointmentAll(Appointment a){
        allAppointments.add(a);
    }
    public static void addAppointmentMonthly(Appointment a){
        monthlyAppointments.add(a);
    }
    public static void addAppointmentWeekly(Appointment a){
        weeklyAppointments.add(a);
    }
    public static void removeAppointmentMonthly(Appointment a){
        monthlyAppointments.remove(a);
    }
    public static void removeAppointmentWeekly(Appointment a){
        weeklyAppointments.remove(a);
    }
    public static void removeAppointmentFromAll(Appointment a){
        allAppointments.remove(a);
    }
    
    public static void deleteAppointment(Appointment a){
        QueryManager.makeQuery("DELETE FROM appointment WHERE appointmentId = '" + a.getAppointmentID() + "'");
        allAppointments.clear();
        weeklyAppointments.clear();
        monthlyAppointments.clear();
        populateAllAppointmentsList();
        populateWeeklyAppointmentsList();
        populateMonthlyAppointmentsList();
    }
    
    public static ObservableList<Country> getAllCountries(){
        return allCountries;
    }
    
    public static ObservableList<City> getAllCities(){
        return allCities;
    }
    
    public static ObservableList<Appointment> getWeeklyAppointments(){
        return weeklyAppointments;
    }
    
    
    public static void populateCustomersList(){
        try {
            QueryManager.makeQuery("SELECT * FROM customer JOIN address ON customer.addressId=address.addressId\n JOIN city ON address.cityId=city.cityId\n" +
                                    "JOIN country ON city.countryId=country.countryId");
            ResultSet customerSet = QueryManager.getResult();
            while(customerSet.next()){            
                Customer c = new Customer();
                c.setCustomerID(customerSet.getInt("customerId"));
                c.setCustomerName(customerSet.getString("customerName"));
                c.setCustomerAddress(customerSet.getString("address"));
                c.setCustomerPhone(customerSet.getString("phone"));
                c.setCustomerCountry(customerSet.getString("country"));
                c.setCustomerCountryID(customerSet.getInt("countryId"));
                c.setCustomerAddressID(customerSet.getInt("addressId"));
                c.setCustomerCityID(customerSet.getInt("cityId"));
                c.setCustomerCity(customerSet.getString("city"));
                c.setCustomerPostalCode(customerSet.getString("postalCode"));
                
                SchedulingCollections.getAllCustomers().add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void populateWeeklyAppointmentsList(){
        try {
            QueryManager.makeQuery("SELECT * FROM appointment JOIN customer ON appointment.customerId=customer.customerId \n" +
                "JOIN user ON appointment.userId=user.userId \n" +
                "WHERE START BETWEEN NOW() AND (SELECT ADDDATE(NOW(), INTERVAL 7 DAY))");
            ResultSet appmntSet = QueryManager.getResult();
            while(appmntSet.next()){
                Appointment appmnt = new Appointment();
                appmnt.setAppointmentID(appmntSet.getInt("appointmentId"));
                appmnt.setAppCustomerID(appmntSet.getInt("customerId"));
                appmnt.setAppointmentTitle(appmntSet.getString("title"));
                appmnt.setAppointmentType(appmntSet.getString("type"));
                appmnt.setAppointmentCustomer(appmntSet.getString("customerName"));
                appmnt.setAppointmentConsultant(appmntSet.getString("userName"));
                
                LocalDateTime startldt = appmntSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endldt = appmntSet.getTimestamp("end").toLocalDateTime();
                
                ZonedDateTime startzdt = startldt.atZone(ZoneId.of("UTC"));
                ZonedDateTime endzdt = endldt.atZone(ZoneId.of("UTC"));
                //convert to user's local time zone
                ZonedDateTime localStartZDT = startzdt.withZoneSameInstant(localZoneId);
                ZonedDateTime localEndZDT = endzdt.withZoneSameInstant(localZoneId);
                
                appmnt.setAppointmentStart(localStartZDT);
                appmnt.setAppointmentEnd(localEndZDT);
                SchedulingCollections.getWeeklyAppointments().add(appmnt);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void populateMonthlyAppointmentsList(){
        try {
            QueryManager.makeQuery("SELECT * FROM appointment JOIN customer ON appointment.customerId=customer.customerId JOIN user ON appointment.userId=user.userId\n" +
                    "WHERE start between (SELECT DATE_ADD(DATE_ADD(LAST_DAY(current_date()), INTERVAL 1 DAY), INTERVAL - 1 MONTH)) and DATE_ADD(LAST_DAY(current_date()), INTERVAL 1 DAY)");
            
            ResultSet appmntSet = QueryManager.getResult();
            while(appmntSet.next()){
                Appointment appmnt = new Appointment();
                appmnt.setAppointmentID(appmntSet.getInt("appointmentId"));
                appmnt.setAppCustomerID(appmntSet.getInt("customerId"));
                appmnt.setAppointmentTitle(appmntSet.getString("title"));
                appmnt.setAppointmentType(appmntSet.getString("type"));
                appmnt.setAppointmentCustomer(appmntSet.getString("customerName"));
                appmnt.setAppointmentConsultant(appmntSet.getString("userName"));
                
                LocalDateTime startldt = appmntSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endldt = appmntSet.getTimestamp("end").toLocalDateTime();
                
                ZonedDateTime startzdt = startldt.atZone(ZoneId.of("UTC"));
                ZonedDateTime endzdt = endldt.atZone(ZoneId.of("UTC"));
                //convert to user's local time zone
                ZonedDateTime localStartZDT = startzdt.withZoneSameInstant(localZoneId);
                ZonedDateTime localEndZDT = endzdt.withZoneSameInstant(localZoneId);
                
                appmnt.setAppointmentStart(localStartZDT);
                appmnt.setAppointmentEnd(localEndZDT);
                SchedulingCollections.getMonthlyAppointments().add(appmnt);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void populateAllAppointmentsList(){
        try {
            QueryManager.makeQuery("SELECT * FROM appointment JOIN customer ON appointment.customerId=customer.customerId JOIN user ON appointment.userId=user.userId");
            ResultSet appmntSet = QueryManager.getResult();
            while(appmntSet.next()){
                Appointment appmnt = new Appointment();
                appmnt.setAppointmentID(appmntSet.getInt("appointmentId"));
                appmnt.setAppCustomerID(appmntSet.getInt("customerId"));
                appmnt.setAppointmentTitle(appmntSet.getString("title"));
                appmnt.setAppointmentType(appmntSet.getString("type"));
                appmnt.setAppointmentCustomer(appmntSet.getString("customerName"));
                appmnt.setAppointmentConsultant(appmntSet.getString("userName"));
                
                LocalDateTime startldt = appmntSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endldt = appmntSet.getTimestamp("end").toLocalDateTime();
                
                ZonedDateTime startzdt = startldt.atZone(ZoneId.of("UTC"));
                ZonedDateTime endzdt = endldt.atZone(ZoneId.of("UTC"));
                //convert to user's local time zone
                ZonedDateTime localStartZDT = startzdt.withZoneSameInstant(localZoneId);
                ZonedDateTime localEndZDT = endzdt.withZoneSameInstant(localZoneId);
                
                appmnt.setAppointmentStart(localStartZDT);
                appmnt.setAppointmentEnd(localEndZDT);
                SchedulingCollections.getAllAppointments().add(appmnt);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static void updateCustomer(int index, Customer c) {
        allCustomers.set(index, c);
    }
    
    public static void updateWeeklyAppointment(int index, Appointment a){
        weeklyAppointments.set(index, a);
    }
    
    public static void updateMonthlyAppointment(int index, Appointment a){
        monthlyAppointments.set(index, a);
    }
    
    public static void updateAllAppointment(int index, Appointment a){
        allAppointments.set(index, a);
    }
    
    public static ObservableList<Appointment> getConsultantAppointments(){
        
        ObservableList<Appointment> consultantAppointments = FXCollections.observableArrayList();
        int consultantId = DBConnection.loggedInUser.getuserID();
        
        try {
            QueryManager.makeQuery("SELECT * FROM appointment JOIN customer ON appointment.customerId=customer.customerId JOIN user ON appointment.userId=user.userId\n" +
                    "WHERE appointment.userId = " + consultantId + "");
            
            ResultSet consultantSet = QueryManager.getResult();
            while(consultantSet.next()){
                Appointment appmnt = new Appointment();
                appmnt.setAppointmentID(consultantSet.getInt("appointmentId"));
                appmnt.setAppCustomerID(consultantSet.getInt("customerId"));
                appmnt.setAppointmentTitle(consultantSet.getString("title"));
                appmnt.setAppointmentType(consultantSet.getString("type"));
                appmnt.setAppointmentCustomer(consultantSet.getString("customerName"));
                appmnt.setAppointmentConsultant(consultantSet.getString("userName"));

                LocalDateTime startldt = consultantSet.getTimestamp("start").toLocalDateTime();
                LocalDateTime endldt = consultantSet.getTimestamp("end").toLocalDateTime();

                ZonedDateTime startzdt = startldt.atZone(ZoneId.of("UTC"));
                ZonedDateTime endzdt = endldt.atZone(ZoneId.of("UTC"));
                //convert to user's local time zone
                ZonedDateTime localStartZDT = startzdt.withZoneSameInstant(localZoneId);
                ZonedDateTime localEndZDT = endzdt.withZoneSameInstant(localZoneId);

                appmnt.setAppointmentStart(localStartZDT);
                appmnt.setAppointmentEnd(localEndZDT);
                consultantAppointments.add(appmnt);
            }   
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        
        return consultantAppointments;
    }
    
    public static ObservableList<AppointmentTypeByMonth> getAppointmentTypesByMonth(){
        ObservableList<AppointmentTypeByMonth> appTypesByMonthList = FXCollections.observableArrayList();
        
        try {
            QueryManager.makeQuery("SELECT MONTHNAME(`start`) AS Month, type, COUNT(*) as Amount\n" +
                "FROM appointment\n" +
                "GROUP BY MONTHNAME(`start`), type");
            ResultSet typeByMonthSet = QueryManager.getResult();
            while(typeByMonthSet.next()){
                AppointmentTypeByMonth appTypeByMonth = new AppointmentTypeByMonth();
                appTypeByMonth.setMonth(typeByMonthSet.getString("Month"));
                appTypeByMonth.setType(typeByMonthSet.getString("Type"));
                appTypeByMonth.setAmount(typeByMonthSet.getString("Amount"));
                appTypesByMonthList.add(appTypeByMonth);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return appTypesByMonthList;
    }
    
    public static Appointment checkUpcomingAppointment(){
        Appointment upcomingAppointment;
        int currentUserId = DBConnection.loggedInUser.getuserID();
        
        LocalDateTime now = LocalDateTime.now();
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime zdt = now.atZone(zid);
        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldt2 = ldt.plusMinutes(15);
        
        try{
            
            QueryManager.makeQuery("SELECT * FROM appointment \n" +
                "JOIN customer ON appointment.customerId=customer.customerId JOIN user ON appointment.userId=user.userId\n" +
                "WHERE start BETWEEN '" + ldt + "' AND '" + ldt2 + "' AND " + 
                "appointment.userId=" + currentUserId + "");
            
            ResultSet rs = QueryManager.getResult();
            if(rs.next()){
                upcomingAppointment = new Appointment();
                upcomingAppointment.setAppointmentID(rs.getInt("appointmentId"));
                upcomingAppointment.setAppCustomerID(rs.getInt("customerId"));
                upcomingAppointment.setAppointmentTitle(rs.getString("title"));
                upcomingAppointment.setAppointmentType(rs.getString("type"));
                upcomingAppointment.setAppointmentCustomer(rs.getString("customerName"));
                upcomingAppointment.setAppointmentConsultant(rs.getString("userName"));

                LocalDateTime startldt = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime endldt = rs.getTimestamp("end").toLocalDateTime();

                ZonedDateTime startzdt = startldt.atZone(ZoneId.of("UTC"));
                ZonedDateTime endzdt = endldt.atZone(ZoneId.of("UTC"));
                //convert to user's local time zone
                ZonedDateTime localStartZDT = startzdt.withZoneSameInstant(localZoneId);
                ZonedDateTime localEndZDT = endzdt.withZoneSameInstant(localZoneId);

                upcomingAppointment.setAppointmentStart(localStartZDT);
                upcomingAppointment.setAppointmentEnd(localEndZDT);
                return upcomingAppointment;
            }
        }catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        return null;
    }
}
