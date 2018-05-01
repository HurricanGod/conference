package cn.hurrican.service;

import cn.hurrican.beans.ConferenceType;
import cn.hurrican.cache.ConferenceTypeCache;
import cn.hurrican.dao.IConferenceTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConferenceTypeService {

    @Autowired
    private IConferenceTypeDao conferenceTypeDao;

    @Autowired
    private ConferenceTypeCache conferenceTypeCache;


    /**
     * 查询会议所有归类
     * @return Map, key → 类别ID， value → 类别名称
     */
    public Map<Integer, String> queryAllConferenceType(){
        Map<Integer, String> types = conferenceTypeCache.querySystemConferenceTypes();
        if(types != null && types.size() > 0){
            return types;
        }
        List<ConferenceType> conferenceTypes = conferenceTypeDao.queryAllConferenceType();
        HashMap<Integer, String> map = new HashMap<>();
        conferenceTypes.forEach(ele -> map.put(ele.getTypeId(), ele.getTypeName()));
        conferenceTypeCache.cacheSystemConferenceTypes(map);
        return map;
    }
}
