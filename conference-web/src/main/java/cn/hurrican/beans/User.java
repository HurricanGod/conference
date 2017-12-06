package cn.hurrican.beans;

import java.io.Serializable;

/**
 * Created by NewObject on 2017/8/10.
 */
public class User implements Serializable{
    private Integer uid;
    private String username;
    private String userpwd;
    private Integer userrole;
    private String email;

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
}
