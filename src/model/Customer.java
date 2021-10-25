package model;


/** This class handles the customer.
 *
 * @author Ryan Zeigler*/
public class Customer
{
    public int customerId;
    public String customerName;
    public String address;
    public String postalCode;
    public String phone;
    public int divisionId;
    public int countryId;
    public String divisionName;


    /** Constructor for building an customer.
     *
     * @param customerId The id of the customer.
     * @param customerName The name of the customer.
     * @param address The address of the customer.
     * @param postalCode The postal code of the customer.
     * @param phone The phone number of the customer.
     * @param divisionId The division id for the customer.
     * @param countryId The country id for customer.
     * @param divisionName The division name for the customer.
     */
    public Customer (int customerId, String customerName, String address, String postalCode, String phone, int divisionId, int countryId, String divisionName)
    {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.countryId = countryId;
        this.divisionName = divisionName;
    }


    /**
     * @return the customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return the divisionName
     */
    public String getDivisionName() {
       return divisionName;
    }

}
