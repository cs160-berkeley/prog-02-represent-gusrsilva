package gusrsilva.represent;

/**
 * Created by GusSilva on 2/28/16.
 */

import java.util.ArrayList;

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
    private String bioId;
    private String twitterId;
    private String termStart, termEnd;
    private int imageResource;
    private int wideImageResource;
    private int color;
    private ArrayList<Bill> bills;

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
        if(repType.equalsIgnoreCase("house"))
            this.repType = "Representative";
        else
            this.repType = "Senator";
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
        if(party.equalsIgnoreCase("R")) {
            this.party = "Republican";
        }
        else {
            this.party = "Democrat";
        }
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

    public String getBioId() {
        return bioId;
    }

    public void setBioId(String bioId) {
        this.bioId = bioId;
    }


    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }


    public String getTermStart() {
        return termStart;
    }

    public void setTermStart(String termStart) {
        this.termStart = termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }


    public ArrayList<Bill> getBills() {
        return bills;
    }

    public void setBills(ArrayList<Bill> bills) {
        this.bills = bills;
    }

    @Override
    public String toString() {
        return "Rep{" +
                "repType='" + repType + '\'' +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", bioId='" + bioId + '\'' +
                ", twitterId='" + twitterId + '\'' +
                ", termStart='" + termStart + '\'' +
                ", termEnd='" + termEnd + '\'' +
                ", imageResource=" + imageResource +
                ", wideImageResource=" + wideImageResource +
                ", color=" + color +
                '}';
    }


}

