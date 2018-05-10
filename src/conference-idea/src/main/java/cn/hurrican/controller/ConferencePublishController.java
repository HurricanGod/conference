package cn.hurrican.controller;

import cn.hurrican.beans.PublishMessage;
import cn.hurrican.beans.User;
import cn.hurrican.cache.CommonConfigCache;
import cn.hurrican.cache.UserCache;
import cn.hurrican.constant.AppConfig;
import cn.hurrican.constant.AppConstant;
import cn.hurrican.constant.BusinessCode;
import cn.hurrican.constant.QQEmailConfig;
import cn.hurrican.dtl.PublishConferenceQueryParams;
import cn.hurrican.dtl.ResMessage;
import cn.hurrican.service.ConferencePublishService;
import cn.hurrican.service.CrawlerService;
import cn.hurrican.service.JavaEmailSender;
import cn.hurrican.service.UserService;
import cn.hurrican.utils.CrawlerUtils;
import cn.hurrican.utils.QQEmailUtils;
import cn.hurrican.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by NewObject on 2017/11/3.
 */

@Controller
@RequestMapping(value = "/conference/publish")
public class ConferencePublishController {



    @Resource(name = "publishService")
    private ConferencePublishService service;

    @Autowired
    private UserCache userCache;

    @Autowired
    private UserService userService;

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private QQEmailConfig qqEmailConfig;

    @Autowired
    private AppConfig appConfig;
    /**
     * 保存用户发布的会议，用户发布会议时需要先进行邮箱，验证操作为：
     * 用户填写邮箱后点击获取验证码按钮，服务器会向用户提供的邮箱发送
     * 包含验证码的邮件，用户没有填错邮箱便可获取验证码进行下一步操作
     *
     * @param object
     * @return
     */
    @RequestMapping(value = "/savePublishConference.do",
            produces = "application/json;charset=UTF-8",
            method = RequestMethod.POST)
    @ResponseBody
    public Object savePublishConference(@RequestBody PublishMessage object, HttpServletRequest request){
        /** 前端未传递uid的请求为Web端的请求，需要进行验证码校验  **/
        if(object.getUid() == null){
            HttpSession session = request.getSession();
            Object code = session.getAttribute("code");
            if (code == null) {
                return new Serializable() {
                    public boolean isSuccess = false;
                    public String msg = "验证码失效";
                };
            }
            if (!((String)code).equals(object.getVerificationCode())) {
                return new Serializable() {
                    public boolean isSuccess = false;
                    public String msg = "验证码错误";
                };
            }
        }else{
            User user = userService.queryUserSetting(object.getUid(), AppConstant.APPLET_USER_TYPE);
            if(user.getEmail() == null){
                return new Serializable() {
                    public boolean isSuccess = false;
                    public String msg = "请先到个人设置里进行邮箱绑定";
                };
            }else {
                String uri = object.getUri();
                if (appConfig.getNeedCheckUrl() && (uri == null || !RegexUtils.isLegalSimpleUrl(uri)) ){
                    return new Serializable() {
                        public boolean isSuccess = false;
                        public String msg = "请检查会议网址是否正确";
                    };
                }
                object.setEmail(user.getEmail());

                object.setUri(CrawlerUtils.repairUrl(object.getUri()));
            }
        }

        boolean res = service.savePublishConferenceService(object);

        // TODO 发送http请求访问发布者提供的会议网址
        crawlerService.sendRequestObtainMainConferenceContent(object);
        return new Serializable() {public boolean isSuccess = res;};

    }


    @RequestMapping(value = "/checkEmail.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage checkEmail(String email, String uid, HttpServletRequest request){
        Random random = new Random();
        ResMessage resMessage = new ResMessage();
        StringBuilder verificationCode = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            verificationCode.append(random.nextInt(10));
        }
        HttpSession session = request.getSession();

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("请勿回复此邮件，验证码为： ").append(verificationCode);
        emailContent.append(",验证码5分钟内有效，如不是本人操作请检查邮箱有没有被盗！");

        try{
            QQEmailUtils.sendEmail("邮箱验证", qqEmailConfig, emailContent.toString(), email, null);
        }catch(Exception e){
            e.printStackTrace();
            return resMessage.logIs(e.getMessage()).retCodeEqual(BusinessCode.ServerError.getCode())
                    .msg("发送邮件时发生异常");
        }
        session.setAttribute("code", verificationCode.toString());
        session.setMaxInactiveInterval(60 * 3);
        if(uid != null){
            userCache.expireEmailVerifyCode(verificationCode.toString(), uid);
        }
        return resMessage.retCodeEqual(BusinessCode.Ok.getCode()).msg("成功发送验证码到邮箱");
    }

    @RequestMapping(value = "/queryUnCrawledConference.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryUncrawledConference(HttpServletRequest request){
        /**
         * @decription: 查询用户发布的会议中，网址未被爬取解析的
         * @param request
         * @return: java.lang.Object
         */

        String pagestr = request.getParameter("page");
        String perPageNumberStr = request.getParameter("perPageNumber");
        List<PublishMessage> list;

        if (pagestr != null &&  perPageNumberStr != null) {

            list = service.queryUnCrawledUrl(Integer.valueOf(pagestr), Integer.valueOf(perPageNumberStr));

        }else if(perPageNumberStr != null){

            list = service.queryUnCrawledUrl(Integer.valueOf(perPageNumberStr));

        } else {
            list = service.queryUnCrawledUrl();
        }
        return list;
    }

    @RequestMapping(value = "/queryPublishConference.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage queryPublishConferenceByCondition(PublishConferenceQueryParams args){
        ResMessage resMessage = new ResMessage();

        List<PublishMessage> publishConference = service.queryPublishConference(args.passCheck, args.isCrawled,
                args.statusCode, args.offset, args.number);
        resMessage.put("conferenceFromApplet", publishConference);
        return resMessage;
    }


    @RequestMapping(value = "/updatePublishConference.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage updatePublishConferenceSelective(){
        ResMessage resMessage = new ResMessage();
        return resMessage;
    }
}
