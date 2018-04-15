package cn.hurrican.beans;

/**
 * Created by NewObject on 2017/10/30.
 */
public class GivenPraiseBean {

    private Integer id;
    private Integer conferenceid;
    private Integer uid;

    public GivenPraiseBean(Integer conferenceid, Integer uid) {
        this.conferenceid = conferenceid;
        this.uid = uid;
    }

    public GivenPraiseBean() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setConferenceid(Integer conferenceid) {
        this.conferenceid = conferenceid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getConferenceid() {
        return conferenceid;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUid() {
        return uid;
    }
}
