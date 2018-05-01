package cn.hurrican.cache;

import cn.hurrican.redis.RedisExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConferenceTypeCache {

    private static final String CONFERENCE_TYPE_HASH_KEY = "conference_type_hash_key";

    @Autowired
    private RedisExecutor executor;

    /**
     * 缓存系统里所有的会议类别
     * @param types
     */
    public void cacheSystemConferenceTypes(Map<Integer, String> types){
        executor.doInPipeline(pipeline -> {
            types.forEach((k,v)-> pipeline.hset(CONFERENCE_TYPE_HASH_KEY, k.toString(), v));
            pipeline.sync();
        });
    }

    /**
     *
     * @return
     */
    public Map<Integer,String> querySystemConferenceTypes(){
        return executor.doInRedis(instance -> {
            Map<String, String> map = instance.hgetAll(CONFERENCE_TYPE_HASH_KEY);
            HashMap<Integer, String> types = new HashMap<>();
            if(map != null){
                map.forEach((k,v) -> types.put(Integer.valueOf(k), v));
            }
            return types;
        });
    }
}
