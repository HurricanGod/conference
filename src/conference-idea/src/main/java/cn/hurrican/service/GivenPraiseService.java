package cn.hurrican.service;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.beans.GivenPraiseBean;
import cn.hurrican.dao.IGivenPraiseDao;
import cn.hurrican.dtl.ConferenceMsg;
import cn.hurrican.dtl.PraiseAndCollectRequestParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/10/30.
 */

@Service(value = "givenPraiseService")
public class GivenPraiseService {

    @Resource(name = "IGivenPraiseDao")
    private IGivenPraiseDao dao;


    public void saveBeAdmiredConference(GivenPraiseBean entity){
        dao.insertOneBeAdmiredConference(entity);
    }


    public void dropBeAdmiredConference(GivenPraiseBean entity){
        HashMap<String, Object> param = new HashMap<>();

        param.put("uid", entity.getUid());
        param.put("conferenceid", entity.getConferenceid());

        dao.deleteOneBeAdmiredConference(param);
    }


    public List<ConferenceMsg> queryBeAdmiredConferenceByUidService(PraiseAndCollectRequestParams params){
        HashMap<String,Object> map = new HashMap<>();
        PraiseAndCollectRequestParams.QueryParams queryParams = null;
        ArrayList<ConferenceMsg> list = new ArrayList<>();


        try {
            queryParams = PraiseAndCollectRequestParams.parseToQueryParams(params);
        } catch (ParseException e) {
            e.printStackTrace();
            return list;
        }

        map.put("startTime", queryParams.startTime);
        map.put("endTime", queryParams.endTime);
        map.put("skip", queryParams.skip);
        map.put("pageNumber", queryParams.perPageNumber);
        map.put("uid", queryParams.uid);

        List<ConferenceInfo> infos = dao.queryBeAdmiredConferenceByUid(map);

        infos.forEach(e -> list.add(ConferenceMsg.convert(e)));

        return list;

    }

    public Integer queryUserHadAdmiredConferenceCountService(PraiseAndCollectRequestParams requestParam){
        HashMap<String,Object> map = new HashMap<>();
        PraiseAndCollectRequestParams.QueryParams queryParams = null;
        try {
            queryParams = PraiseAndCollectRequestParams.parseToQueryParams(requestParam);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        map.put("uid", queryParams.uid);
        map.put("startTime", queryParams.startTime);
        map.put("endTime", queryParams.endTime);

        return dao.queryUserHadAdmiredConferenceCount(map);
    }


    public Integer queryBeAdmiredConferenceCount(Integer cid){
        HashMap<String, Object> map = new HashMap<>();
        map.put("cid", cid);
        return dao.queryTheNumberOfBeAdmiredConferenceByCid(map);
    }


    public boolean checkIsSaveBePraisedConference(GivenPraiseBean msg){
        HashMap<String, Object> param = new HashMap<>();

        param.put("uid", msg.getUid());
        param.put("cid", msg.getConferenceid());
        Integer count = dao.selectConferenceIsBePraised(param);
        return count != 0;
    }

}
