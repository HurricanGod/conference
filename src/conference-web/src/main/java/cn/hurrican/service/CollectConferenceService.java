package cn.hurrican.service;

import cn.hurrican.beans.ConcernedConference;
import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.dao.ICollectConferenceDao;
import cn.hurrican.dtl.PraiseAndCollectRequestParams;
import cn.hurrican.dtl.ConferenceMsg;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/10/30.
 */

@Service(value = "collectService")
public class CollectConferenceService {

    @Resource(name = "ICollectConferenceDao")
    private ICollectConferenceDao dao;


    public void saveConcernedConference(ConcernedConference concernedMsg){
        dao.insertOneConcernedConference(concernedMsg);
    }


    public void cancelCollectingConference(ConcernedConference c){

        HashMap<String,Object> map = new HashMap<>();
        map.put("cid", c.getCid());
        map.put("uid", c.getUid());

        dao.deleteOneConcernedConference(map);
    }


    public List<ConferenceMsg> queryCollectedConference(PraiseAndCollectRequestParams args)
            throws ParseException {

        PraiseAndCollectRequestParams.QueryParams queryParams = PraiseAndCollectRequestParams.parseToQueryParams(args);
        HashMap<String,Object> map = new HashMap<>();
        map.put("startTime", queryParams.startTime);
        map.put("endTime", queryParams.endTime);
        map.put("skip", queryParams.skip);
        map.put("pageNumber", queryParams.perPageNumber);
        map.put("uid", queryParams.uid);

        List<ConferenceInfo> conferenceInfos = dao.queryCollectedConferenceByUserId(map);
        List<ConferenceMsg> list = new ArrayList<>();

        conferenceInfos.forEach(c -> list.add(ConferenceMsg.convert(c)));

        return list;
    }

    public Integer queryTheNumberOfByCollectedConferenceService(Integer cid){

        HashMap<String,Object> param = new HashMap<>();
        param.put("cid", cid);
        return dao.queryTheNumberOfBeCollectedConference(param);
    }


    public Integer queryUserIsCollectedTheConferenceService(ConcernedConference c){
        HashMap<String,Object> map = new HashMap<>();
        map.put("cid", c.getCid());
        map.put("uid", c.getUid());

        return dao.queryUserIsCollectedTheConference(map);
    }


    public Integer queryTotalOfUserColectedConference(Integer uid){
        HashMap<String,Object> map = new HashMap<>();
        map.put("uid", uid);

        return dao.queryTheTotalOfUserHadCollectedConference(map);
    }
}
