package dao;

import beans.WorkInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by NewObject on 2017/8/14.
 */
public interface IWorkInfoDao {
    void addWorkInfo(WorkInfo info);
    List<WorkInfo> queryWorkInfoByManyConditions(Map<String,Object> map);
    List<WorkInfo> queryWorkInfoByNumberIndex(Integer userIdRange, String prefixPosition);
}
