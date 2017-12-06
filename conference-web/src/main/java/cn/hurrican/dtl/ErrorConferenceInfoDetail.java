package cn.hurrican.dtl;

import cn.hurrican.beans.ConferenceInfo;

/**
 * Created by NewObject on 2017/10/31.
 */
public class ErrorConferenceInfoDetail {

    private Integer eid;
    private String errorDetail;
    private ConferenceInfo info;

    public ErrorConferenceInfoDetail() {
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

    public ConferenceInfo getInfo() {
        return info;
    }

    public void setInfo(ConferenceInfo info) {
        this.info = info;
    }


    public static ErrorCorrectionInfo parseToDtlObject(ErrorConferenceInfoDetail bean){
        ErrorCorrectionInfo entity = new ErrorCorrectionInfo();
        entity.setEid(bean.getEid());
        entity.setErrorDetail(bean.getErrorDetail());
        entity.setConference(ConferenceMsg.convert(bean.getInfo()));

        return entity;
    }
}

