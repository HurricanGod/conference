package beans;


import java.sql.Date;

/**
 * Created by NewObject on 2017/8/14.
 */
public class ConferenceInfo {
    private Long id;
    private String cnname;
    private String enname;
    private String tag;
    private String location;
    private String sponsor;
    private Date startdate;
    private Date enddate;
    private Date deadline;
    private Date acceptance;
    private String website;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id:\t").append(this.id)
                .append("\ncnname:\t").append(this.cnname)
                .append("\nenname:\t").append(this.enname)
                .append("\nlocation:\t").append(this.location)
                .append("\nsponsor:\t").append(this.sponsor)
                .append("\nwebsite:\t").append(this.website)
                .append("\nstartdate:\t").append(this.startdate.toString())
                .append("\nenddate:\t").append(this.enddate.toString())
                .append("\n");
        return builder.toString();
    }

    public ConferenceInfo(String cnname, String location,
                          String sponsor, String website) {
        this.cnname = cnname;
        this.location = location;
        this.sponsor = sponsor;
        this.website = website;
    }

    public ConferenceInfo() {
        this.id = 0L;
    }

    public ConferenceInfo(String cnname, String location, String sponsor) {
        this.cnname = cnname;
        this.location = location;
        this.sponsor = sponsor;
    }

    public ConferenceInfo(String cnname, String location,Date startdate,
                          String sponsor,String website) {
        this.cnname = cnname;
        this.location = location;
        this.startdate = startdate;
        this.sponsor = sponsor;
        this.website = website;
    }

    public ConferenceInfo(String cnname,String location, String sponsor,
                          Date startdate, Date enddate,String website) {
        this.cnname = cnname;
        this.location = location;
        this.sponsor = sponsor;
        this.startdate = startdate;
        this.enddate = enddate;
        this.website = website;
    }

    public String getTag() {
        return tag;
    }

    public Long getId() {
        return id;
    }

    public String getCnname() {
        return cnname;
    }

    public String getEnname() {
        return enname;
    }

    public String getLocation() {
        return location;
    }

    public String getSponsor() {
        return sponsor;
    }

    public Date getStartdate() {
        return startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public Date getAcceptance() {
        return acceptance;
    }

    public String getWebsite() {
        return website;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setAcceptance(Date acceptance) {
        this.acceptance = acceptance;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
