package gusrsilva.represent.Objects;

/**
 * Created by GusSilva on 3/7/16.
 */
public class Place {
    private String city, county, state, zip;

    public Place()
    {
        // Empty constructor
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPrettyLocation()
    {
        if(city != null && state != null)
            return city + ", " + state;
        else
            return null;
    }
}