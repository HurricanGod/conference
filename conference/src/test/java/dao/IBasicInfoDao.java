package dao;

import beans.BasicInfo;

import java.util.List;

/**
 * Created by NewObject on 2017/8/13.
 */
public interface IBasicInfoDao {
    BasicInfo queryById(Integer id);
    BasicInfo queryByIdAndMapper(Integer id);
    List<BasicInfo> queryByLike(String key);
    List<BasicInfo> queryAll();
    void addBasicInfo(BasicInfo bf);
}
