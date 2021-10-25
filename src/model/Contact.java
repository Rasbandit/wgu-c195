package model;


/** This class handles the contact.
 *
 * @author Ryan Zeigler*/
public class Contact
{
    public int contactId;
    public String contactName;
    public String contactEmail;

    /** Constructor for building a contact.
     *
     * @param contactId The id of the contact.
     * @param contactName The Name of the contact.
     * @param contactEmail The Email of the contact.
     */
    public Contact (int contactId, String contactName, String contactEmail)
    {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
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
     * @return the contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
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
     * @param contactEmail the contactEmail to set
     */
    public void setEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }


    /**
     * @return returns a contact name for use in the combobox
     */
    @Override
    public String toString()
    {
        return (contactName);
    }
}
