package cn.hurrican.dao;

import cn.hurrican.beans.PublishMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NewObject on 2017/11/3.
 */
public interface IPublishDao {

    void savePublishConferenceByUser(PublishMessage entity);


    List<PublishMessage> queryUnCraweldUrl(HashMap<String,Object> map);

    void updatePublishConferenceSelective(PublishMessage obj);

    List<PublishMessage> queryPublishConferenceByCondition(Map<String,Object> map);
}
