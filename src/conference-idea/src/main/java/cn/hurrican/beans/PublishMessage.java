package cn.hurrican.beans;

import java.util.Date;

/**
 * Created by NewObject on 2017/11/3.
 */
public class PublishMessage {
    private Integer id;
    private String uri;
    private String description;
    private String email;
    private Integer iscrawled;
    private String organization;
    /** 邮箱验证码 **/
    private String verificationCode;
    /** 小程序用户唯一标志 **/
    private Integer uid;

    /** 会议详情ID **/
    private Integer cid;

    /** 访问会议网址返回的状态码 **/
    private Integer statusCode;

    /** 会议网址对应的主要内容 **/
    private String mainContent;

    /** 发布的会议是否通过审核 **/
    private Boolean passCheck;

    /** 扩展json字符串 **/
    private String extendJson;

    private Date lastUpdateTime = new Date();

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public PublishMessage() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public Integer getIscrawled() {
        return iscrawled;
    }

    public void setIscrawled(Integer iscrawled) {
        this.iscrawled = iscrawled;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }


    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public Boolean getPassCheck() {
        return passCheck;
    }

    public void setPassCheck(Boolean passCheck) {
        this.passCheck = passCheck;
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

    @Override
    public String toString() {
        return "PublishMessage{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}
