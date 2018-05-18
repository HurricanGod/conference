package cn.hurrican.beans;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by NewObject on 2017/8/10.
 */
public class User implements Serializable{
    private Integer uid;
    private String username;
    private String userpwd;
    private Integer userrole;
    private String email;
    private String followType;
    private String extendJson;
    private Boolean acceptNotice;
    private Boolean isAdmin;
    private Date lastUpdateTime = new Date();

    public User uidEqual(Integer uid){
        this.setUid(uid);
        return this;
    }

    public User() {
    }

    public User(String username, Integer userrole) {
        this.username = username;
        this.userrole = userrole;
    }

    public User(int uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", userpwd='" + userpwd + '\'' +
                ", userrole=" + userrole +
                ", email='" + email + '\'' +
                ", followType='" + followType + '\'' +
                ", extendJson='" + extendJson + '\'' +
                ", acceptNotice=" + acceptNotice +
                ", isAdmin=" + isAdmin +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public void setUserrole(Integer userrole) {
        this.userrole = userrole;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Integer getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public Integer getUserrole() {
        return userrole;
    }

    public String getFollowType() {
        return followType;
    }

    public void setFollowType(String followType) {
        this.followType = followType;
    }

    public String getExtendJson() {
        return extendJson;
    }

    public void setExtendJson(String extendJson) {
        this.extendJson = extendJson;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getAcceptNotice() {
        return acceptNotice;
    }

    public void setAcceptNotice(Boolean acceptNotice) {
        this.acceptNotice = acceptNotice;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
