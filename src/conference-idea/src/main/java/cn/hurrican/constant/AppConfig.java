package cn.hurrican.constant;

import lombok.Data;

/**
 * 小程序应用配置，配置内容可在spring-service里修改
 */
@Data
public class AppConfig {

    private String appid;
    private String appSecret;

    private Boolean needCheckUrl;

    /** 置顶会议条数 **/
    private Integer topNumber = 3;

    /** 小程序首页 **/
    private String appletHomePage;

    /** 小程序二维码宽度 **/
    private Integer width;

    /** 自动配置线条颜色,如果颜色依然是黑色，则说明不建议配置主色调 **/
    private  Boolean auto_color;

    /** 是否需要透明底色， is_hyaline 为true时，生成透明底色的小程序码 **/
    private Boolean is_hyaline;
}
