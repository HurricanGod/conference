package cn.hurrican.service;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.dao.IConferenceInfoDao;
import cn.hurrican.dtl.ConferenceMsg;
import cn.hurrican.dtl.ConferenceTag;
import cn.hurrican.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * Created by NewObject on 2017/8/14.
 */

@Service(value = "conferenceInfoService")
public class ConferenceInfoService {

    @Resource(name = "IConferenceInfoDao")
    private IConferenceInfoDao dao;

    public List<ConferenceMsg> queryLatestConference(Integer offset, Integer number,
                                                     Integer position){
        HashMap<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("number", number);
        params.put("time", new Date());
        params.put("endTime", DateUtils.getBeforeOrAfterSomeDayFromToday(position));

        List<ConferenceInfo> list = dao.queryLatestConferenceInfo(params);
        List<ConferenceMsg> conferenceMsgs = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            conferenceMsgs.add(ConferenceMsg.convert(list.get(i)));

        }
        return conferenceMsgs;
    }

    public List<ConferenceMsg> queryLatestConferenceByTag(Integer offset, Integer number,
                                                          Integer position, List<String> tags){
        HashMap<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("number", number);
        params.put("time", new Date());
        params.put("endTime", DateUtils.getBeforeOrAfterSomeDayFromToday(position));
        params.put("tags",tags);

        List<ConferenceInfo> list = dao.queryLatestConcernedConferenceInfo(params);

        List<ConferenceMsg> conferenceMsgs = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            conferenceMsgs.add(ConferenceMsg.convert(list.get(i)));

        }
        return conferenceMsgs;

    }

    private HashMap<String, Integer> queryLatestConferenceHotTag(){
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Integer> hotTagMap = new HashMap<>();

        params.put("start", new Date());

        List<String> hotTagsList = dao.queryLatestTags(params);

        hotTagsList.forEach(s ->{
            if (s != null && ! s.equals("")) {
                String[] tags = s.split(",");

                for (int i = 0; i < tags.length; i++) {
                    String tag = tags[i];

                    if (hotTagMap.keySet().contains(tag)) {
                        Integer value = hotTagMap.get(tag);
                        hotTagMap.put(tag, value+1);
                    } else {
                        hotTagMap.put(tag, 1);
                    }
                }
            }

        });
        return hotTagMap;
    }

    public List<ConferenceTag> queryLatestConferenceHotTagTopN(Integer... n){

        HashMap<String, Integer> map = queryLatestConferenceHotTag();

        Set<Map.Entry<String, Integer>> entries = map.entrySet();

        ArrayList<Map.Entry<String, Integer>> entryList = new ArrayList<>(entries);

        Collections.sort(entryList,(e1,e2)-> e2.getValue().compareTo(e1.getValue()));

        ArrayList<ConferenceTag> topTagMapList = new ArrayList<>();

        entryList.forEach(ele -> topTagMapList.add(new ConferenceTag(ele.getKey(), ele.getValue())));

        if (n == null || n.length == 0) {
            return topTagMapList;
        }
        Integer toIndex = n[0];

        return toIndex <= entryList.size() ?
                topTagMapList.subList(0, toIndex) : topTagMapList;
    }


    public Integer queryLatestConferenceInfoCountService(Integer offset){
        HashMap<String, Object> map = new HashMap<>();
        Date current = new Date();

        if (offset < 0) {
            map.put("startTime", DateUtils.calculateDateByCalendar(current, offset));
            map.put("endTime", current);
        } else {
            map.put("startTime", current);
            map.put("endTime", DateUtils.calculateDateByCalendar(current, offset));
        }

        return dao.queryLatestConferenceInfoCount(map);
    }


    public boolean updateConferenceInfo(ConferenceMsg conference){
        /**
         * @decription: 更新会议信息，ConferenceMsg的id属性不允许为 null
         * @param conference
         * @return: boolean
         */
        try {
            dao.updateConferenceTag(ConferenceInfo.convertTo(conference));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
