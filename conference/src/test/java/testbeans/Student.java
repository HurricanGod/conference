package testbeans;

import java.util.Set;

/**
 * Created by NewObject on 2017/8/15.
 */
public class Student {
    private Integer sno;            //学号
    private String sname;           //姓名
    private Integer age;            //年龄
    private String department;      //系别
    private String gender;          //性别
    private Set<Course> courses;   //选修的课程
    private Set<Mark> marks;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("sno:\t").append(sno)
                .append("\nsname:\t").append(this.sname)
                .append("\nage:\t").append(this.age)
                .append("\ndepartment:\t").append(this.department)
                .append("\ngender:\t").append(this.gender).append("\n");
        return builder.toString();
    }

    public Student(String sname, Integer age, String department, String gender) {
        this.sname = sname;
        this.age = age;
        this.department = department;
        this.gender = gender;
    }

    public Student(Integer sno) {
        this.sno = sno;
    }

    public Student() {
        this.sno = 0;
        this.gender = "女";
        this.age = 18;
        this.department = "计算机系";
    }

    public Integer getSno() {
        return sno;
    }

    public String getSname() {
        return sname;
    }

    public Integer getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public String getGender() {
        return gender;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Set<Mark> getMarks() {
        return marks;
    }

    public void setMarks(Set<Mark> marks) {
        this.marks = marks;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
