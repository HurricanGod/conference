package cn.hurrican.controller;

import cn.hurrican.beans.PublishMessage;
import cn.hurrican.cache.CommonConfigCache;
import cn.hurrican.service.ConferencePublishService;
import cn.hurrican.service.JavaEmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * Created by NewObject on 2017/11/3.
 */

@Controller
@RequestMapping(value = "/conference/publish")
public class ConferencePublishController {

    @Resource(name = "publishService")
    private ConferencePublishService service;

    @Autowired
    private CommonConfigCache configCache;


    /**
     * 保存用户发布的会议，用户发布会议时需要先进行邮箱，验证操作为：
     * 用户填写邮箱后点击获取验证码按钮，服务器会向用户提供的邮箱发送
     * 包含验证码的邮件，用户没有填错邮箱便可获取验证码进行下一步操作
     *
     * @param object
     * @param request
     * @return
     */
    @RequestMapping(value = "/savePublishConference.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object savePublishConference(@RequestBody PublishMessage object, HttpServletRequest request){

        HttpSession session = request.getSession();
        Object code = session.getAttribute("code");

        if (code == null) {
            return new Serializable() {
                boolean isSuccess = false;
                String msg = "验证码失效";
            };
        }

        if (!((String)code).equals(object.getVerificationCode())) {
            return new Serializable() {
                boolean isSuccess = false;
                String msg = "验证码错误";
            };
        }

        boolean res = service.savePublishConferenceService(object);
        return new Serializable() {public boolean isSuccess = res;};

    }


    @RequestMapping(value = "/checkEmail.do", produces = "application/json;charset=utf-8")
    public void checkEmail(String email,HttpServletRequest request) throws Exception {
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            verificationCode.append(random.nextInt(10));
        }
        HttpSession session = request.getSession();

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("请勿回复此邮件，验证码为：").append(verificationCode);
        emailContent.append(",验证码3分钟内有效，如不是本人操作请检查邮箱有没有被盗！");

        String pwd = configCache.getEmailServerToken();
        JavaEmailSender.sendEmail(email, "邮箱验证", emailContent.toString(), pwd);
        session.setAttribute("code", verificationCode.toString());
        session.setMaxInactiveInterval(60 * 3);
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
}
