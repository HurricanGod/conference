package cn.hurrican.dtl;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.utils.DateUtils;

/**
 * Created by NewObject on 2017/10/6.
 */
public class ConferenceMsg {

    private Long id;
    private String cnName;
    private String enName;
    private String tag;
    private String location;
    private String sponsor;
    private String startdate;
    private String enddate;
    private String deadline;
    private String acceptance;
    private String website;

    public static ConferenceMsg convert(ConferenceInfo conference){
        ConferenceMsg msg = new ConferenceMsg();

        msg.setId(conference.getId());
        msg.setCnName(conference.getCnName());
        msg.setEnName(conference.getEnName());
        msg.setTag(conference.getTag());
        msg.setLocation(conference.getLocation());
        msg.setSponsor(conference.getSponsor());
        msg.setWebsite(conference.getWebsite());
        msg.setStartdate(DateUtils.convertDateToString(conference.getStartdate()));
        msg.setEnddate(DateUtils.convertDateToString(conference.getEnddate()));
        msg.setDeadline(DateUtils.convertDateToString(conference.getDeadline()));
        msg.setAcceptance(DateUtils.convertDateToString(conference.getAcceptance()));

        return msg;
    }


    public ConferenceMsg(String cnName, String enName, String tag, String location,
                         String sponsor, String startdate, String enddate, String deadline,
                         String website) {
        this.cnName = cnName;
        this.enName = enName;
        this.tag = tag;
        this.location = location;
        this.sponsor = sponsor;
        this.startdate = startdate;
        this.enddate = enddate;
        this.deadline = deadline;
        this.website = website;
    }

    public ConferenceMsg() {
    }

    @Override
    public String toString() {
        return "ConferenceMsg{" +
                "id=" + id +
                ", cnName='" + cnName + '\'' +
                ", enName='" + enName + '\'' +
                ", tag='" + tag + '\'' +
                ", location='" + location + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", website='" + website + '\'' +
                '}';
    }

    public String getCnName() {
        return cnName;
    }

    public String getEnName() {
        return enName;
    }


    public String getTag() {
        return tag;
    }

    public String getLocation() {
        return location;
    }

    public String getSponsor() {
        return sponsor;
    }

    public String getStartdate() {
        return startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public String getWebsite() {
        return website;
    }


    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
