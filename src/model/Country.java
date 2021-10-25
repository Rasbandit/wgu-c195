package model;

/** This class handles the country.
 *
 * @author Ryan Zeigler*/
public class Country
{
    private int countryId;
    private String countryName;

    /** Constructor for building a country.
     *
     * @param countryId The id of the country.
     * @param countryName The name of the country.
     */
    public Country(int countryId, String countryName)
    {
        this.countryId = countryId;
        this.countryName = countryName;
    }


    /**
     * @return the countryId
     */
    public int getCountryId()
    {
        return countryId;
    }


    /**
     * @return the countryName
     */
    public String getCountryName()
    {
        return countryName;
    }


    /**
     * @return returns a name for the country for use in the combobox
     */
    @Override
    public String toString()
    {
        return (countryName);
    }

}
