package cn.hurrican.beans;

import lombok.Data;

@Data
public class AppletUser {

    public static AppletUser build(){
        return new AppletUser();
    }

    public AppletUser openidEqual(String openid){
        this.openid = openid;
        return this;
    }

    public AppletUser headimgEqual(String headimg){
        this.headimg = headimg;
        return this;
    }

    public AppletUser nicknameEqual(String nickname){
        this.nickname = nickname;
        return this;
    }

    public AppletUser sessionKeyEqual(String sessionKey){
        this.sessionKey = sessionKey;
        return this;
    }

    private Integer appletId;

    private String openid;

    private String sessionKey;

    private String nickname;

    private String headimg;

    private Integer uid;

    private String extendJson;

    private String extendJsonArray;

}
