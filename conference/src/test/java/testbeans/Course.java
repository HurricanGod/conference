package testbeans;

import java.util.Set;

/**
 * Created by NewObject on 2017/8/15.
 */
public class Course {
    private Integer cno;            //课程号
    private String cname;           //课程名
    private Double credit;          //学分
    private Set<Course> cpno;      //先修课程号
    private Set<Student> students;  //选修该课程的学生
    private Course directadvancedcourse;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("cno:\t").append(cno)
                .append("\ncname:\t").append(cname)
                .append("\ncredit:\t").append(credit).append("\n");
        return builder.toString();
    }

    public Course(String cname, Double credit) {
        this.cname = cname;
        this.credit = credit;
    }

    public static void visitAllCpno(Set<Course> courses){
        if (courses != null && courses.size() > 0) {
            for (Course element : courses) {
                System.out.println(element.toString());
                visitAllCpno(element.getCpno());
            }
        }
    }

    public Course() {
        this.cno = -1;
    }

    public Integer getCno() {
        return cno;
    }

    public String getCname() {
        return cname;
    }

    public Double getCredit() {
        return credit;
    }

    public Set<Course> getCpno() {
        return cpno;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public Course getDirectadvancedcourse() {
        return directadvancedcourse;
    }

    public void setDirectadvancedcourse(Course directadvancedcourse) {
        this.directadvancedcourse = directadvancedcourse;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public void setCpno(Set<Course> cpno) {
        this.cpno = cpno;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
