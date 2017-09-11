package dao;

import testbeans.Student;

/**
 * Created by NewObject on 2017/8/16.
 */
public interface IStudentDao {
    Student queryStudentAndCourseById(Integer id);
    Student queryStudentScoreById(Integer id);
}
