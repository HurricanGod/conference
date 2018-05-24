package cn.hurrican.dao;

import cn.hurrican.beans.ConferenceInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NewObject on 2017/8/14.
 */
public interface IConferenceInfoDao {
    boolean addConferenceInfo(ConferenceInfo conference);
    List<ConferenceInfo> queryConferenceInfoByIf(ConferenceInfo conference);
    List<ConferenceInfo> queryConferenceInfoByIf(Map<String, Object>map);
    List<ConferenceInfo> queryConferenceInfoByForEach(String[] cities);
    List<ConferenceInfo> queryConferenceInfoByForEachList(List<String> cities);

    /**
     *  分页查询一段时间内要举办的会议
     * @param params 包括 4 个参数，分别为 time,endTime,offset,number
     *               time,endTime 为时间范围期间，
     *               offset,number为分页查询 limit 参数
     * @return
     */
    List<ConferenceInfo> queryLatestConferenceInfo(HashMap<String, Object> params);


    /**
     *  根据给定 Tag 集合分页查询近期要举办的会议
     * @param params
     * @return
     */
    List<ConferenceInfo> queryLatestConcernedConferenceInfo(HashMap<String, Object> params);


    /**
     *
     * @param map {"keywords": List,"offset": Integer,"number": Integer}
     * @return
     */
    List<ConferenceInfo> queryConferenceByKeyWords(Map<String, Object> map);
    /**
     *  查询刚爬取未归类的会议信息
     * @return
     */
    List<ConferenceInfo> queryUnClassifyConferenceByTag();


    /**
     * 更新会议的Tag，并把会议标志位 isClassify 设置为1
     * @param c
     */
    void updateConferenceTag(ConferenceInfo c);


    /**
     * 查询今天以后的会议的 Tag 信息
     * @param params map 中只有一个当前日期 Date 对象
     * @return 近期会议的 Tag
     */
    List<String> queryLatestTags(HashMap<String, Object> params);


    /**
     *  查询一段时间内点赞次数前n的会议
     */
    List<ConferenceInfo> queryGivenLikedTopConferenceByUser(HashMap<String, Object> params);


    /**
     *
     * @param params
     * @return
     */
    List<ConferenceInfo> queryPopularConferenceByTopTag(HashMap<String, Object> params);


    /**
     *
     * @param params
     * @return
     */
    Integer queryPopularConferenceByTopTagCount(HashMap<String, Object> params);


    /**
     *
     * @param params
     * @return
     */
    List<String> queryLatestMostPopularOfTagBePraised(HashMap<String, Object> params);


    /**
     *
     * @param params
     * @return
     */
    Integer queryLatestConferenceInfoCount(HashMap<String, Object> params);


    Integer queryConferenceCountByWebsite(HashMap<String, Object> params);


    void updateConferenceInfo(ConferenceInfo c);

    /**
     * 查询被置顶的会议
     * @param params {"number":"int,置顶的会议条数"}
     * @return
     */
    List<ConferenceInfo> queryConferenceByOrderLevel(Map<String, Object> params);

    /**
     * 查询会议的简介
     */
    List<String> getConferenceIntro(HashMap<String, Object> params);

    /**
     * 获取图片
     */
    List<String> getConferenceImage(HashMap<String, Object> params);

}
