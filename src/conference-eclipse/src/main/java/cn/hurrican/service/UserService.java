package cn.hurrican.service;

import cn.hurrican.beans.User;
import cn.hurrican.dao.IUserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NewObject on 2017/10/30.
 */

@Service(value = "userService")
public class UserService {

    @Resource(name = "IUserDao")
    private IUserDao dao;

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

}
