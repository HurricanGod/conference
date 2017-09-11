package dao;

import beans.UserInfo;

import java.util.List;

/**
 * Created by NewObject on 2017/8/11.
 */
public interface IUserinfoDao {
    void add(UserInfo user);
    void remove(UserInfo user);
    UserInfo queryById(long id);
    List<UserInfo> queryAll();
    void update(UserInfo user);
    List<UserInfo> queryByField(String fieldVal);
}
