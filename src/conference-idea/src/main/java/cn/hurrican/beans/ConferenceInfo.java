package cn.hurrican.beans;


import cn.hurrican.dtl.ConferenceMsg;
import cn.hurrican.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by NewObject on 2017/8/14.
 */
public class ConferenceInfo {
    private Long id;
    private String cnName;
    private String enName;
    private String name;
    private String tag;
    private String location;
    private String sponsor;
    private Date startdate;
    private Date enddate;
    private Date deadline;
    private Date acceptance;
    private String website;
    private String introduce;
    private String image;

    public static ConferenceInfo convertTo(ConferenceMsg c) throws ParseException {
        ConferenceInfo conference = new ConferenceInfo();

        conference.setTag(c.getTag());
        conference.setCnName(c.getCnName());
        conference.setEnName(c.getEnName());
        conference.setId(c.getId());
        conference.setLocation(c.getLocation());
        conference.setSponsor(c.getSponsor());
        conference.setWebsite(c.getWebsite());

        if (c.getStartdate() != null) {
            conference.setStartdate(DateUtils.convertStringToDate(c.getStartdate()));
        }
        if (c.getEnddate() != null) {
            conference.setEnddate(DateUtils.convertStringToDate(c.getEnddate()));
        }

        if (c.getDeadline() != null){
            conference.setDeadline(DateUtils.convertStringToDate(c.getDeadline()));
        }

        if (c.getAcceptance() != null) {
            conference.setAcceptance(DateUtils.convertStringToDate(c.getAcceptance()));
        }
        return conference;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id:\t").append(this.id)
                .append("\ncnName:\t").append(this.cnName)
                .append("\nenName:\t").append(this.enName)
                .append("\nlocation:\t").append(this.location)
                .append("\nsponsor:\t").append(this.sponsor)
                .append("\nwebsite:\t").append(this.website)
                .append("\nstartdate:\t").append(this.startdate)
                .append("\nenddate:\t").append(this.enddate)
                .append("\n");
        return builder.toString();
    }

    public ConferenceInfo(String cnName, String location,
                          String sponsor, String website) {
        this.cnName = cnName;
        this.location = location;
        this.sponsor = sponsor;
        this.website = website;
    }

    public ConferenceInfo() {

    }

    public ConferenceInfo(String cnName, String location, String sponsor) {
        this.cnName = cnName;
        this.location = location;
        this.sponsor = sponsor;
    }

    public ConferenceInfo(String cnName, String location,Date startdate,
                          String sponsor,String website) {
        this.cnName = cnName;
        this.location = location;
        this.startdate = startdate;
        this.sponsor = sponsor;
        this.website = website;
    }

    public ConferenceInfo(String cnName,String location, String sponsor,
                          Date startdate, Date enddate,String website) {
        this.cnName = cnName;
        this.location = location;
        this.sponsor = sponsor;
        this.startdate = startdate;
        this.enddate = enddate;
        this.website = website;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTag() {
        return tag;
    }

    public Long getId() {
        return id;
    }

    public String getCnName() {
        return cnName;
    }

    public String getEnName() {
        return enName;
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

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
