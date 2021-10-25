package model;


/** This class handles the division.
 *
 * @author Todd Rasband*/
public class Division
{
    private int divisionId;
    private String divisionName;
    private int countryId;


    /** Constructor for building an division.
     *
     * @param divisionId The id of the division.
     * @param divisionName The name of the division.
     * @param countryId The country id for the division.
     */
    public Division (int divisionId, String divisionName, int countryId)
    {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * @return the divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @return the divisionName
     */
    public String getName() {
        return divisionName;
    }

    /**
     * @return the countryId
     */
    public int getCountryId() {
        return countryId;
    }



    /**
     * @param divisionId the divisionId to set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @param divisionName the divisionName to set
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * @param countryId the countryId to set
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }


    /**
     * @return returns a division name for use in the combobox
     */
    @Override
    public String toString()
    {
        return (divisionName);
    }

}
