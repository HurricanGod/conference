package cn.hurrican.beans;

import java.util.Date;

/**
 * Created by NewObject on 2017/10/31.
 */
public class FeedbackSuggestion {

    private Integer sid;
    private Integer uid;
    private String description;
    private String email;
    private Date submitTime = new Date();

    public FeedbackSuggestion(Integer uid, String description, String email) {
        this.uid = uid;
        this.description = description;
        this.email = email;
    }

    public FeedbackSuggestion(String description, String email) {
        this.description = description;
        this.email = email;
    }

    public FeedbackSuggestion() {
    }

    @Override
    public String toString() {
        return "FeedbackSuggestion{" +
                "sid=" + sid +
                ", uid=" + uid +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }
}
