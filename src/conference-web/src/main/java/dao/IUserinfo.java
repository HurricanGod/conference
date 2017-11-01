package dao;

import beans.UserInfo;

import java.util.Map;

/**
 * Created by NewObject on 2017/8/15.
 */
public interface IUserinfo {
    UserInfo queryWorkExperienceByUserId(Map<String,Object> map);
    UserInfo queryWorkExperienceByUserIdSingleTable(Map<String,Object> map);
}
