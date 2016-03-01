package gusrsilva.represent;

/**
 * Created by GusSilva on 2/28/16.
 */
/**
 * Created by GusSilva on 2/24/16.
 */
public class Rep {

    private String repType;
    private String name;
    private String party;
    private String email;
    private String website;
    private String imageUri;
    private int imageResource;
    private int wideImageResource;
    private int color;

    public Rep()
    {
        //Default Constructor
    }

    public int getWideImageResource() {
        return wideImageResource;
    }

    public void setWideImageResource(int wideImageResource) {
        this.wideImageResource = wideImageResource;
    }

    public String getRepType() {
        return repType;
    }

    public void setRepType(String repType) {
        this.repType = repType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}

