package cn.hurrican.constant;

public class AppConstant {

    /** 小程序用户类型常量 **/
    public static final int APPLET_USER_TYPE = 2;

    /** 有改绑邮箱的个人设置更新 **/
    public static final Integer UPDATE_EMAIL_ACTION = 100;

    /** 获取access_token 的参数 **/
    public static final String GRANT_TYPE = "client_credential";

    /** 获取 access_token 的微信接口，请求方式: GET，需要的参数：grant_type、appid、secret **/
    public static final String ACCESS_TOKEN_API_URL = "https://api.weixin.qq.com/cgi-bin/token";
}
