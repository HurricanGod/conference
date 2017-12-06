package cn.hurrican.beans;

import java.util.Date;

/**
 * Created by NewObject on 2017/10/31.
 */
public class ErrorConferenceInfo {

    private Integer eid;
    private Integer conferenceid;
    private String errordetail;
    private Date submitTime = new Date();
    private Integer isSolve;

    public ErrorConferenceInfo(Integer conferenceid, String errordetail) {
        this.conferenceid = conferenceid;
        this.errordetail = errordetail;
    }

    public ErrorConferenceInfo() {
    }

    @Override
    public String toString() {
        return "ErrorConferenceInfo{" +
                "eid=" + eid +
                ", conferenceid=" + conferenceid +
                ", errordetail='" + errordetail + '\'' +
                ", submitTime=" + submitTime +
                ", isSolve=" + isSolve +
                '}';
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getIsSolve() {
        return isSolve;
    }

    public void setIsSolve(Integer isSolve) {
        this.isSolve = isSolve;
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public Integer getConferenceid() {
        return conferenceid;
    }

    public void setConferenceid(Integer conferenceid) {
        this.conferenceid = conferenceid;
    }

    public String getErrordetail() {
        return errordetail;
    }

    public void setErrordetail(String errordetail) {
        this.errordetail = errordetail;
    }


}
