package cn.hurrican.dtl;

import cn.hurrican.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by NewObject on 2017/10/31.
 */
public class PraiseAndCollectRequestParams {

    private String startTime = null;
    private Integer offset = 7;
    private Integer uid;
    private Integer page = 1;
    private Integer perPageNumber = 8;


    public PraiseAndCollectRequestParams() {
    }

    public static class QueryParams{
        public Date startTime;
        public Date endTime;
        public Integer uid;
        public Integer skip;
        public Integer perPageNumber;
    }

    public static QueryParams parseToQueryParams(PraiseAndCollectRequestParams param)
            throws ParseException {
        QueryParams queryParams = new QueryParams();

        if (param.getStartTime() == null) {
            queryParams.startTime = null;
            queryParams.endTime = null;
        } else {
            if (param.getOffset() > 0) {

                queryParams.startTime = DateUtils.convertStringToDate(param.getStartTime());
                queryParams.endTime = DateUtils.calculateDateByCalendar(queryParams.startTime, param.getOffset());

            } else {
                queryParams.endTime = DateUtils.convertStringToDate(param.getStartTime());
                queryParams.startTime = DateUtils.calculateDateByCalendar(queryParams.endTime, param.getOffset());

            }
        }

        queryParams.perPageNumber = param.getPerPageNumber();
        queryParams.uid = param.getUid();
        queryParams.skip = param.getPage() != null && param.getPage() > 0 ? (queryParams.perPageNumber * (param.getPage()-1)) : 0;
        return queryParams;
    }

    @Override
    public String toString() {
        return "PraiseAndCollectRequestParams\n{\n" +
                "\tstartTime = '" + startTime + '\'' +
                "\n\toffset = " + offset +
                "\n\tuid = " + uid +
                "\n\tpage = " + page +
                ",\n\tperPageNumber = " + perPageNumber + "\n" +
                '}';
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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
