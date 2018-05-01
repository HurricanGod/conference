package cn.hurrican.dtl;

import cn.hurrican.beans.User;

public class UserVo extends User {

    private Integer action;

    private String verificationCode;

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
