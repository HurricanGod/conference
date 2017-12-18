package cn.hurrican.dtl;

import java.util.Date;

/**
 * Created by NewObject on 2017/10/31.
 */
public class ErrorConferenceRequestParams {

    private Date currentTime = new Date();
    private Integer page = null;
    private Integer perPageNumber = null;

    private String startTime;

    public ErrorConferenceRequestParams() {

    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPageNumber() {
        return perPageNumber;
    }

    public void setPerPageNumber(Integer perPageNumber) {
        this.perPageNumber = perPageNumber;
    }
}
