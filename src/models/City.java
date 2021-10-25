package models;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import utilities.QueryManager;
import java.sql.SQLException;

/**
 *
 * @author Shawn
 */
public class City {
    
    private int cityId;
    private String city;
    private int countryId;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public int getCityId() {
        return cityId;
    }
    
    public int getCityId(String city){
        int cityId = 0;
        
        try {
            QueryManager.makeQuery("SELECT cityId FROM city\n" +
            "WHERE city = '" + city + "'");
            ResultSet set = QueryManager.getResult();
            if(set.next()){
                cityId = set.getInt("cityId");
            }
        } catch (SQLException e) {
            System.out.println("Error: getCityId() didn't work" + e.getMessage());
        }
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
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
