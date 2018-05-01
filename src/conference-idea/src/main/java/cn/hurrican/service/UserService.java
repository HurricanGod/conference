package cn.hurrican.service;

import cn.hurrican.beans.User;
import cn.hurrican.cache.UserCache;
import cn.hurrican.dao.IUserDao;
import cn.hurrican.exception.NecessaryParameterNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by NewObject on 2017/10/30.
 */

@Service(value = "userService")
public class UserService {

    @Resource(name = "IUserDao")
    private IUserDao dao;

    @Autowired
    private UserCache userCache;

    public Integer insertOneUserAndReturnUserId(User user){
        if (user.getUserpwd() != null && !user.getUserpwd().equals("")) {
            user.setUserrole(0);
        } else {
            user.setUserrole(1);
        }
        dao.insertOneUser(user);
        return user.getUid() != null ? user.getUid() : -1;
    }

    public List<User> queryAllUserService(){
        return dao.queryAllUser();
    }


    public boolean queryAccountAndPasswordIsMatch(String name, String pwd){
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", name);
        map.put("pwd", pwd);
        map.put("role", 0);

        Integer count = dao.queryUserByNameAndPassword(map);
        return count==1;
    }

    public User queryUserSetting(Integer uid, Integer role){
        User userSetting = userCache.queryUserSetting(uid);
        if(userSetting != null){
            return userSetting;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("role", role);
        List<User> users = dao.selectUserSetting(map);
        User user = users != null && users.size() > 0 ? users.get(0) : null;
        if(user != null){
            user.setUid(uid);
            userCache.cacheUserSetting(user);
        }
        return user;
    }

    public void updateUserSetting(User user){
        if(user.getUid() == null){
            throw new NecessaryParameterNullException("更新个人设置时没有uid参数为null")
                    .put("msg", "更新个人设置时没有uid参数为null");
        }
        dao.updateUserSetting(user);
        userCache.cacheUserSetting(user);
    }

    public List<User> queryUserByRole(Integer roleType){
        return dao.queryUserByRole(roleType);
    }
}
