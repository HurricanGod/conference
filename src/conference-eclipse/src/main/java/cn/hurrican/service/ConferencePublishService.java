package cn.hurrican.service;

import cn.hurrican.beans.PublishMessage;
import cn.hurrican.dao.IConferenceInfoDao;
import cn.hurrican.dao.IPublishDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/11/3.
 */

@Service(value = "publishService")
public class ConferencePublishService {

    @Resource(name = "IPublishDao")
    private IPublishDao dao;

    @Resource(name = "IConferenceInfoDao")
    private IConferenceInfoDao conferenceInfoDao;


    public boolean savePublishConferenceService(PublishMessage entity){
        HashMap<String, Object> map = new HashMap<>();
        Date currentTime = new Date();

        map.put("website", entity.getUri());
        map.put("currentTime", currentTime);

        Integer count = conferenceInfoDao.queryConferenceCountByWebsite(map);

        if (count > 0) {
            return false;
        }

        dao.savePublishConferenceByUser(entity);
        return true;
    }

    public List<PublishMessage> queryUnCrawledUrl(Integer... args){
        /**
         * @decription:
         * @param args
         * @return: java.util.List<PublishMessage>
         */
        HashMap<String, Object> map = new HashMap<>();
        map.put("skip", null);
        map.put("number", null);

        if (args != null && args.length > 0) {

            if (args.length == 1) {
                map.put("number", args[0] > 0 ? args[0] : 8);

            } else if (args.length == 2) {
                map.put("skip", args[0] > 0 ? (args[0] - 1) * args[1] : 0);
                map.put("number", args[1]);
            }
        }
        return dao.queryUnCraweldUrl(map);
    }
}
