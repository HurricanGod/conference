package services;

import dao.ICourseDao;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import testbeans.Course;
import utils.MybatisSessionManage;

import java.util.Set;

/**
 * Created by NewObject on 2017/8/16.
 */

public class TestICourseDao {
    private ICourseDao dao;

    @Before
    public void startTest() {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        dao = session.getMapper(ICourseDao.class);
    }

    @Test
    public void queryAllCourseBySpecifiedCnoTest() {
        Integer id = 8;
        Course course = dao.queryAllCourseBySpecifiedCno(id);
        if (course != null) {
            System.out.println("待查询课程信息：");
            System.out.println(course.toString());
            System.out.println("以待查询课程为先修课的所有课程信息：");
            Set<Course> set = course.getCpno();
            Course.visitAllCpno(set);
        }
    }


    @Test
    public void queryDirectAdvancesCourseTest() {
        Integer id = 11;
        Course course = dao.queryDirectAdvancesCourse(id);
        System.out.println("待查询课程信息：");
        System.out.println(course.toString());
        Course advanceCourse = course.getDirectadvancedcourse();
        if (advanceCourse != null) {
            System.out.println("待查询课程的直接先行课信息：");
            System.out.println(advanceCourse.toString());
        }
    }




    @Test
    public void queryCpnoNameTest() {
        Integer id = 11;
        Course course = dao.queryAllCpnoInfo(id);
        if (course != null) {
            System.out.println("待查询课程信息：");
            System.out.println(course.toString());
            System.out.println("待查询课程的直接或间接先修课程信息");
            Set<Course> set = course.getCpno();
            Course.visitAllCpno(set);
        }

    }



}
