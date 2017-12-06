package cn.hurrican.beans;

/**
 * Created by NewObject on 2017/10/30.
 */
public class ConcernedConference {

    private Integer id;
    private Integer cid;
    private Integer uid;

    public ConcernedConference(Integer cid, Integer uid) {
        this.cid = cid;
        this.uid = uid;
    }

    public ConcernedConference() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
