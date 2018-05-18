package cn.hurrican.controller;

import cn.hurrican.beans.PublishMessage;
import cn.hurrican.beans.User;
import cn.hurrican.cache.CommonConfigCache;
import cn.hurrican.constant.AppConstant;
import cn.hurrican.constant.AppSwitchKey;
import cn.hurrican.constant.BusinessCode;
import cn.hurrican.constant.QQEmailConfig;
import cn.hurrican.dtl.ResMessage;
import cn.hurrican.service.ConferencePublishService;
import cn.hurrican.service.ConferenceTypeService;
import cn.hurrican.service.UserService;
import cn.hurrican.utils.QQEmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/conference/common")
public class CommonController {

    private static final String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    private static Pattern pattern = Pattern.compile(regex);

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
}
