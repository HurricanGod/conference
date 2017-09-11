package dao;

import testbeans.Course;

/**
 * Created by NewObject on 2017/8/16.
 */


public interface ICourseDao {
    Course queryAllCpnoInfo(Integer cno);
    Course queryAllCourseBySpecifiedCno(Integer cno);
    Course queryDirectAdvancesCourse(Integer cno);
}
