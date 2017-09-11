package services;

import dao.IStudentDao;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import testbeans.Course;
import testbeans.Mark;
import testbeans.Student;
import utils.MybatisSessionManage;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by NewObject on 2017/8/16.
 */

public class TestManyToManyRelative {
    private IStudentDao dao;
    @Before
    public void startTest() {
        SqlSession session = MybatisSessionManage.getSqlSessionAutoCommit();
        dao = session.getMapper(IStudentDao.class);
    }

    @Test
    public void queryStudentScoreByIdTest() {
        Integer id = 3;
        Student student = dao.queryStudentScoreById(id);
        if (student != null) {
            System.out.println("学生基本信息：");
            System.out.println(student.toString());
            Set<Mark> marks = student.getMarks();
            if (marks != null) {
                System.out.println("学生选修课成绩如下：");
                for (Mark element : marks) {
                    System.out.println(element.toString());
                }
            }
        }
    }



    @Test
    public void queryStudentAndCourseByIdTest() {
        Integer id = 2;
        Student student = dao.queryStudentAndCourseById(id);
        if (student != null) {
            System.out.println("学生基本信息：");
            System.out.println(student.toString());
            Set<Course> courses = student.getCourses();
            if (courses != null) {
                Iterator<Course> iterator = courses.iterator();
                System.out.println("学生选修的课程信息如下：");
                while (iterator.hasNext()) {
                    Course itor = iterator.next();
                    System.out.println(itor.toString());
                }
            }
        }
    }


}
