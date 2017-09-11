package services;

import beans.UserInfo;
import beans.WorkInfo;
import dao.IUserinfo;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import utils.MybatisSessionManage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by NewObject on 2017/8/15.
 */
public class TestIUserinfo {

    @Test
    public void queryWorkExperienceByUserIdTest() {
        /**
         * @decription: 一对多关联关系查询
         * sql语句：
         *      select id, username, email,  work_id,
         *             company, position, duty, user_id
                from account, work_info
                where account.id = ? and user_id = account.id
         */
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        IUserinfo dao = session.getMapper(IUserinfo.class);
        Map<String,Object> map = new HashMap<>();
        map.put("idCondition", 3L);
        UserInfo user = dao.queryWorkExperienceByUserId(map);
        System.out.println(user.toString());
        Set<WorkInfo> set = user.getWorkexperience();
        Iterator<WorkInfo> iterator = set.iterator();
        while (iterator.hasNext()){
            WorkInfo experience = iterator.next();
            System.out.println(experience.toString());
        }
    }


    @Test
    public void queryWorkExperienceByUserIdSingleTableTest() {
        /**
         * @decription: 多表分别单独查询实现一对多关联查询
         *               把一对多关联查询分成两个查询语句，
         *               第1个查询的结果作为第二个查询语句的条件
         * sql语句：
         * select id, username, email from account where id = ?
         * select work_id, company, position, duty, user_id
         * from work_info where user_id = ?
         */
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        IUserinfo dao = session.getMapper(IUserinfo.class);
        Map<String,Object> map = new HashMap<>();
        map.put("idCondition", 3L);
        UserInfo user = dao.queryWorkExperienceByUserIdSingleTable(map);
        System.out.println(user.toString());
        Set<WorkInfo> set = user.getWorkexperience();
        Iterator<WorkInfo> iterator = set.iterator();
        while (iterator.hasNext()){
            WorkInfo experience = iterator.next();
            System.out.println(experience.toString());
        }
    }
}
