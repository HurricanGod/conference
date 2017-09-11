package services;

import beans.SecurityGuarantee;
import beans.UserInfo;
import dao.ISecurityGuaranteeDao;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import utils.MybatisSessionManage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by NewObject on 2017/8/15.
 */

public class TestSecurityGuaranteeDao implements Serializable{
    private ISecurityGuaranteeDao dao;

    @Before
    public void startTest() {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        dao = session.getMapper(ISecurityGuaranteeDao.class);
    }

    @Test
    public void querySecurityGuaranteeBySingleTest() {
        Integer id = 2;
        SecurityGuarantee sg = dao.querySecurityGuaranteeBySingle(id);
        System.out.println(sg.getQuestion());
        if (sg != null) {
            System.out.println(sg.toString());
        }
    }


    @Test
    public void querySecurityGuaranteeByJoinTest() {
        Map<String,Object> map = new HashMap<>();
        map.put("sid", 2);
        SecurityGuarantee sg = dao.querySecurityGuaranteeByJoin(map);
        if (sg != null) {
            System.out.println(sg.toString());
        }
    }



    @Test
    public void addSecurityGuaranteeTest() {
        SecurityGuarantee sg = new SecurityGuarantee();
        UserInfo user = new UserInfo();
        user.setId(3l);
        sg.setQuestion("volatile关键字能不能保证线程安全？");
        sg.setAnswer("不能");
        sg.setUser(user);
        dao.addSecurityGuarantee(sg);
    }


}
