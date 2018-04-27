package cn.hurrican.controller;

import cn.hurrican.beans.ErrorConferenceInfo;
import cn.hurrican.beans.FeedbackSuggestion;
import cn.hurrican.dtl.ErrorConferenceRequestParams;
import cn.hurrican.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by NewObject on 2017/10/31.
 */
/**
 *  反馈控制器，主要处理小程序用户的反馈意见及纠错会议信息
 */
@Controller
@RequestMapping(value = "/conference/feedback")
public class FeedBackController {

    @Resource(name = "feedbackService")
    private FeedbackService service;

    @RequestMapping(value = "/submitError.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object saveOneErrorConference(@RequestBody ErrorConferenceInfo obj){
        /**
         * @decription:
         * @param errorInfo
         * @return: java.lang.Object
         */
        System.out.println(obj);
        service.saveOneErrorConferenceInfoService(obj);
        return new Serializable() {public boolean isSuccess = true;};
    }


    @RequestMapping(value = "/submitSuggestion.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object saveFeedbackInfo(@RequestBody FeedbackSuggestion object){
        /**
         * @decription:
         * @param suggestion
         * @return: java.lang.Object
         */
        service.saveOneFeedbackInfoService(object);
        return new Serializable() {public boolean isSuccess = true;};
    }


    @RequestMapping(value = "/queryErrorInfo.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryUnsolvedErrorConference(@RequestBody ErrorConferenceRequestParams params){
        /**
         * @decription:
         * @param params
         * @return: List<ErrorCorrectionInfo>
         */
        return service.queryErrorConference(params);
    }

    @RequestMapping(value = "/queryErrorInfoCount.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryUnsolvedErrorConferenceCount(){

        Integer count = service.queryUnsolvedErrorConferenceCount();
        return new Serializable() {public Integer number = count;};

    }


    @RequestMapping(value = "/solveError.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object solveErrorConference(Integer cid){

        /**
         * @decription:
         * @param cid
         * @return: java.lang.Object
         */
        service.solveErrorConferenceInfoService(cid);
        return new Serializable() {public boolean isSuccess = true;};
    }


    @RequestMapping(value = "/querySuggestion.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryUserSuggestion(HttpServletRequest request){

        String startTime = request.getParameter("startTime");
        String page = request.getParameter("page");
        String number = request.getParameter("number");

        return service.queryUserSuggestionService(number, page, startTime);
    }


    @RequestMapping(value = "/querySuggestCount.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryUserSuggestionCount(HttpServletRequest request){

        String startTime = request.getParameter("startTime");
        Integer count = service.queryUserSuggestionCountService(startTime);

        return new Serializable() {public int number = count;};
    }
}
