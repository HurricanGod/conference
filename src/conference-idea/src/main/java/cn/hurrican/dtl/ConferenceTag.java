package cn.hurrican.dtl;

import cn.hurrican.beans.ConferenceInfo;

/**
 * Created by NewObject on 2017/10/6.
 */
public class ConferenceTag {

    private String name;
    private String tag;
    private Integer number;

    public static ConferenceTag convert(ConferenceInfo c){
        ConferenceTag tag = new ConferenceTag();
        tag.setName(c.getName());
        tag.setTag(c.getTag());
        return tag;
    }

    public ConferenceTag(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public ConferenceTag() {
    }

    public ConferenceTag(String tag, Integer number) {
        this.tag = tag;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "ConferenceTag{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
