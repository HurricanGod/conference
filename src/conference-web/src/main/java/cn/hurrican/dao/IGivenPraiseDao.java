package cn.hurrican.dao;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.beans.GivenPraiseBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/10/30.
 */
public interface IGivenPraiseDao {

    /**
     *
     * @param params
     * @return
     */
    Integer selectConferenceIsBePraised(HashMap<String,Object> params);

    /**
     *
     * @param entity
     */
    void insertOneBeAdmiredConference(GivenPraiseBean entity);


    /**
     *
     * @param params
     */
    void deleteOneBeAdmiredConference(HashMap<String,Object> params);


    /**
     *
     * @param params
     * @return
     */
    List<ConferenceInfo> queryBeAdmiredConferenceByUid(HashMap<String,Object> params);


    Integer queryUserHadAdmiredConferenceCount(HashMap<String,Object> params);
    /**
     *
     * @param params
     * @return
     */
    Integer queryTheNumberOfBeAdmiredConferenceByCid(HashMap<String,Object> params);
}
