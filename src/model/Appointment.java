package model;

import java.sql.Timestamp;


/** This class handles the appointment.
 *
 * @author Ryan Zeigler*/
public class Appointment
{
    public int appointmentId;
    public String title;
    public String description;
    public String location;
    public int contactId;
    public String contactName;
    public String type;
    public Timestamp start;
    public Timestamp end;
    public int customerId;
    public int userId;


    /** Constructor for building an appointment.
     *
     * @param appointmentId The id of the appointment.
     * @param title The title of the appointment.
     * @param description The description of the appointment.
     * @param location The location of the appointment.
     * @param contactId The contact id for the appointment.
     * @param contactName The contact name for the appointment.
     * @param type The type of appointment.
     * @param start The start time and date of the appointment.
     * @param end The end time and date of the appointment.
     * @param customerId The customerID for the appointment.
     * @param userId The userID for with the appointment.
     */
    public Appointment (int appointmentId, String title, String description, String location, int contactId, String contactName, String type, Timestamp start, Timestamp end, int customerId, int userId)
    {
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactId = contactId;
        this.contactName = contactName;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
    }


    /**
     * @return the appointmentId
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the contactId
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the start
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }





    /**
     * @param appointmentId the appointmentId to set
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @param contactId the contactId to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

}
