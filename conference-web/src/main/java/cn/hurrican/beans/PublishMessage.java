package cn.hurrican.beans;

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
    private String verificationCode;


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
