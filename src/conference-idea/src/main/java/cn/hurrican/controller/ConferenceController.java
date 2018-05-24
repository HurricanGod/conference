package cn.hurrican.controller;

import cn.hurrican.beans.ConferenceInfo;
import cn.hurrican.dtl.ConferenceMsg;
import cn.hurrican.dtl.ConferenceTag;
import cn.hurrican.service.ConferenceInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by NewObject on 2017/10/4.
 */

@Controller
@RequestMapping(value = "/conference/conference/")
public class ConferenceController {

    @Autowired
    @Qualifier(value = "conferenceInfoService")
    private ConferenceInfoService mysqlService;


    @RequestMapping(value = "/latest.do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object queryLatestConference(Integer days, Integer page){
        /**
         * @decription: 查询近期要召开的会议
         * @param days 整数，表示最近几天
         * @param page 整数，用于分页查询
         * @return:  ConferenceMsg 集合
         */
        List<ConferenceMsg> conferenceMsgs = mysqlService.queryLatestConference((page - 1) * 8, 8, days);
        System.out.println("exec latest.do");
        for (int i = 0; i < conferenceMsgs.size(); i++) {
            System.out.println(conferenceMsgs.get(i));

        }
        return conferenceMsgs;
    }

    @RequestMapping(value = "/queryLatestCount.do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object queryLatestConferenceCount(Integer offset){
        /**
         * @decription:
         * @param offset
         * @return:  匿名类对象，该对象只有一个成员(number)
         */
        Integer count = mysqlService.queryLatestConferenceInfoCountService(offset);
        return new Serializable() {public Integer number = count;};
    }



    @RequestMapping(value = "/getHotConference.do",
                    produces = "application/json;charset=UTF-8",
                    method = RequestMethod.POST)
    @ResponseBody
    public Object queryLatestConcernedConference(Integer days, Integer page, String tagsString){
        /**
         * @decription: 根据 Tag 分页查询近期的会议
         *
         * @param days 整数，表日期范围，用于指定近期的具体时间，从今天到 days天后这段时间
         *
         * @param page 页数，根据 Tag 和 时间范围查找会议可能很多，
         *             这里默认返回8条数据，要想全部获取数据，可以设置不同page的值获取
         *             例：page=1表示获取第一页，前8条数据，page=2获取第8-15条数据……
         *
         * @param tagsString 查询条件：会议的Tag，可以允许有多个tag，使用","分隔开
         *                   例：前端可以提交一个像 "人工智能,深度学习" 这样的 tagsString
         *                   本方法会对含有多个 Tag 的 tagsString进行解析
         *
         * @return: ConferenceMsg 集合
         */
        String[] array = tagsString.split(",");
        List<String> tags = Arrays.asList(array);
        List<ConferenceMsg> conferenceMsgs = null;

        if (tags.size() > 0) {
            conferenceMsgs = mysqlService.queryLatestConferenceByTag((page - 1) * 8, 8, days, tags);
        } else {

            conferenceMsgs = mysqlService.queryLatestConference((page - 1) * 8, 8, days);

        }
        return conferenceMsgs;
    }


    @RequestMapping(value = "/getTag.do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object queryAllTags(HttpServletRequest request){
        /**
         * @decription: 查询近期举办的会议Tag
         * @param request
         * @return: ConferenceTag 集合
         */
        ServletContext servletContext = request.getServletContext();


        Object tags = servletContext.getAttribute("tags");
        if (tags == null) {
            synchronized(servletContext){
                if (tags == null) {
                    tags = mysqlService.queryLatestConferenceHotTagTopN();
                    servletContext.setAttribute("tags", tags);
                }
            }

        }
        List<ConferenceTag> list = (List<ConferenceTag>) tags;
        System.out.println("exec getTag.do =======> return " + list.size() + " records");
        return tags;
    }


    public void setMysqlService(ConferenceInfoService mysqlService) {
        this.mysqlService = mysqlService;
    }

    //查询会议简介接口
    @RequestMapping(value = "/getConferenceIntro.do" ,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getConferenceIntro(HttpServletRequest request, HttpServletResponse response){
        ServletContext servletContext = request.getServletContext();
        Integer id = (Integer) servletContext.getAttribute("id");
//        if(id==null){
//            id=4429;
//        }
        //下面的方法调用从数据库中获取对应会议简介
        List<String> conferenceInfos = mysqlService.getConferenceIntro(id);
        List<String> conferenceImage = mysqlService.getConferenceImage(id);

        //以下开始变换返回的对象
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        int[] temp = {0,0,0};   //用来存放换行符位置
        int p=0;
        char[] arg0 = {};
        String info1 = "";
        String info2 = "";
        String info3 = "";
        System.out.println("会议简介");
//        for (int i=0 ; i < conferenceInfos.size() ;i++ )
        arg0 = conferenceInfos.get(0).toCharArray();
        for (int j = 0; j < arg0.length; j++) {
            if(arg0[0] == '\n' || arg0[1] == '\n' || arg0[0] == '\r' || arg0[1] == '\r')
                continue;
            if (arg0[j] == '\n') {
                temp[p] = j - 1;
                p++;
            }
        }
        if (temp[0] != 0)
            info1 = String.valueOf(arg0, 0, temp[0] + 1);
        if (temp[1] != 0)
            info2 = String.valueOf(arg0, temp[0] + 3, temp[1] - temp[0] - 2);
        if (temp[2] != 0)
            info2 = String.valueOf(arg0, temp[1] + 3, temp[2] - temp[1] - 2);
        if (temp[0] == 0 && temp[1] == 0 && temp[2] == 0) {
            info1 = String.valueOf(arg0);
        }
        jsonArray.add("info1");
        if (!"".equals(info2))
            jsonArray.add("info2");
        if (!"".equals(info3))
            jsonArray.add("info3");
        jsonObject.put("infoKeys",jsonArray);
        jsonObject.put("info1",info1);
        if (!"".equals(info2))
            jsonObject.put("info2",info2);
        if (!"".equals(info3))
            jsonObject.put("info3",info3);
        jsonObject.put("banner",conferenceImage.get(0));
        return jsonObject;
    }
}
