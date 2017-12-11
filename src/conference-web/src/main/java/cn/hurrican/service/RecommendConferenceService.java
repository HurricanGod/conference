package cn.hurrican.service;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.dao.IConferenceInfoDao;
import cn.hurrican.dtl.ConferenceMsg;
import cn.hurrican.dtl.QueryTopNumMeetingParam;
import cn.hurrican.utils.DateUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * Created by NewObject on 2017/10/29.
 */
@Component(value = "recommendService")
public class RecommendConferenceService {

    @Resource(name = "IConferenceInfoDao")
    private IConferenceInfoDao dao;


    public List<ConferenceMsg> recommendBePraisedConferenceTopNumberService
            (Integer top, Integer offsetDate, String startTime) throws ParseException {

        HashMap<String, Object> param = new HashMap<>();
        Date base = DateUtils.convertStringToDate(startTime);
        Date endDate = DateUtils.calculateDateByCalendar(base, offsetDate);

        param.put("startTime", base);
        param.put("endTime", endDate);
        param.put("top", top);

        List<ConferenceInfo> infos = dao.queryGivenLikedTopConferenceByUser(param);

        ArrayList<ConferenceMsg> msgs = new ArrayList<>();

        infos.forEach(e -> msgs.add(ConferenceMsg.convert(e)));

        return msgs;
    }


    public List<ConferenceMsg> recommendPopularTagConferenceService(QueryTopNumMeetingParam param) throws ParseException {
        HashMap<String, Object> map = new HashMap<>();

        Date startTime = param.getStartTime() == null ?
                new Date() : DateUtils.convertStringToDate(param.getStartTime());
        Date endTime = DateUtils.calculateDateByCalendar(startTime, param.getOffsetTime());
        Integer skip = param.getPage() != null && param.getPage() > 0
                ? ( param.getPage() - 1 ) * param.getPerPageNumber() : 0;

        if (param.getOffsetTime() > 0) {
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        } else {
            map.put("startTime", endTime);
            map.put("endTime", startTime);
        }

        map.put("tag", param.getTag());
        map.put("num", param.getPerPageNumber());
        map.put("skip", skip);

        List<ConferenceInfo> list = dao.queryPopularConferenceByTopTag(map);

        ArrayList<ConferenceMsg> conferenceMsgArrayList = new ArrayList<>();

        list.forEach(e -> conferenceMsgArrayList.add(ConferenceMsg.convert(e)));

        return conferenceMsgArrayList;
    }


    public Integer queryPopularTagConferenceCountService(QueryTopNumMeetingParam param) throws ParseException {
        HashMap<String, Object> map = new HashMap<>();


        Date startTime = param.getStartTime() == null ?
                new Date() : DateUtils.convertStringToDate(param.getStartTime());
        Date endTime = DateUtils.calculateDateByCalendar(startTime, param.getOffsetTime());

        if (param.getOffsetTime() > 0) {
            map.put("startTime", startTime);
            map.put("endTime", endTime);
        } else {
            map.put("startTime", endTime);
            map.put("endTime", startTime);
        }

        map.put("tag", param.getTag());

        Integer integer = dao.queryPopularConferenceByTopTagCount(map);

        return integer == null ? 0 : integer;
    }


    public List<String> queryLatestMostPopularOfTagBePraisedService(
            String startTime, Integer offset, Integer top) throws ParseException {

        HashMap<String, Object> map = new HashMap<>();
        Date startDate = DateUtils.convertStringToDate(startTime);
        Date endDate = DateUtils.calculateDateByCalendar(startDate, offset);

        if (offset > 0) {
            map.put("startTime",startDate);
            map.put("endTime", endDate);
        } else {
            map.put("startTime",endDate);
            map.put("endTime", startDate);
        }

        map.put("top", top);

        List<String> tagList = dao.queryLatestMostPopularOfTagBePraised(map);

        HashMap<String, Integer> tag2CountMap = new HashMap<>();


        tagList.stream().map(tagString -> Arrays.asList(tagString.split(",")))
                .forEach(list -> list.forEach(tag -> {

                    if (tag2CountMap.containsKey(tag)) {
                        Integer val = tag2CountMap.get(tag);
                        tag2CountMap.put(tag, val + 1);
                    } else {
                        tag2CountMap.put(tag, 1);
                    }
                }));

        Set<Map.Entry<String, Integer>> entrySet = tag2CountMap.entrySet();
        ArrayList<Map.Entry<String, Integer>> entryArrayList = new ArrayList<>(entrySet);

        Collections.sort(entryArrayList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        ArrayList<String> tags = new ArrayList<>();
        entryArrayList.forEach(entry -> tags.add(entry.getKey()));

        return tags;
    }



}
