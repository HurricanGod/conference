package cn.hurrican.service;


import cn.hurrican.beans.AppletUser;
import cn.hurrican.beans.User;
import cn.hurrican.constant.AppConstant;
import cn.hurrican.dao.IAppletUserDao;
import cn.hurrican.dao.IUserDao;
import cn.hurrican.redis.RedisExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Response;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppletTokenService {

    /** 小程序APPID Redis key**/
    private static final String APPLET_APPID_KEY = "appid";

    /** 小程序 appsecret Redis key**/
    private static final String APPLET_SECRET_KEY = "app_secret";

    /** hash 结构的key，存放 openid(value) → uid(key) **/
    private static final String OPENID_MAP_UID_KEY = "hash_uid:openid";

    /** 小程序端返回的 session_key 默认过期时间 **/
    private static final int SESSION_KEY_EXPIRE_TIME = 60 * 90;

    private static final String THIRD_SESSION_PREFIX_KEY = "session_3rd_uid:";

    @Autowired
    private RedisExecutor redisExecutor;

    @Autowired
    private IAppletUserDao appletUserDao;

    @Autowired
    private IUserDao userDao;



    private void setKey(String key, String value){
        redisExecutor.doInRedis(instance -> {
            instance.set(key, value);
        });
    }

    public void setAppid(String appid){
        setKey(APPLET_APPID_KEY, appid);
    }

    public void setAppSecret(String secret){
        setKey(APPLET_SECRET_KEY, secret);
    }


    /**
     *  获取 appid 和 appsecret
     * @return list.get(0) → appid; list.get(1) → appsecret
     */
    public List<String> getAppToken(){
        ArrayList<String> list = new ArrayList<>();
        redisExecutor.doInPipeline(pipeline -> {
            Response<String> response1 = pipeline.get(APPLET_APPID_KEY);
            Response<String> response2 = pipeline.get(APPLET_SECRET_KEY);
            pipeline.sync();
            list.add(response1.get());
            list.add(response2.get());
        });
        return list;
    }



    /**
     * 保存微信获取的小程序用户openid
     * @param appletUser 必要字段 nickname、sessionKey、openid 不允许为 null
     * @return 小程序用户唯一id
     */
    public Integer saveOpenId(AppletUser appletUser){
        User user = new User();
        user.setUsername(appletUser.getNickname());
        user.setUserrole(AppConstant.APPLET_USER_TYPE);
        appletUserDao.saveAppletUser(appletUser);
        userDao.insertOneUser(user);

        Integer uid = user.getUid();
        String openid = appletUser.getOpenid();
        appletUserDao.updateAppletUserUidByOpenid(openid, uid);
        redisExecutor.doInRedis(instance -> {
            // 缓存 session_key 5400s
            instance.setex(THIRD_SESSION_PREFIX_KEY + uid.toString(), SESSION_KEY_EXPIRE_TIME, appletUser.getSessionKey());
            // hash 里保存 uid 到 openid 的映射
            instance.hset(OPENID_MAP_UID_KEY, openid, uid.toString());
        });
        return uid;
    }

    public void setExpireSessionKey(Integer uid, String sessionKey){
        redisExecutor.doInRedis(instance -> {
            instance.setex(THIRD_SESSION_PREFIX_KEY + uid.toString(), SESSION_KEY_EXPIRE_TIME, sessionKey);
        });
    }

    public Boolean sessionIsExpired(Integer uid){
        return redisExecutor.doInRedis(instance -> instance.get(THIRD_SESSION_PREFIX_KEY + uid) != null);
    }

    /**
     * 判断是否保存过小程序用户的openid
     * @param openid 小程序用户openid
     * @return uid or null (如果未保存过openid则返回null)
     */
    public String existUid(String openid){
        return redisExecutor.doInRedis(instance -> {
            return instance.hget(OPENID_MAP_UID_KEY, openid);
        });
    }
}
