package beans;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by NewObject on 2017/8/14.
 */
public class WorkInfo {
    private Integer work_id;
    private String company;  //公司名称
    private String position; //职位
    private String duty;     //岗位职责
    private Date departure;  //离职时间
    private Long user_id; //关联account表外键


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("work_id:\t").append(this.work_id)
                .append("\ncompany:\t").append(this.company)
                .append("\nposition:\t").append(this.position)
                .append("\ndeparture:\t").append(this.formatDate(this.departure))
                .append("\nduty:\t").append(this.duty)
                .append("\nuser_id:\t").append(this.user_id).append("\n");
        return builder.toString();
    }

    private String formatDate(Date date){
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public WorkInfo(String company, String position,
                    String duty, Date departure, Long user_id) {
        this.company = company;
        this.position = position;
        this.duty = duty;
        this.departure = departure;
        this.user_id = user_id;
    }

    public WorkInfo() {
        this.work_id = -1;
        this.company = "网易";
        this.position = "后台研发工程师";
        this.duty = "核心代码审核与修改";
    }

    public WorkInfo(String company, String position, String duty, Date departure) {
        this.company = company;
        this.position = position;
        this.duty = duty;
        this.departure = departure;
    }

    public WorkInfo(String company, String position, String duty) {
        this.company = company;
        this.position = position;
        this.duty = duty;
    }


    public Integer getWork_id() {
        return work_id;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public String getDuty() {
        return duty;
    }

    public Date getDeparture() {
        return departure;
    }

    public Long getUser_id() {
        return user_id;
    }


    public void setWork_id(Integer work_id) {
        this.work_id = work_id;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
