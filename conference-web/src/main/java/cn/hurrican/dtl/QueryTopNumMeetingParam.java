package cn.hurrican.dtl;

/**
 * Created by NewObject on 2017/10/29.
 */
public class QueryTopNumMeetingParam {

    private String tag;
    private String startTime;
    private Integer offsetTime = 7;
    private Integer page;
    private Integer perPageNumber = 8;

    @Override
    public String toString() {
        return "QueryTopNumMeetingParam\n{" +
                "tag='" + tag + '\'' +
                ", \nstartTime='" + startTime + '\'' +
                ", \noffsetTime=" + offsetTime +
                ", \npage=" + page +
                ", \nperPageNumber=" + perPageNumber + "\n" +
                '}';
    }

    public String getTag() {
        return tag;
    }

    public String getStartTime() {
        return startTime;
    }

    public Integer getOffsetTime() {
        return offsetTime;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPerPageNumber() {
        return perPageNumber;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setOffsetTime(Integer offsetTime) {
        this.offsetTime = offsetTime;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setPerPageNumber(Integer perPageNumber) {
        this.perPageNumber = perPageNumber;
    }
}
