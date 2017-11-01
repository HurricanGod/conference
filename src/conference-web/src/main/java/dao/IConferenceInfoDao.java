package dao;

import beans.ConferenceInfo;

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

}
