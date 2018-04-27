package cn.hurrican.controller;

import cn.hurrican.dtl.ConferenceTag;
import cn.hurrican.dtl.IAnonymous;
import cn.hurrican.dtl.QueryTopNumMeetingParam;
import cn.hurrican.service.RecommendConferenceService;
import cn.hurrican.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by NewObject on 2017/10/5.
 */
@Controller
@RequestMapping(value = "/conference/push")
public class RecommendController {

    @Autowired
    @Qualifier(value = "recommendService")
    private RecommendConferenceService service;

    @RequestMapping(value = "/getPopularMeeting.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryLatestPopularConferenceByPraise(HttpServletRequest request) throws ParseException {
        /**
         * @decription: 查询给定日期区间内点赞数前 top 的会议
         *
         * @param request 前端可以传递2个或3个参数; 3个参数为：startTime、offsetDay、top)
         *                startTime —— 起始日期(可选)，如果没有传递这个参数，会默认使用系统当前日期
         *                offsetDay —— 必选，表示日期范围，可以正数或负数；
         *                              正数表示含义： startTime 到 startTime + offsetDay 范围内的时间
         *                              负数表示含义： startTime - offsetDay 到 startTime 范围内的时间
         *                top —— 必选，表示获得点赞数前 top 的会议
         *
         * @return: ConferenceMsg 的list集合
         */
        String startTime = request.getParameter("startTime");
        Integer offsetDay = Integer.valueOf(request.getParameter("offsetDay"));
        Integer top = Integer.valueOf(request.getParameter("top"));

        if (startTime == null) {
            startTime = DateUtils.convertDateToString(new Date());
        }

        return service.recommendBePraisedConferenceTopNumberService(top, offsetDay, startTime);
    }


    @RequestMapping(value = "/queryConferenceByTag.do",produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryPopularConferenceByTopTag(QueryTopNumMeetingParam param) throws ParseException {

        System.out.println(param.toString());
        return service.recommendPopularTagConferenceService(param);
    }


    @RequestMapping(value = "/queryNumOfConferenceByTag.do",produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryPopularConferenceCountByTopTag(QueryTopNumMeetingParam param) throws ParseException {
        System.out.println(param.toString());
        Integer num = service.queryPopularTagConferenceCountService(param);
        return new IAnonymous() {public int count = num;};
    }


    /**
     * @decription: 推荐每日最受欢迎的会议 Tag，最受欢迎的Tag由两部分组成
     *                 第1部分：被点赞次数排名靠前的会议所属的 Tag
     *                 第2部分：将要召开会议中，所属Tag排名靠前的
     * @param request
     * @param startTime
     * @param offset
     * @return: java.lang.Object
     */
    @RequestMapping(value = "/queryPopTag.do", produces = "application/json;charset=utf-8")
    @ResponseBody
    public Object queryPopularTopTag(HttpServletRequest request, String startTime, Integer offset)
            throws ParseException {
        ServletContext servletContext = request.getSession().getServletContext();
        String topParam = request.getParameter("top");
        Integer top = topParam != null ? Integer.valueOf(topParam) > 10 ? 10 : Integer.valueOf(topParam): 5;
        List<String> tags = new ArrayList<>();
        List<ConferenceTag> tagList = (List<ConferenceTag>)servletContext.getAttribute("tags");
        tagList = tagList != null ? tagList : new ArrayList<>();
        List<String> popTagList = service.queryLatestMostPopularOfTagBePraisedService(startTime, offset, top);
        popTagList.stream().limit(3).forEach(tags::add);
        tagList.stream().limit(top-3).forEach(cTag -> tags.add(cTag.getTag()));
        return tags;
    }
}
