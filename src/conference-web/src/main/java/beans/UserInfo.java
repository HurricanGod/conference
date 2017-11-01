package beans;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by NewObject on 2017/8/10.
 */
public class UserInfo implements Serializable{
    private Long id;
    private String username;
    private String email;
    private String pwd;
    private Set<WorkInfo> workexperience;

    public UserInfo(String username, String email, String pwd) {
        this.username = username;
        this.email = email;
        this.pwd = pwd;
    }

    public UserInfo()
    {
        this.id = -1L;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public Set<WorkInfo> getWorkexperience() {
        return workexperience;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public void setWorkexperience(Set<WorkInfo> workexperience) {
        this.workexperience = workexperience;
    }

    @Override
    public String toString() {
        return new StringBuffer().append("id:\t").append(this.id)
                .append("\nusername:\t").append(this.username).append("\n").toString();
    }
}
