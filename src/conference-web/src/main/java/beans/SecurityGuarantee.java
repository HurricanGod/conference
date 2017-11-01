package beans;

/**
 * Created by NewObject on 2017/8/15.
 */
public class SecurityGuarantee {
    private Integer securityid;
    private String question;
    private String answer;
    private UserInfo user;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("securityGuaranteeId:\t").append(this.securityid)
                .append("\nquestion:\t").append(this.question)
                .append("\nanswer:\t").append(this.answer)
                .append("\nusername:\t").append(this.user.getUsername())
                .append("\nemail:\t").append(this.user.getEmail())
                .append("\n");
        return builder.toString();
    }

    public SecurityGuarantee(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public SecurityGuarantee() {
        this.securityid = 0;
    }

    public Integer getSecurityid() {
        return securityid;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setSecurityid(Integer securityid) {
        this.securityid = securityid;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
