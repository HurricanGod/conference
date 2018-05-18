package cn.hurrican.cache;

import cn.hurrican.beans.User;
import cn.hurrican.constant.AppConstant;
import cn.hurrican.redis.RedisExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserCache {

    @Autowired
    private RedisExecutor executor;

    private static final String VERIFICATION_CODE_PREFIX_KEY = "conference_verify_code_uid:";

    /** 验证码默认保存的时间，单位：秒 **/
    private static final int VERIFY_CODE_DEFAULT_EXPIRE_SECONDS = 60 * 5;

    /** 用户个人设置缓存Hash Key前缀 **/
    private static final String USER_SETTING_HASH_PREFIX_KEY = "conference_user_setting_uid:";


    /**
     * 缓存邮箱验证码
     * @param code
     * @param uid
     */
    public void expireEmailVerifyCode(String code, String uid){
        executor.doInRedis(instance -> {
            String key = VERIFICATION_CODE_PREFIX_KEY + uid;
            instance.setex(key, VERIFY_CODE_DEFAULT_EXPIRE_SECONDS, code);
        });
    }

    /**
     * 获取邮箱验证码
     * @param uid
     * @return 验证码过期返回null
     */
    public String getEmailVerifyCode(Integer uid){
        return executor.doInRedis(instance -> {
            return instance.get(VERIFICATION_CODE_PREFIX_KEY + uid);
        });
    }

    public void cacheUserSetting(User user){
        executor.doInRedis(instance -> {
            String key = USER_SETTING_HASH_PREFIX_KEY + user.getUid();
            if(user.getEmail() != null){
                instance.hset(key, "email", user.getEmail());
            }
            if( user.getFollowType() != null){
                instance.hset(key, "followType", user.getFollowType());
            }
            if(user.getExtendJson() != null){
                instance.hset(key, "extendJson", user.getExtendJson());
            }
            if(user.getUserrole() != null){
                instance.hset(key, "role", user.getUserrole().toString());
            }
        });
    }

    public User queryUserSetting(Integer uid){
        return executor.doInRedis(instance -> {
            String key = USER_SETTING_HASH_PREFIX_KEY + uid;
            if(instance.exists(key)){
                User user = new User();
                user.setUid(uid);
                user.setUserrole(Optional.ofNullable(instance.hget(key, "role"))
                        .map(Integer::valueOf).orElse(AppConstant.APPLET_USER_TYPE));
                user.setEmail(instance.hget(key, "email"));
                user.setFollowType(instance.hget(key, "followType"));
                user.setExtendJson(instance.hget(key, "extendJson"));
                return user;
            }
            return null;
        });
    }


    public void deleteUserSettingInCache(Integer uid){
        executor.doInRedis(instance -> {
            instance.del(USER_SETTING_HASH_PREFIX_KEY + uid);
        });
    }

}
