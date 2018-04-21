package cn.hurrican.controller;


import cn.hurrican.beans.AppletUser;
import cn.hurrican.constant.BusinessCode;
import cn.hurrican.dtl.ResMessage;
import cn.hurrican.service.AppletTokenService;
import cn.hurrican.utils.HttpClientUtils;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/conference/applet")
public class AppletUserController {

    /** 获取小程序 openid 微信接口 **/
    private static final String WX_OPENID_API = "https://api.weixin.qq.com/sns/jscode2session";

    /**  获取openid需要传递的 grant_type **/
    private static final String GRANT_TYPE = "authorization_code";

    private static Logger logger = LogManager.getLogger(AppletUserController.class);

    @Autowired
    private AppletTokenService tokenService;

    @RequestMapping(value = "/weixinLogin.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage weixinLogin(String code, String nickname, String headimg, HttpServletRequest request){
        ResMessage resMessage = ResMessage.creator();
        List<String> appToken = tokenService.getAppToken();

        Map<String, String> params  = new HashMap<>();
        params.put("appid", appToken.get(0));
        params.put("secret", appToken.get(1));
        params.put("js_code", code);
        params.put("grant_type", GRANT_TYPE);
        ResMessage wxCallResult = HttpClientUtils.sendHttpsGetRequest(WX_OPENID_API, params);
        logger.info("wechat call api result is：\n{}", wxCallResult.toString());

        if(wxCallResult.getRetCode() == 200){
            JSONObject jsonObject = JSONObject.fromObject(wxCallResult.getMessage());
            if(jsonObject.containsKey("openid") && jsonObject.containsKey("session_key")){
                getUid(nickname, headimg, resMessage, jsonObject, request);
            }else if (jsonObject.containsKey("errcode") && jsonObject.containsKey("errmsg")){
                apiCallExceptionHandle(resMessage, jsonObject);
            }
        }else {
            resMessage.retCodeEqual(BusinessCode.NetworkError.getCode()).msg(wxCallResult.getMessage()).logIs(BusinessCode.NetworkError.getMsg());
        }
        return resMessage;
    }

    private void apiCallExceptionHandle(ResMessage resMessage, JSONObject jsonObject) {
        StringBuilder builder = new StringBuilder();
        builder.append("api调用返回的状态码为：   ").append(jsonObject.get("errcode"))
                .append("返回的错误提示为：    ").append(jsonObject.get("errmsg"));
        resMessage.retCodeEqual(BusinessCode.CallApiFail.getCode()).msg(BusinessCode.CallApiFail.getMsg())
                .logIs(builder.toString());
    }

    private void getUid(String nickname, String headimg, ResMessage resMessage,
                        JSONObject jsonObject, HttpServletRequest request) {
        String openid = jsonObject.getString("openid");
        String existUid = tokenService.existUid(openid);
        Integer uid = null;
        if(existUid == null){
            AppletUser appletUser = AppletUser.build().openidEqual(openid)
                    .nicknameEqual(nickname).headimgEqual(headimg)
                    .sessionKeyEqual(jsonObject.getString("session_key"));
            uid = tokenService.saveOpenId(appletUser);
        }else{
            uid = Integer.parseInt(existUid);
        }
        request.getSession().setAttribute("name", uid.toString());
        resMessage.retCodeEqual(BusinessCode.Ok.getCode()).put("uid", uid).msg("成功保存用户openid并返回小程序在系统中的唯一标志uid");
    }


    /**
     *
     * @param appid
     * @param secret
     * @return
     */
    @RequestMapping(value = "/setAppletConfig.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage setAppletConfig(String appid, String secret){
        tokenService.setAppid(appid);
        tokenService.setAppSecret(secret);
        return ResMessage.creator().msg("success");
    }
}
