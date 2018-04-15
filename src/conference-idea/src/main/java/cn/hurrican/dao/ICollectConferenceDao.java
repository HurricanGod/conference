package cn.hurrican.dao;

import cn.hurrican.beans.ConcernedConference;
import cn.hurrican.beans.ConferenceInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/10/30.
 */

public interface ICollectConferenceDao {


    /**
     * 把收藏的会议cid及用户uid保存到数据库
     * @param entity
     */
    void insertOneConcernedConference(ConcernedConference entity);


    /**
     * 根据uid和cid删除收藏的会议
     * @param params
     */
    void deleteOneConcernedConference(HashMap<String,Object> params);


    /**
     * 根据uid查询用户收藏的会议信息
     * @param params
     * @return
     */
    List<ConferenceInfo> queryCollectedConferenceByUserId(HashMap<String,Object> params);


    /**
     *
     * @param params
     * @return
     */
    Integer queryTheTotalOfUserHadCollectedConference(HashMap<String, Object> params);


    /**
     *  根据会议 cid 查询该会议被收藏的次数
     * @param params
     * @return
     */
    Integer queryTheNumberOfBeCollectedConference(HashMap<String,Object> params);


    /**
     *  根据 会议cid 和 用户uid 查询用户是否收藏的 会议id为cid的会议
     * @param params
     * @return
     */
    Integer queryUserIsCollectedTheConference(HashMap<String,Object> params);
}
