package cn.hurrican.dtl;

/**
 * Created by NewObject on 2017/10/5.
 */
public class FavoritiesInfo {

    private String name;
    private Integer userid;
    private String website;

    public FavoritiesInfo(String name, Integer userid, String website) {
        this.name = name;
        this.userid = userid;
        this.website = website;
    }

    public FavoritiesInfo(Integer userid, String website) {
        this.userid = userid;
        this.website = website;
    }

    public FavoritiesInfo() {
    }

    public String getName() {
        return name;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getWebsite() {
        return website;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        String string = "userid:\t" + this.userid+
                "\nname:\t" + this.name+
                "\nwebsite:\t" + this.website;
        return string;
    }
}
