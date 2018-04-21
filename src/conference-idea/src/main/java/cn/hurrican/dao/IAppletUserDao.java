package cn.hurrican.dao;

import cn.hurrican.beans.AppletUser;

public interface IAppletUserDao {

    void saveAppletUser(AppletUser appletUser);

    void updateAppletUserUidByOpenid(String openid, Integer uid);
}
