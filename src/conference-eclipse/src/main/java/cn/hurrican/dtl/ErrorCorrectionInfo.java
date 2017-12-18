package cn.hurrican.dtl;

/**
 * Created by NewObject on 2017/10/31.
 */
public class ErrorCorrectionInfo {

    private Integer eid;
    private String errorDetail;
    private ConferenceMsg conference;

    public ErrorCorrectionInfo() {
    }

    public Integer getEid() {
        return eid;
    }

    public void setEid(Integer eid) {
        this.eid = eid;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }


    public ConferenceMsg getConference() {
        return conference;
    }

    public void setConference(ConferenceMsg conference) {
        this.conference = conference;
    }
}
