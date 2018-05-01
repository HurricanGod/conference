package cn.hurrican.cache;

import cn.hurrican.redis.RedisExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommonConfigCache {

    private static final String Email_Token = "email_token";

    /** 发送邮件功能两次调用之间需要间隔的最小时间 **/
    private static final int Min_Send_Email_Interval = 30;

    private static final String Min_Send_Email_Interval_Prefix_Key = "uid:send:email:interval:";

    /** 会议网站各种开关 **/
    private static final String APP_HASH_CONFIG_KEY = "conference:app:config:switch:hash";

    @Autowired
    private RedisExecutor redis;

    public void setSwitchByName(String switchName, Boolean value){
        redis.doInRedis(instance -> {
            instance.hset(APP_HASH_CONFIG_KEY, switchName, value.toString());
        });
    }

    /**
     * 获取开关的值
     * @param switchName
     * @return hash存储结构中没有 switchName 的key时返回false，或者switchName对应的值为false时返回false
     */
    public Boolean getSwitchValueByName(String switchName){
        return redis.doInRedis(instance -> {
            String switchStatus = instance.hget(APP_HASH_CONFIG_KEY, switchName);
            return Optional.ofNullable(switchStatus).map(Boolean::valueOf).orElse(false);
        });
    }

    public String getEmailServerToken(){
        return redis.doInRedis(instance -> {
            return instance.get(Email_Token);
        });
    }

    public void setLatestSendEmailTime(String uid){
        redis.doInRedis(instance -> {
            instance.setex(Min_Send_Email_Interval_Prefix_Key + uid, Min_Send_Email_Interval, uid);
        });
    }

    public Boolean enableSendEmail(String uid){
        return redis.doInRedis(instance -> instance.get(uid) != null);
    }

}
