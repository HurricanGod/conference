package dao;

import beans.BasicInfo;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisSessionManage;

import java.util.List;

/**
 * Created by NewObject on 2017/8/13.
 */
public class BasicInfoDaoImp implements IBasicInfoDao {
    @Override
    public BasicInfo queryById(Integer id) {
        BasicInfo basicInfo = null;
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        basicInfo = session.selectOne("basicinfo.queryById", id);
        return basicInfo;
    }

    @Override
    public BasicInfo queryByIdAndMapper(Integer id) {
        BasicInfo basicInfo = null;
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        basicInfo = session.selectOne("basicinfo.queryByIdAndMapper", id);
        return basicInfo;
    }

    @Override
    public List<BasicInfo> queryByLike(String key) {
        return null;
    }

    @Override
    public List<BasicInfo> queryAll() {
        return null;
    }

    @Override
    public void addBasicInfo(BasicInfo bf) {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        session.insert("addBasicInfo", bf);
        session.close();
    }
}
