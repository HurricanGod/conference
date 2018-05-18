package cn.hurrican.constant;

import lombok.Data;

@Data
public class AppConfig {

    private String appid;
    private String appSecret;

    private Boolean needCheckUrl;

    private Integer topNumber = 10;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public Boolean getNeedCheckUrl() {
        return needCheckUrl;
    }

    public void setNeedCheckUrl(Boolean needCheckUrl) {
        this.needCheckUrl = needCheckUrl;
    }

    public Integer getTopNumber() {
        return topNumber;
    }

    public void setTopNumber(Integer topNumber) {
        this.topNumber = topNumber;
    }
}
