package cn.hurrican.cache;

import cn.hurrican.redis.RedisExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonConfigCache {

    private static final String Email_Token = "email_token";

    @Autowired
    private RedisExecutor redis;

    public String getEmailServerToken(){
        return redis.doInRedis(instance -> {
            return instance.get(Email_Token);
        });
    }

}
