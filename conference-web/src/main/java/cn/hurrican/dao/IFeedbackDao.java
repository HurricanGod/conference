package cn.hurrican.dao;

import cn.hurrican.beans.ErrorConferenceInfo;
import cn.hurrican.beans.FeedbackSuggestion;
import cn.hurrican.dtl.ErrorConferenceInfoDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/10/31.
 */
public interface IFeedbackDao {

    /**
     *
     * @param errorInfo
     */
    void insertSingleErrorConferenceInfo(ErrorConferenceInfo errorInfo);


    /**
     *
     * @param suggestion
     */
    void insertOneFeedbackInfo(FeedbackSuggestion suggestion);


    /**
     *
     * @param param
     * @return
     */
    List<ErrorConferenceInfoDetail> queryUnsolvedErrorConference(HashMap<String, Object> param);


    /**
     *
     * @return
     */
    Integer queryUnsolvedErrorConferenceCount();


    /**
     *
     * @param param
     */
    void updateUnsolvedErrorConferenceFlag(HashMap<String, Object> param);


    /**
     *
     * @param param
     * @return
     */
    List<FeedbackSuggestion> queryUserSuggestion(HashMap<String, Object> param);


    Integer queryUserSuggestionCount(HashMap<String, Object> param);

}
