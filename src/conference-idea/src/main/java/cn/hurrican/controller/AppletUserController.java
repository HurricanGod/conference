package cn.hurrican.controller;


import cn.hurrican.beans.AppletUser;
import cn.hurrican.constant.AppConfig;
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


//    private static Logger logger = LogManager.getLogger(AppletUserController.class);

    @Autowired
    private AppletTokenService tokenService;

    @Autowired
    private AppConfig appConfig;

    @RequestMapping(value = "/weixinLogin.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage weixinLogin(String code, HttpServletRequest request){
        ResMessage resMessage = ResMessage.creator();
        String nickname = request.getParameter("nickname");
        String headimg = request.getParameter("headimg");

        Map<String, String> params  = new HashMap<>();
        params.put("appid", appConfig.getAppid());
        params.put("secret", appConfig.getAppSecret());
        params.put("js_code", code);
        params.put("grant_type", GRANT_TYPE);
        ResMessage wxCallResult = null;

        try{
            wxCallResult = HttpClientUtils.sendHttpsGetRequest(WX_OPENID_API, params);
        }catch(Exception e){
            return ResMessage.creator().retCodeEqual(BusinessCode.ServerError.getCode())
                    .logIs(e.getMessage());
        }
//        logger.info("wechat call api result is：\n{}", wxCallResult.toString());

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

    @RequestMapping(value = "/checkSession.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage checkSession(Integer uid){
        Boolean sessionIsExpired = tokenService.sessionIsExpired(uid);
        return ResMessage.creator().retCodeEqual(BusinessCode.Ok.getCode())
                .put("sessionIsExpired", sessionIsExpired);
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
        String session_key = jsonObject.getString("session_key");
        if(existUid == null){
            AppletUser appletUser = AppletUser.build().openidEqual(openid)
                    .nicknameEqual(nickname).headimgEqual(headimg)
                    .sessionKeyEqual(session_key);
            uid = tokenService.saveOpenId(appletUser);
        }else{
            uid = Integer.parseInt(existUid);
            tokenService.setExpireSessionKey(uid, session_key);
        }
        request.getSession().setAttribute("uid", uid.toString());
        resMessage.retCodeEqual(BusinessCode.Ok.getCode()).put("uid", uid).msg("成功保存用户openid并返回小程序在系统中的唯一标志uid");
    }

}
