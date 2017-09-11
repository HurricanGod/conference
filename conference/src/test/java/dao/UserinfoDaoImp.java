package dao;

import beans.UserInfo;
import org.apache.ibatis.session.SqlSession;
import utils.MybatisSessionManage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NewObject on 2017/8/11.
 */
public class UserinfoDaoImp implements IUserinfoDao {

    public void add(UserInfo user) {
        SqlSession session = MybatisSessionManage.getSqlSession();
        session.insert("addUser", user);
        session.commit();
        session.close();
    }

    public void getIdAfterAdd(UserInfo user){
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        session.insert("addUserAndQuery", user);
        session.close();
    }

    public void remove(UserInfo user) {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        session.delete("deleteUser", user);
        session.close();
    }



    @Override
    public void update(UserInfo user) {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        session.update("updateUser", user);
        session.close();
    }

    @Override
    public List<UserInfo> queryByField(String fieldVal) {
        List<UserInfo> list = new ArrayList<>();
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        list = session.selectList("queryByLike", fieldVal);
        return list;
    }


    @Override
    public UserInfo queryById(long id) {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        UserInfo user = session.selectOne("dao.IUserinfo.queryById", id);
        return user;
    }

    @Override
    public List<UserInfo> queryAll() {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        List<UserInfo> list = new ArrayList<>();
        list = session.selectList("queryAll");
        session.close();
        return list;
    }

}
