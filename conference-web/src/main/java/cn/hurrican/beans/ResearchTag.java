package cn.hurrican.beans;

/**
 * Created by NewObject on 2017/10/28.
 */
public class ResearchTag {
    private Integer id;
    private String name;
    private Integer directionId;
    private String directionFieldName;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirectionId(Integer directionId) {
        this.directionId = directionId;
    }

    public void setDirectionFieldName(String directionFieldName) {
        this.directionFieldName = directionFieldName;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDirectionId() {
        return directionId;
    }

    public String getDirectionFieldName() {
        return directionFieldName;
    }
}
