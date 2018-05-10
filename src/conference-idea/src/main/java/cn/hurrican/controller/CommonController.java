package cn.hurrican.controller;

import cn.hurrican.beans.PublishMessage;
import cn.hurrican.beans.User;
import cn.hurrican.cache.CommonConfigCache;
import cn.hurrican.constant.*;
import cn.hurrican.dtl.ResMessage;
import cn.hurrican.service.ConferencePublishService;
import cn.hurrican.service.ConferenceTypeService;
import cn.hurrican.service.UserService;
import cn.hurrican.utils.HttpClientUtils;
import cn.hurrican.utils.HttpToolUtils;
import cn.hurrican.utils.QQEmailUtils;
import cn.hurrican.utils.StringUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/conference/common")
public class CommonController {

    /** 邮箱校验正则表达式 **/
    private static final String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    private static Pattern pattern = Pattern.compile(regex);

    private static final  String qrcodeFileName = "qrcode.png";

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private QQEmailConfig qqEmailConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private CommonConfigCache commonConfigCache;

    @Autowired
    private ConferenceTypeService  conferenceTypeService;

    @Autowired
    private ConferencePublishService conferencePublishService;

    @RequestMapping(value = "/sendEmail.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage sendEmail(String subject, String content, String receiveUsers, HttpServletRequest request){
        ResMessage resMessage = new ResMessage();
        String uid = (String) request.getSession().getAttribute("uid");
        Boolean enableSendOnWeb = commonConfigCache.getSwitchValueByName(AppSwitchKey.WEB_SEND_EMAIL);
        if(!enableSendOnWeb && (uid == null || !commonConfigCache.enableSendEmail(uid))){
            return resMessage.msg(uid == null ? "web用户不允许调用此接口发邮件" : "发送邮件操作太频繁，请稍后重试！")
                    .put("successSendEmail", false);
        }
        String[] arr = receiveUsers.split(",");
        for (int i = 0; i < arr.length; i++) {
            Matcher matcher = pattern.matcher(arr[i]);
            if(!matcher.matches()){
                return resMessage.msg("邮箱格式错误").put("successSendEmail", false);
            }
        }
        try{
            QQEmailUtils.sendEmail(subject, qqEmailConfig, content, receiveUsers, null);
        }catch(Exception e){
            return resMessage.logIs(e.getMessage()).put("successSendEmail", false)
                    .retCodeEqual(BusinessCode.ServerError.getCode());
        }
        commonConfigCache.setLatestSendEmailTime(uid);
        return resMessage.put("successSendEmail", true)
                .retCodeEqual(BusinessCode.Ok.getCode());
    }


    @RequestMapping(value = "/init.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage init(Integer uid){
        ResMessage resMessage = new ResMessage();
        if(uid == null){
            return resMessage.msg("uid不允许为null").retCodeEqual(BusinessCode.ParametersIsNullError.getCode());
        }
        User userSetting = userService.queryUserSetting(uid, AppConstant.APPLET_USER_TYPE);
        resMessage.put("userSetting", userSetting);

        Map<Integer, String> conferenceTypes = conferenceTypeService.queryAllConferenceType();
        resMessage.put("conferenceType", conferenceTypes.values());

        List<PublishMessage> scholarRecommendConference = conferencePublishService.
                queryPublishConference(true, null, 200, null, null);
        resMessage.put("scholarRecommendConference", scholarRecommendConference);

        return resMessage;
    }


    @RequestMapping(value = "/generateAppletQrcode.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage generateAppletQrcode(HttpServletRequest request){
        ResMessage resMessage = new ResMessage();
        String accessToken = commonConfigCache.getAccessToken();
        if(StringUtil.isNullOrEmpty(accessToken)){
            HashMap<String, String> params = new HashMap<>();
            params.put("grant_type", AppConstant.GRANT_TYPE);
            params.put("appid", appConfig.getAppid());
            params.put("secret", appConfig.getAppSecret());
            ResMessage response = HttpClientUtils.sendHttpsGetRequest(AppConstant.ACCESS_TOKEN_API_URL, params);
            JSONObject jsonObject = JSONObject.fromObject(response.getMessage());

            if(jsonObject.containsKey("access_token")){
                accessToken = jsonObject.getString("access_token");
                commonConfigCache.cacheAccessToken(accessToken);
            }else if(jsonObject.containsKey("errmsg")){
                return response.retCodeEqual(BusinessCode.CallApiFail.getCode());
            }
        }
        return saveQrcodeToLocal(request, resMessage, accessToken);
    }

    private ResMessage saveQrcodeToLocal(HttpServletRequest request, ResMessage resMessage, String accessToken) {
        Map<String,Object> line_color = new HashMap<>();
        line_color.put("r", 0);
        line_color.put("g", 0);
        line_color.put("b", 0);
        String webRealPath = request.getSession().getServletContext().getRealPath("/");
        String picPath = null;
        if(webRealPath.endsWith(File.separator)){
            picPath  = webRealPath + "picture" + File.separator;
        }else{
            picPath  = webRealPath + File.separator + "picture" + File.separator;
        }
        File file = new File(picPath);
        if(!file.exists()|| !file.isDirectory()){
            System.out.println("file.mkdir() = " + file.mkdir());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.element("width", appConfig.getWidth());
        jsonObject.element("auto_color", appConfig.getAuto_color());
        jsonObject.element("path", appConfig.getAppletHomePage());
        jsonObject.element("is_hyaline", appConfig.getIs_hyaline());
        jsonObject.element("line_color", JSONObject.fromObject(line_color));

        URIBuilder builder = null;
        FileOutputStream outputStream = null;
        try {
            builder = new URIBuilder("https://api.weixin.qq.com/wxa/getwxacode");
            builder.setParameter("access_token", accessToken);
            URI uri = builder.build();
            byte[] data = HttpToolUtils.simplePostInvokeWithByte(uri,jsonObject, "utf-8");
            File pic = new File(picPath + qrcodeFileName);
            if (!pic.exists()){
                System.out.println("file.createNewFile() = " + pic.createNewFile());
            }else{
                System.out.println("pic.delete() = " + pic.delete());
                System.out.println("file.createNewFile() = " + pic.createNewFile());
            }
            outputStream = new FileOutputStream(pic);
            outputStream.write(data);
            outputStream.flush();
        } catch (URISyntaxException | IOException e1) {
            e1.printStackTrace();
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resMessage.retCodeEqual(BusinessCode.ServerError.getCode()).msg(e1.getMessage());
        }
        return resMessage;
    }


    @RequestMapping(value = "/getAppletQrcode.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage getQrcodePath(HttpServletRequest request){
        ResMessage resMessage = new ResMessage();
        String webRealPath = request.getSession().getServletContext().getRealPath("/");
        String picturePath = webRealPath + File.separator + "picture" + File.separator;
        String qrcodeFilePathName = picturePath + qrcodeFileName;
        File file = new File(qrcodeFilePathName);
        File dir = new File(picturePath);
        if(dir.exists() && dir.isDirectory() && file.exists()){
           resMessage.msg("使用已生成的二维码").put("qrcode", "../picture/" + qrcodeFileName);
        }else{
            ResMessage response = generateAppletQrcode(request);
            if(resMessage.getRetCode() != 0){
                return resMessage.retCodeEqual(response.getRetCode()).msg(response.getMessage())
                        .logIs(response.getLog()).put("qrcode", "#");
            }
            resMessage.msg("成功重新生成小程序二维码");
        }
        return resMessage.put("qrcode", "../picture/" + qrcodeFileName);
    }



}
