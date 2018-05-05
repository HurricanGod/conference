package cn.hurrican.constant;

import lombok.Data;

@Data
public class AppConfig {

    private String appid;
    private String appSecret;

    private Boolean needCheckUrl;

    private Integer topNumber = 10;
}
