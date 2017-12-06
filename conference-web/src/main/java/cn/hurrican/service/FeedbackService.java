package cn.hurrican.service;

import cn.hurrican.beans.ErrorConferenceInfo;
import cn.hurrican.beans.FeedbackSuggestion;
import cn.hurrican.dao.IFeedbackDao;
import cn.hurrican.dtl.ErrorConferenceInfoDetail;
import cn.hurrican.dtl.ErrorConferenceRequestParams;
import cn.hurrican.dtl.ErrorCorrectionInfo;
import cn.hurrican.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/10/31.
 */

@Service(value = "feedbackService")
public class FeedbackService {

    @Resource(name = "IFeedbackDao")
    private IFeedbackDao dao;

    public void saveOneErrorConferenceInfoService(ErrorConferenceInfo errorInfo){
        dao.insertSingleErrorConferenceInfo(errorInfo);
    }

    public void saveOneFeedbackInfoService(FeedbackSuggestion suggestion){
        dao.insertOneFeedbackInfo(suggestion);
    }


    public List<ErrorCorrectionInfo> queryErrorConference(ErrorConferenceRequestParams params){
        HashMap<String, Object> map = new HashMap<>();

        map.put("currentTime", params.getCurrentTime());
        if (params.getPerPageNumber() == null || params.getPage() == null) {
            map.put("skip", null);
            map.put("perPageNumber", null);
        } else {
            Integer skip = params.getPage() > 0 ? (params.getPage() -1) * params.getPerPageNumber() : 0;
            map.put("skip", skip);
            map.put("perPageNumber", params.getPerPageNumber());
        }

        List<ErrorConferenceInfoDetail> errorConferenceInfoDetails = dao.queryUnsolvedErrorConference(map);
        List<ErrorCorrectionInfo> list = new ArrayList<>();

        errorConferenceInfoDetails.forEach(e -> list.add(ErrorConferenceInfoDetail.parseToDtlObject(e)));

        return list;
    }


    public Integer queryUnsolvedErrorConferenceCount(){
        /**
         *  查询被用户纠正的会议总数
         */

        return dao.queryUnsolvedErrorConferenceCount();
    }


    public void solveErrorConferenceInfoService(Integer cid){
        /**
         *  对被用户纠错的会议信息进行修改，把其标志位设置为1
         */
        HashMap<String, Object> map = new HashMap<>();
        map.put("cid", cid);
        dao.updateUnsolvedErrorConferenceFlag(map);
    }

    public Integer queryUserSuggestionCountService(String... args){

        HashMap<String, Object> map = new HashMap<>();
        map.put("startTime", null);

        if (args != null && args.length == 1 && args[0] != null) {
            try {
                Date startTime = DateUtils.convertStringToDate(args[0]);
                map.put("startTime", startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return dao.queryUserSuggestionCount(map);
    }

    public List<FeedbackSuggestion> queryUserSuggestionService(String... params){
        if (params == null) {
            return new ArrayList<>();
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("startTime", null);
        map.put("skip", null);
        map.put("number", null);
        int len = params.length;
        switch (len){
            case 1:
                if (params[0] != null) {
                    getNumberQueryParam(map, params[0]);
                }
                break;
            case 2:
                if (params[0] != null && params[1] != null) {

                    getNumberAndSkipQueryParam(map, params);
                }
                break;
            case 3:
                if (params[0] != null) {

                    getNumberQueryParam(map, params[0]);

                    if (params[1] != null) {

                        getNumberAndSkipQueryParam(map, params);

                        if (params[2] != null) {
                            try {
                                Date startTime = DateUtils.convertStringToDate(params[2]);
                                map.put("startTime", startTime);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;

        }
        return dao.queryUserSuggestion(map);
    }





    private void getNumberAndSkipQueryParam(HashMap<String, Object> map, String[] params) {
        Integer perPageNumber = getNumberQueryParam(map, params[0]);

        int page = Integer.parseInt(params[1]);
        int skip = page > 0 ? (page-1) * perPageNumber : 0;
        map.put("skip", skip);
    }

    private Integer getNumberQueryParam(HashMap<String, Object> map, String param) {
        int perPageNumber = Integer.parseInt(param);
        perPageNumber = perPageNumber > 0 ? perPageNumber : 5;
        map.put("number", perPageNumber);
        return perPageNumber;
    }
}
