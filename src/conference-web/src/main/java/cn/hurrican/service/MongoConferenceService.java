package cn.hurrican.service;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.dtl.ConferenceMsg;
import cn.hurrican.dtl.ConferenceTag;
import cn.hurrican.mongodbdao.ConferenceDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by NewObject on 2017/10/4.
 */

@Service(value = "mongoConferenceService")
public class MongoConferenceService {

    @Resource(name = "conferenceMongoDao")
    private ConferenceDao mongodao;



    public List<ConferenceMsg> queryAllConferenceLimit(Integer skip, Integer limit){
        List<ConferenceInfo> conferences = mongodao.findAllLimit(skip, limit, "conference");

        List<ConferenceMsg> list = new ArrayList<>(conferences.size());

        for (int i = 0; i < conferences.size(); i++) {
            ConferenceInfo info = conferences.get(i);
            list.add(ConferenceMsg.convert(info));

        }

        return list;
    }

    public List<ConferenceMsg> queryLatestConference(Integer days){
        List<ConferenceInfo> conference = mongodao.selectConferenceLastly(days, "conference");
        List<ConferenceMsg> list = new ArrayList<>(conference.size());

        for (int i = 0; i < conference.size(); i++) {
            ConferenceInfo info = conference.get(i);
            list.add(ConferenceMsg.convert(info));

        }
        return list;
    }


    public List<ConferenceTag> queryConferenceTag(Integer top){
        List<ConferenceInfo> conferences = mongodao.selectTag("conference");

        TreeMap<String, Integer> map = new TreeMap<>();

        for (int i = 0; i < conferences.size(); i++) {
            ConferenceInfo c = conferences.get(i);

            String tags = c.getTag();
            if (tags != null) {

                String[] tag = tags.split(",");

                for (int j = 0; j < tag.length; j++) {
                    String s = tag[j];

                    if (map.containsKey(s)) {
                        Integer oldval = map.get(s);
                        map.replace(s, oldval + 1);
                    } else {
                        map.put(s, 1);
                    }
                }
            }
        }

        List<Map.Entry<String,Integer>> sortMapValueList = new ArrayList<>(map.entrySet());

        Collections.sort(sortMapValueList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        ArrayList<ConferenceTag> result = new ArrayList<>();

        for (int i = 0; i < sortMapValueList.size(); i++) {
            Map.Entry<String, Integer> entry = sortMapValueList.get(i);

            if (i < top) {
                ConferenceTag tag = new ConferenceTag();
                tag.setTag(entry.getKey());
                result.add(tag);
            } else {
                break;
            }

        }

        return result;
    }



    public void setMongodao(ConferenceDao mongodao) {
        this.mongodao = mongodao;
    }
}
