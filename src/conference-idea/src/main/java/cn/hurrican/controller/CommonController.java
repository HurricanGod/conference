package cn.hurrican.controller;

import cn.hurrican.constant.BusinessCode;
import cn.hurrican.constant.QQEmailConfig;
import cn.hurrican.dtl.ResMessage;
import cn.hurrican.utils.QQEmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/conference/common")
public class CommonController {

    @Autowired
    private QQEmailConfig qqEmailConfig;


    @RequestMapping(value = "/sendEmail.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ResMessage sendEmail(String subject, String content, String receiveUsers){
        ResMessage resMessage = new ResMessage();
        try{
            QQEmailUtils.sendEmail(subject, qqEmailConfig, content, receiveUsers, null);
        }catch(Exception e){
            return resMessage.logIs(e.getMessage()).put("successSendEmail", false)
                    .retCodeEqual(BusinessCode.ServerError.getCode());
        }
        return resMessage.put("successSendEmail", true)
                .retCodeEqual(BusinessCode.Ok.getCode());
    }
}
